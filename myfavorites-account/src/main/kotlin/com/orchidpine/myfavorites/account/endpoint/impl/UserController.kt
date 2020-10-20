package com.orchidpine.myfavorites.account.endpoint.impl

import com.orchidpine.myfavorites.account.endpoint.UserEndpoint
import com.orchidpine.myfavorites.account.exception.BaseException
import com.orchidpine.myfavorites.account.exception.ExceptionEnum
import com.orchidpine.myfavorites.account.model.request.UserRegister
import com.orchidpine.myfavorites.account.model.request.UserUpdate
import com.orchidpine.myfavorites.account.model.response.PasswordCheck
import com.orchidpine.myfavorites.account.model.response.UserInfo
import com.orchidpine.myfavorites.account.model.response.UsernameCheck
import com.orchidpine.myfavorites.account.model.response.VerificationSend
import com.orchidpine.myfavorites.account.service.UserService
import org.springframework.lang.Nullable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val service: UserService) : UserEndpoint {
    @RequestMapping(value = ["/username/{username}"], method = [RequestMethod.OPTIONS])
    @PreAuthorize("#oauth2.hasAnyScope('Visitor-Restricted','User-WEB','User-MOBILE')")
    override fun checkNewUsernameValid(@PathVariable("username") username: String): UsernameCheck {
        return UsernameCheck(username, service.checkNewUsernameValid(username))
    }

    @RequestMapping(value = ["/password/{password}"], method = [RequestMethod.OPTIONS])
    @PreAuthorize("#oauth2.hasAnyScope('Visitor-Restricted','User-WEB','User-MOBILE')")
    override fun checkPasswordValid(@PathVariable("password") password: String): PasswordCheck {
        return PasswordCheck(service.checkNewPasswordValid(password))
    }

    @GetMapping("/verify/{email}")
    @PreAuthorize("#oauth2.hasAnyScope('Visitor-Restricted','User-WEB','User-MOBILE')")
    override fun sendVerificationEmail(@PathVariable("email") email: String): VerificationSend {
        return service.generateVerificationCode(email)
    }

    @PostMapping("/user")
    @PreAuthorize("#oauth2.hasScope('Visitor-Restricted')")
    override fun registerUser(@RequestBody request: UserRegister): UserInfo {
        if (!service.checkNewPasswordValid(request.password))
            throw BaseException(ExceptionEnum.PASSWORD_INVALID)
        if (request.username != null && !service.checkNewUsernameValid(request.username))
            throw BaseException(ExceptionEnum.USERNAME_INVALID)
        if (!service.checkVerificationCode(request.email, request.verifyCode))
            throw BaseException(ExceptionEnum.SAFE_VERIFY_CODE_ERROR)
        return UserInfo(service.saveUser(request.toUser()))
    }

    @GetMapping("/user")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun getCurrentUser(): UserInfo {
        val userId = SecurityContextHolder.getContext().authentication.name
        return UserInfo(service.getActiveUser(userId) ?: throw BaseException(ExceptionEnum.USER_NOT_FOUND))
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun getUserByUserId(@PathVariable("userId") userId: String): UserInfo {
        val userInfo = UserInfo(service.getActiveUser(userId)
                ?: throw BaseException(ExceptionEnum.USER_NOT_FOUND))
        userInfo.email = null
        userInfo.phone = null
        return userInfo
    }

    @PutMapping("/user/{userId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun updateUser(@PathVariable("userId") userId: String,
                   @RequestHeader headers: MultiValueMap<String, String>,
                   @RequestParam("access_token") @Nullable tokenParam: String?,
                   @RequestBody request: UserUpdate): UserInfo {
        val user = service.getActiveUser(userId) ?: throw BaseException(ExceptionEnum.USER_NOT_FOUND)
        if (!service.checkVerificationCode(user.email, request.verifyCode))
            throw BaseException(ExceptionEnum.SAFE_VERIFY_CODE_ERROR)
        if (request.username != null && !service.checkNewUsernameValid(request.username))
            throw BaseException(ExceptionEnum.USERNAME_INVALID)
        if (request.password != null && !service.checkNewPasswordValid(request.password) || user.password.equals(request.password))
            throw BaseException(ExceptionEnum.PASSWORD_INVALID)
        if (request.email != null && (request.newVerifyCode == null || !service.checkVerificationCode(request.email, request.newVerifyCode)))
            throw BaseException(ExceptionEnum.SAFE_VERIFY_CODE_ERROR)
        val tokenAuthHeader = headers.get("authorization")?.get(0)?.toString()?.removePrefix("Bearer ")
        val tokenExtendHeader = headers.get("x-legacy-token")?.get(0)?.toString()
        service.revokeToken(tokenAuthHeader ?: tokenExtendHeader ?: tokenParam)
        return UserInfo(service.saveUser(request.toUser(user)))
    }
}