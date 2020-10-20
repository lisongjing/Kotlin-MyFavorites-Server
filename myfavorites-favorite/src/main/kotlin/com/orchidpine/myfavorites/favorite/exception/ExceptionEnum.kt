package com.orchidpine.myfavorites.favorite.exception

enum class ExceptionEnum(val errorCode: String, val description: String, val httpCode: Int) {
    FAVORITE_NOT_FOUND("error-b1-01", "No favorite found in the system database", 404),
    FAVORITE_DATA_NOT_FOUND("error-b1-02", "No favorite content found in the system database", 404),
    FILE_DATA_NOT_FOUND("error-b1-03", "No file found in the system database", 404),
    FILE_EMPTY("error-b1-04", "Request file is empty", 400),
    FILE_SERVICE_BREAK("error-b1-05", "File operation circuit breaker open", 503),
    INVALID_USER("error-v3-01", "Not a legitimate user of the operation", 403)
}