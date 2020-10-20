package com.orchidpine.myfavorites.account.service

import com.orchidpine.myfavorites.account.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(private val repository: RoleRepository) {
    fun getAllRoles() = repository.findAll().toList()
}