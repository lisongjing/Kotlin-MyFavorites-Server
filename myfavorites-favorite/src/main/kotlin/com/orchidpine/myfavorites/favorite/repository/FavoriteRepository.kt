package com.orchidpine.myfavorites.favorite.repository

import com.orchidpine.myfavorites.favorite.model.entity.Favorite
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FavoriteRepository : CrudRepository<Favorite, Long> {

    fun findAllByUserIdOrderByModifiedTimeDesc(userId: String, pageable: Pageable): Page<Favorite>

    fun findByFavoriteId(favoriteId: String): Favorite?
}