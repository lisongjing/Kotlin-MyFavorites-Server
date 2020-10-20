package com.orchidpine.myfavorites.account.endpoint

import com.orchidpine.myfavorites.account.model.request.UserRegister
import com.orchidpine.myfavorites.account.model.request.UserUpdate
import com.orchidpine.myfavorites.account.model.response.PasswordCheck
import com.orchidpine.myfavorites.account.model.response.UserInfo
import com.orchidpine.myfavorites.account.model.response.UsernameCheck
import com.orchidpine.myfavorites.account.model.response.VerificationSend
import org.springframework.lang.Nullable
import org.springframework.util.MultiValueMap

interface UserEndpoint {
    fun checkNewUsernameValid(username: String): UsernameCheck

    fun checkPasswordValid(password: String): PasswordCheck

    fun sendVerificationEmail(email: String): VerificationSend

    fun registerUser(request: UserRegister): UserInfo

    fun getCurrentUser(): UserInfo

    fun getUserByUserId(userId: String): UserInfo

    fun updateUser(userId: String, headers: MultiValueMap<String, String>, @Nullable tokenParam: String?, request: UserUpdate): UserInfo
}