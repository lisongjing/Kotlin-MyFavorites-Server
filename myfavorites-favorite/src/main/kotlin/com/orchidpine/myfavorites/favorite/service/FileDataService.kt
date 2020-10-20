package com.orchidpine.myfavorites.favorite.service

import com.orchidpine.myfavorites.favorite.model.entity.FileData
import com.orchidpine.myfavorites.favorite.model.entity.FileMetaData
import com.orchidpine.myfavorites.favorite.model.response.FileOperateResult
import com.orchidpine.myfavorites.favorite.repository.FileDataRepository
import com.orchidpine.myfavorites.favorite.util.md5
import com.orchidpine.myfavorites.favorite.util.toSimpleString
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileDataService(private val repository: FileDataRepository) {
    @CachePut(value = ["file-cache"], key="#result.dataId", condition = "#result.success == true")
    fun uploadFile(file: MultipartFile): FileOperateResult {
        val metaData = FileMetaData(UUID.randomUUID().toSimpleString(), file.contentType ?: "", file.md5(), file.originalFilename ?: file.name)
        return FileOperateResult(true, repository.insertFileData(FileData(metaDataInput = metaData, data = file.inputStream)))
    }

    @Cacheable(value = ["file-cache"], key="#dataId", condition = "#result != null")
    fun downloadFile(dataId: String): FileData {
        return repository.getFileData(dataId)
    }

    @CacheEvict(value = ["file-cache"], key="#dataId", condition = "#result.success == true")
    fun deleteFile(dataId: String): FileOperateResult {
        return FileOperateResult(true, repository.deleteFileData(dataId))
    }
}