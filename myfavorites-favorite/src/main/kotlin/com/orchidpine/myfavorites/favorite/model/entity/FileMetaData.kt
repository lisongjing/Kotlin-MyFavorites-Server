package com.orchidpine.myfavorites.favorite.model.entity

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class FileMetaData(val dataId: String, val mimeType: String,
                        val md5: String, val filename: String) : java.io.Serializable