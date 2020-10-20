package com.orchidpine.myfavorites.favorite.repository

import com.orchidpine.myfavorites.favorite.model.entity.FileData
import org.springframework.stereotype.Repository

@Repository
interface FileDataRepository {
    fun insertFileData(data: FileData): String
    fun getFileData(dataId: String): FileData
    fun deleteFileData(dataId: String): String
}