package com.orchidpine.myfavorites.account.repository

import com.orchidpine.myfavorites.account.model.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsernameOrEmailOrPhone(username: String, email: String, phone: String): User?

    fun findByUserIdAndEnableTrue(userId: String): User?

    fun findTop1ByUsername(username: String): User?
}