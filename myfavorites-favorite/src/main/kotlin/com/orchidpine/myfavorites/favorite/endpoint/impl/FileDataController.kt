package com.orchidpine.myfavorites.favorite.endpoint.impl

import cn.hutool.core.io.IoUtil
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import com.orchidpine.myfavorites.favorite.endpoint.FileDataEndpoint
import com.orchidpine.myfavorites.favorite.exception.BaseException
import com.orchidpine.myfavorites.favorite.exception.ExceptionEnum
import com.orchidpine.myfavorites.favorite.model.response.FileOperateResult
import com.orchidpine.myfavorites.favorite.service.FileDataService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse

@RestController
@DefaultProperties(defaultFallback = "fileDataDefaultHandler", ignoreExceptions = [BaseException::class], commandProperties = [
    HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
    HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
    HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
    HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "70")])
class FileDataController(private val service: FileDataService) : FileDataEndpoint {

    @PostMapping("/file")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    @HystrixCommand
    override fun fileUpload(@RequestParam("file") file: MultipartFile): FileOperateResult {
        if (file.isEmpty)
            throw BaseException(ExceptionEnum.FILE_EMPTY)
        return service.uploadFile(file)
    }

    @GetMapping("/file/{dataId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    @HystrixCommand(fallbackMethod = "fileDataDownloadHandler")
    override fun fileDownload(@PathVariable("dataId") dataId: String, response: HttpServletResponse) {
        val file = service.downloadFile(dataId)
        response.contentType = file.metaDataOutput?.getString("mimeType")
                ?: "application/octet-stream" // "application/force-download"
        response.characterEncoding = "UTF-8";
        response.setHeader("Content-Disposition", "attachment;fileName=${URLEncoder.encode(file.metaDataOutput?.getString("filename"), "UTF-8")}");
        val outputStream = response.outputStream
        IoUtil.copy(file.data, outputStream)
        file.data.close()
        outputStream.close()
    }

    @DeleteMapping("/file/{dataId}")
    @PreAuthorize("#oauth2.hasAnyScope('User-WEB','User-MOBILE')")
    @HystrixCommand
    override fun fileDelete(@PathVariable("dataId") dataId: String): FileOperateResult {
        return service.deleteFile(dataId)
    }

    fun fileDataDefaultHandler(): FileOperateResult {
        throw BaseException(ExceptionEnum.FILE_SERVICE_BREAK)
    }

    fun fileDataDownloadHandler(dataId: String, response: HttpServletResponse) {
        throw BaseException(ExceptionEnum.FILE_SERVICE_BREAK)
    }
}