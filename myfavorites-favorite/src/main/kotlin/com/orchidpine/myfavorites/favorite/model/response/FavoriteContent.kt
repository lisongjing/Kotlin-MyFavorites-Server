package com.orchidpine.myfavorites.favorite.model.response

import com.orchidpine.myfavorites.favorite.model.entity.Favorite
import com.orchidpine.myfavorites.favorite.model.entity.FavoriteData
import java.util.*

data class FavoriteContent(val favoriteId: String,
                           val userId: String,
                           val title: String?,
                           val abbreviation: String,
                           val dataId: String,
                           val public: Boolean,
                           val createTime: Date?,
                           val modifiedTime: Date?,
                           val version: Int,
                           val data: String,
                           val md5: String,
                           val length: Int) : java.io.Serializable {
    constructor(favorite: Favorite, data: FavoriteData) : this(favorite.favoriteId, favorite.userId, favorite.title,
            favorite.abbreviation, favorite.dataId, favorite.public, favorite.createTime,
            favorite.modifiedTime, favorite.version, data.data, data.md5, data.length)
}