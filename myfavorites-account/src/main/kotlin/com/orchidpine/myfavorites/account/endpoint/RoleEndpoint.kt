package com.orchidpine.myfavorites.account.endpoint

import com.orchidpine.myfavorites.account.model.entity.Role

interface RoleEndpoint {
    fun getAllRole(): List<Role>
}