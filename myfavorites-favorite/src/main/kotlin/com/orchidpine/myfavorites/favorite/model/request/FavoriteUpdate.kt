package com.orchidpine.myfavorites.favorite.model.request

import com.orchidpine.myfavorites.favorite.model.entity.Favorite
import com.orchidpine.myfavorites.favorite.model.entity.FavoriteData
import com.orchidpine.myfavorites.favorite.util.md5

data class FavoriteUpdate(val title: String?,
                          val public: Boolean?,
                          val dataId: String?,
                          val data: String?,
                          val version: Int) : java.io.Serializable {

    fun toFavorite(favorite: Favorite, abbreviateData: (data: String) -> String): Favorite {
        if (title != null)
            favorite.title = title
        if (public != null)
            favorite.public = public
        if (dataId != null && data != null) {
            favorite.dataId = dataId
            favorite.abbreviation = abbreviateData(data)
        }
        favorite.version = version
        return favorite
    }

    fun toFavoriteData(): FavoriteData? {
        if (dataId != null && data != null)
            return FavoriteData(dataId, data, data.md5(), data.length)
        return null
    }
}