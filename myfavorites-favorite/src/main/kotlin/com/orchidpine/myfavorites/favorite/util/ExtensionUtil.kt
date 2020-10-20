package com.orchidpine.myfavorites.favorite.util

import org.springframework.util.DigestUtils
import org.springframework.web.multipart.MultipartFile
import java.util.*

fun UUID.toSimpleString(): String {
    val builder: StringBuilder = StringBuilder(32)
    builder.append(digits(this.mostSignificantBits shr 32, 8))
    builder.append(digits(this.mostSignificantBits shr 16, 4))
    builder.append(digits(this.mostSignificantBits, 4))
    builder.append(digits(this.mostSignificantBits, 4))
    builder.append(digits(this.leastSignificantBits shr 48, 4))
    builder.append(digits(this.leastSignificantBits, 12))
    return builder.toString()
}

private fun digits(value: Long, digits: Int): String {
    val hi = 1L shl digits * 4
    return java.lang.Long.toHexString(hi or (value and hi - 1)).substring(1)
}

fun String.md5(): String {
    return DigestUtils.md5DigestAsHex(this.toByteArray())
}

fun MultipartFile.md5(): String {
    return DigestUtils.md5DigestAsHex(this.bytes)
}