package com.orchidpine.myfavorites.account.model.response

import com.orchidpine.myfavorites.account.model.entity.User

data class UserInfo(val userId: String,
                    val username: String,
                    var email: String?,
                    var phone: String?,
                    val avatar: String?,
                    val version: Int) : java.io.Serializable {
    constructor(user: User) : this(user.userId, user.username, user.email, user.phone, user.avatar, user.version)
}