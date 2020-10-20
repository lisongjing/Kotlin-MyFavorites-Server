package com.orchidpine.myfavorites.account.model.request

import com.orchidpine.myfavorites.account.model.entity.Role
import com.orchidpine.myfavorites.account.model.entity.User
import com.orchidpine.myfavorites.account.util.cipher.AESOperation
import java.time.ZonedDateTime
import java.util.*

data class UserRegister(val username: String?,
                        val password: String,
                        val email: String,
                        val verifyCode: String) : java.io.Serializable {
    fun toUser(): User {
        return User(
                null,
                System.currentTimeMillis().toString() + "-" + UUID.randomUUID().toString(),
                username
                        ?: email.substringBeforeLast("@") + UUID.randomUUID().toString().substring(24..33),
                AESOperation.instance.encrypt(password),
                email,
                null,
                null,
                ZonedDateTime.now(),
                true,
                Role(2, "ROLE_USER")
        )
    }
}
