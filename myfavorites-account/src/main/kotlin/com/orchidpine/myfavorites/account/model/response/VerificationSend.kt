package com.orchidpine.myfavorites.account.model.response

data class VerificationSend(val email: String,
                            val success: Boolean,
                            val errorCode: String? = null) : java.io.Serializable