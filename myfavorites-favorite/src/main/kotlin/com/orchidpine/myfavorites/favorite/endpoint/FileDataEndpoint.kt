package com.orchidpine.myfavorites.favorite.endpoint

import com.orchidpine.myfavorites.favorite.model.response.FileOperateResult
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

interface FileDataEndpoint {
    fun fileUpload(file: MultipartFile): FileOperateResult

    fun fileDownload(dataId: String, response: HttpServletResponse)

    fun fileDelete(dataId: String): FileOperateResult
}