package com.orchidpine.myfavorites.favorite.exception

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.ZonedDateTime

@JsonIgnoreProperties("stackTrace", "message", "suppressed", "localizedMessage")
class BaseException(val errorCode: String, val description: String, val httpCode: Int, val timestamp: ZonedDateTime = ZonedDateTime.now())
    : RuntimeException("HTTP status: $httpCode, errorCode: $errorCode, description: $description, timestamp: $timestamp") {
    constructor(error: ExceptionEnum) : this(error.errorCode, error.description, error.httpCode)
}