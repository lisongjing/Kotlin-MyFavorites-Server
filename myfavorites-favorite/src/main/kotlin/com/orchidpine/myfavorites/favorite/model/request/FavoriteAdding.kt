package com.orchidpine.myfavorites.favorite.model.request

import com.orchidpine.myfavorites.favorite.model.entity.Favorite
import com.orchidpine.myfavorites.favorite.model.entity.FavoriteData
import com.orchidpine.myfavorites.favorite.util.md5
import com.orchidpine.myfavorites.favorite.util.toSimpleString
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

data class FavoriteAdding(val title: String?,
                          var data: String,
                          val dataId: String = UUID.randomUUID().toSimpleString()) : java.io.Serializable {

    fun toFavorite(abbreviateData: (data: String) -> String): Favorite {
        return Favorite(
                null,
                UUID.randomUUID().toSimpleString(),
                SecurityContextHolder.getContext().authentication.name,
                title,
                abbreviateData(data),
                dataId)
    }

    fun toFavoriteData(): FavoriteData {
        return FavoriteData(dataId, data, data.md5(), data.length)
    }
}

