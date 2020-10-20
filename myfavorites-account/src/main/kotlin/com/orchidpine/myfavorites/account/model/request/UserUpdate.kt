package com.orchidpine.myfavorites.account.model.request

import com.orchidpine.myfavorites.account.model.entity.User
import com.orchidpine.myfavorites.account.util.cipher.AESOperation

data class UserUpdate(val username: String?,
                      val password: String?,
                      val email: String?,
                      val phone: String?,
                      val verifyCode: String,
                      val newVerifyCode: String?,
                      val version: Int) : java.io.Serializable {
    fun toUser(user: User): User {
        if (username != null)
            user.username = username
        if (password != null)
            user.password = AESOperation.instance.encrypt(password)
        if (email != null)
            user.email = email
        if (phone != null)
            user.phone = phone
        user.version = version
        return user
    }
}