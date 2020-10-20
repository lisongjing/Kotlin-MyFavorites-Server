package com.orchidpine.myfavorites.favorite.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class FavoriteData(val dataId: String, val data: String,
                        val md5: String, val length: Int) : java.io.Serializable