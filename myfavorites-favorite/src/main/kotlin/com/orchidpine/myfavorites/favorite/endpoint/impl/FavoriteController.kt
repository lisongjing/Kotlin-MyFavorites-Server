package com.orchidpine.myfavorites.favorite.endpoint.impl

import com.orchidpine.myfavorites.favorite.endpoint.FavoriteEndpoint
import com.orchidpine.myfavorites.favorite.model.request.FavoriteAdding
import com.orchidpine.myfavorites.favorite.model.request.FavoriteUpdate
import com.orchidpine.myfavorites.favorite.service.FavoriteService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FavoriteController(private val service: FavoriteService) : FavoriteEndpoint {
    @PostMapping("/favorite")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun addNewFavorite(@RequestBody adding: FavoriteAdding) = service.saveNewFavorite(adding)

    @GetMapping("/favorite/{favoriteId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun getFavorite(@PathVariable("favoriteId") favoriteId: String) = service.getFavoriteContent(favoriteId)

    @PutMapping("/favorite/{favoriteId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun updateFavorite(@PathVariable("favoriteId") favoriteId: String,
                                @RequestBody request: FavoriteUpdate) = service.updateFavorite(favoriteId, request)

    @DeleteMapping("/favorite/{favoriteId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun deleteFavorite(@PathVariable("favoriteId") favoriteId: String) = service.deleteFavorite(favoriteId)

    @GetMapping("/favorites/{userId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    override fun getPageFavorites(@PathVariable("userId") userId: String, @RequestParam(value = "page", required = false) page: Int?)
            = service.getFavoriteBasicInfoPageByUser(userId, page ?: 0)
}