package com.orchidpine.myfavorites.account.repository

import com.orchidpine.myfavorites.account.model.entity.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : CrudRepository<Role, Long> {
    fun findByRole(role: String): Role
}