package com.orchidpine.myfavorites.favorite.service

import com.orchidpine.myfavorites.favorite.exception.BaseException
import com.orchidpine.myfavorites.favorite.exception.ExceptionEnum
import com.orchidpine.myfavorites.favorite.model.entity.Favorite
import com.orchidpine.myfavorites.favorite.model.request.FavoriteAdding
import com.orchidpine.myfavorites.favorite.model.request.FavoriteUpdate
import com.orchidpine.myfavorites.favorite.model.response.FavoriteContent
import com.orchidpine.myfavorites.favorite.model.response.FavoriteUpdateResult
import com.orchidpine.myfavorites.favorite.repository.FavoriteDataRepository
import com.orchidpine.myfavorites.favorite.repository.FavoriteRepository
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class FavoriteService(private val repository: FavoriteRepository, private val dataRepository: FavoriteDataRepository) {
    @Value("\${custom-params.page-size}")
    private val pageSize: Int? = null

    @Cacheable(value = ["favorites-cache"], condition = "#result != null")
    fun getFavoriteBasicInfoPageByUser(userId: String, page: Int): Page<Favorite> {
        return repository.findAllByUserIdOrderByModifiedTimeDesc(userId, PageRequest.of(page, pageSize ?: 50))
    }

    @Caching(
            put = [CachePut(value = ["favorite-cache"], key = "#result.favoriteId", condition = "#result != null")],
            evict = [CacheEvict(value = ["favorites-cache"], allEntries = true, beforeInvocation = true)]
    )
    fun saveNewFavorite(adding: FavoriteAdding): FavoriteContent {
        adding.data = preprocessFavoriteData(adding.data)
        return FavoriteContent(repository.save(adding.toFavorite { abbreviateFavoriteData(adding.data) }),
                dataRepository.insertFavoriteData(adding.toFavoriteData()))
    }

    @Cacheable(value = ["favorite-cache"], key = "#favoriteId", condition = "#result != null")
    fun getFavoriteContent(favoriteId: String): FavoriteContent {
        val favorite = repository.findByFavoriteId(favoriteId) ?: throw BaseException(ExceptionEnum.FAVORITE_NOT_FOUND)
        return FavoriteContent(favorite, dataRepository.getFavoriteData(
                favorite.dataId) ?: throw BaseException(ExceptionEnum.FAVORITE_DATA_NOT_FOUND))
    }

    @Caching(
            evict = [CacheEvict(value = ["favorite-cache"], key = "#favoriteId", beforeInvocation = true),
                CacheEvict(value = ["favorites-cache"], allEntries = true, beforeInvocation = true)]
    )
    fun updateFavorite(favoriteId: String, update: FavoriteUpdate): FavoriteUpdateResult {
        var favorite = repository.findByFavoriteId(favoriteId) ?: throw BaseException(ExceptionEnum.FAVORITE_NOT_FOUND)
        if (!favorite.userId.equals(SecurityContextHolder.getContext().authentication.name))
            throw BaseException(ExceptionEnum.INVALID_USER)
        var data = update.toFavoriteData()
        if (data != null)
            data = dataRepository.updateFavoriteData(data)
        if (data == null)
            throw BaseException(ExceptionEnum.FAVORITE_DATA_NOT_FOUND)
        favorite = repository.save(update.toFavorite(favorite) { abbreviateFavoriteData(update.data) })
        return FavoriteUpdateResult(true, null, favorite.version)
    }

    @Caching(
            evict = [CacheEvict(value = ["favorite-cache"], key = "#favoriteId", beforeInvocation = true),
                CacheEvict(value = ["favorites-cache"], allEntries = true, beforeInvocation = true)]
    )
    fun deleteFavorite(favoriteId: String): FavoriteUpdateResult {
        val favorite = repository.findByFavoriteId(favoriteId) ?: throw BaseException(ExceptionEnum.FAVORITE_NOT_FOUND)
        if (!favorite.userId.equals(SecurityContextHolder.getContext().authentication.name))
            throw BaseException(ExceptionEnum.INVALID_USER)
        dataRepository.deleteFavoriteData(favorite.dataId) ?: throw BaseException(ExceptionEnum.FAVORITE_DATA_NOT_FOUND)
        repository.delete(favorite)
        return FavoriteUpdateResult(true, null)
    }

    private fun preprocessFavoriteData(data: String): String {
        val whitelist = Whitelist.basicWithImages()
                .addTags("audio", "video", "source")
                .addAttributes("audio", "style", "controls", "height", "width")
                .addAttributes("video", "style", "controls", "height", "width")
                .addAttributes("source", "src", "type")
                .addProtocols("source", "src", "ftp", "http", "https")
        arrayListOf("a", "b", "blockquote", "cite", "code", "dd", "dl", "dt", "em",
                "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub",
                "sup", "u", "ul", "img").forEach { tag -> whitelist.addAttributes(tag, "style") }

        return Jsoup.clean(data, whitelist)
    }

    private fun abbreviateFavoriteData(data: String?): String {
        return if (data == null) "" else Jsoup.parse(data).body().text()
    }
}