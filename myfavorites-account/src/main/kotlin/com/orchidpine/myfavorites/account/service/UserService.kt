package com.orchidpine.myfavorites.account.service

import com.orchidpine.myfavorites.account.exception.BaseException
import com.orchidpine.myfavorites.account.exception.ExceptionEnum
import com.orchidpine.myfavorites.account.model.entity.User
import com.orchidpine.myfavorites.account.model.environment.CustomParams
import com.orchidpine.myfavorites.account.model.response.VerificationSend
import com.orchidpine.myfavorites.account.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.mail.internet.MimeMessage


@Service
class UserService(private val params: CustomParams,
                  private val repository: UserRepository,
                  private val redisTemplate: StringRedisTemplate,
                  private val tokenServices: ConsumerTokenServices,
                  private val sender: JavaMailSender) {

    @Value("\${spring.mail.username}")
    private val from: String? = null

    fun generateVerificationCode(email: String): VerificationSend {
        var numberOfTimes = redisTemplate.opsForValue().get("$email-numberOfTimes")?.toInt() ?: 0
        if (numberOfTimes >= params.verifyCodeRetryMax)
            return VerificationSend(email, false, ExceptionEnum.MAX_VERIFY_CODE_TRY.errorCode)
        var code: String
        do {
            code = SecureRandom().nextInt(1000000).toString()
        } while (code.length != 6)
        redisTemplate.opsForValue().set("$email-verifyCode", code, params.verifyCodeValidSeconds, TimeUnit.SECONDS)
        // 24hours
        redisTemplate.opsForValue().set("$email-numberOfTimes", (++numberOfTimes).toString(), 24, TimeUnit.HOURS)
        sendHtmlMail(email, "MyFavorite-Email Verification", """
        <html>
            <head>
                <meta charset="utf-8" />
            </head>
            <body>
                <div style="background-color:#6666CC;color:#FFFFFF">
                    <p style="text-align:left;font-size:40px">Account Email Verification</p>
                    <div style="text-align:center>
                        <p style="color:#99FFFF;font-size:20px">This email was sent by MyFavorites APP for verification, if you did not request this verification, please ignore this email.</p>
                        <br />
                        <b style="color:#FFCC00;font-size:30px">$code</b>
                    </div>
                </div>
            </body>
        </html>
        """.trimIndent())
        return VerificationSend(email, true)
    }

    private fun sendHtmlMail(to: String, subject: String, content: String) {
        val message: MimeMessage = sender.createMimeMessage()
        try {
            val helper = MimeMessageHelper(message, true)
            helper.setFrom(from!!)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(content, true)
            sender.send(message)
        } catch (e: Exception) {
            throw BaseException(ExceptionEnum.NW_MAIL_ERROR)
        }
    }

    @Cacheable(value = ["username-cache"], key="#username", condition = "#result == false")
    fun checkNewUsernameValid(username: String): Boolean {
        return username.matches("""\S{4,30}""".toRegex()) && repository.findTop1ByUsername(username) == null
    }

    fun checkNewPasswordValid(password: String): Boolean {
        // TODO implement verify rules
        return password.matches("""\w{6,30}""".toRegex())
    }

    fun checkVerificationCode(email: String, code: String): Boolean {
        val actualCode = redisTemplate.opsForValue().get("$email-verifyCode") ?: return false
        return code.equals(actualCode)
    }

    @CachePut(value = ["user-cache"], key="#user.userId", condition = "#result != null")
    fun saveUser(user: User): User {
        return repository.save(user)
    }

    @Cacheable(value = ["user-cache"], key="#userId", condition = "#result != null")
    fun getActiveUser(userId: String): User? {
        return repository.findByUserIdAndEnableTrue(userId)
    }

    fun revokeToken(accessToken: String?): Boolean {
        if (accessToken == null)
            return false
        if (tokenServices.revokeToken(accessToken)) {
            return true
        }
        return false
    }
}