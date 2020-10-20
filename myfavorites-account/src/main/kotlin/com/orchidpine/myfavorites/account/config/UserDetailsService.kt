package com.orchidpine.myfavorites.account.config

import com.orchidpine.myfavorites.account.exception.BaseException
import com.orchidpine.myfavorites.account.exception.ExceptionEnum
import com.orchidpine.myfavorites.account.repository.UserRepository
import com.orchidpine.myfavorites.account.util.cipher.AESOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null)
            throw BaseException(ExceptionEnum.USERNAME_PASSWORD_ERROR)
        val user = repository.findByUsernameOrEmailOrPhone(username, username, username)
                ?: throw BaseException(ExceptionEnum.USER_NOT_FOUND)
        return User(user.userId, passwordEncoder.encode(AESOperation.instance.decrypt(user.password)), user.enable, true, true, true, listOf(object : GrantedAuthority {
            override fun getAuthority(): String? {
                return user.role?.role
            }
        }))
    }

}