package com.orchidpine.myfavorites.favorite.model.response

data class FavoriteUpdateResult(val success: Boolean = false, val errorCode: String? = null,
                                val version: Int? = null) : java.io.Serializable