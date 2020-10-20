package com.orchidpine.myfavorites.account.endpoint.impl

import com.orchidpine.myfavorites.account.endpoint.RoleEndpoint
import com.orchidpine.myfavorites.account.service.RoleService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RoleController(private val service : RoleService) : RoleEndpoint {
    @GetMapping("/roles")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun getAllRole() = service.getAllRoles()
}