package com.orchidpine.myfavorites.favorite.endpoint

import com.orchidpine.myfavorites.favorite.model.entity.Favorite
import com.orchidpine.myfavorites.favorite.model.response.FavoriteContent
import com.orchidpine.myfavorites.favorite.model.response.FavoriteUpdateResult
import com.orchidpine.myfavorites.favorite.model.request.FavoriteAdding
import com.orchidpine.myfavorites.favorite.model.request.FavoriteUpdate
import org.springframework.data.domain.Page

interface FavoriteEndpoint {
    fun addNewFavorite(adding: FavoriteAdding): FavoriteContent

    fun getFavorite(favoriteId: String): FavoriteContent

    fun updateFavorite(favoriteId: String, request: FavoriteUpdate): FavoriteUpdateResult

    fun deleteFavorite(favoriteId: String): FavoriteUpdateResult

    fun getPageFavorites(userId: String, page: Int?): Page<Favorite>
}