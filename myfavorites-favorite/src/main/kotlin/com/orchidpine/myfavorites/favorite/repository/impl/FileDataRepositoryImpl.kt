package com.orchidpine.myfavorites.favorite.repository.impl

import com.mongodb.client.gridfs.model.GridFSFile
import com.orchidpine.myfavorites.favorite.exception.BaseException
import com.orchidpine.myfavorites.favorite.exception.ExceptionEnum
import com.orchidpine.myfavorites.favorite.model.entity.FileData
import com.orchidpine.myfavorites.favorite.repository.FileDataRepository
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.stereotype.Repository

@Repository
class FileDataRepositoryImpl(private val gridFsTemplate: GridFsTemplate) : FileDataRepository {
    override fun insertFileData(data: FileData): String {
        gridFsTemplate.store(data.data, data.metaDataInput!!.dataId, data.metaDataInput.mimeType, data.metaDataInput)
        return data.metaDataInput.dataId
    }

    override fun getFileData(dataId: String): FileData {
        val file: GridFSFile = gridFsTemplate.findOne(Query(Criteria.where("filename").isEqualTo(dataId)))
                ?: throw BaseException(ExceptionEnum.FILE_DATA_NOT_FOUND)
        val resource = gridFsTemplate.getResource(file)
        return FileData(metaDataOutput = file.metadata, data = resource.inputStream, length = file.length)
    }

    override fun deleteFileData(dataId: String): String {
        gridFsTemplate.delete(Query(Criteria.where("dataId").isEqualTo(dataId)))
        return dataId
    }
}