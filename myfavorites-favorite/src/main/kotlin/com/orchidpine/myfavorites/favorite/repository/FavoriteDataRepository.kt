package com.orchidpine.myfavorites.favorite.repository

import com.orchidpine.myfavorites.favorite.model.entity.FavoriteData
import org.springframework.stereotype.Repository

@Repository
interface FavoriteDataRepository {
    fun insertFavoriteData(favoriteData: FavoriteData): FavoriteData
    fun getFavoriteData(dataId: String): FavoriteData?
    fun updateFavoriteData(favoriteData: FavoriteData): FavoriteData?
    fun deleteFavoriteData(dataId: String): FavoriteData?
}