package com.orchidpine.myfavorites.account.exception

enum class ExceptionEnum(val errorCode: String, val description: String, val httpCode: Int) {
    REQUEST_FORMAT_ERROR("error-v1-01", "Invalid request fields", 400),
    USERNAME_PASSWORD_ERROR("error-v2-01", "Username or password entered by the user is incorrect", 400),
    SAFE_VERIFY_CODE_ERROR("error-v2-02", "Security verification code entered by the user is incorrect", 403),
    MAX_VERIFY_CODE_TRY("error-v2-03", "Maximum times of attempts to security verify has been reached in 24 hours", 403),
    USER_NOT_FOUND("error-v2-04", "No user found in the system database", 404),
    HUMAN_VERIFY_ERROR("error-v2-05", "Human-machine verification failed", 403),
    USERNAME_INVALID("error-v2-06", "Username is invalid", 400),
    PASSWORD_INVALID("error-v2-07", "Password is invalid", 400),
    OPERATION_FREQUENT("error-o1-01", "User actions are too frequent", 503),
    DB_REDIS_ERROR("error-s1-01", "Redis database operation error", 507),
    DB_MYSQL_ERROR("error-s1-02", "MySQL database operation error", 507),
    NW_MAIL_ERROR("error-n1-01", "Send email error", 500),
}