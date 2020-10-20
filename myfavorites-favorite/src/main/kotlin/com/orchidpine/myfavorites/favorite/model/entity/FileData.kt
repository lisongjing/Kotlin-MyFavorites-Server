package com.orchidpine.myfavorites.favorite.model.entity

import org.bson.Document
import java.io.InputStream

data class FileData(val metaDataInput: FileMetaData? = null,
                    val metaDataOutput: Document? = null,
                    val data: InputStream,
                    val length: Long? = null) : java.io.Serializable