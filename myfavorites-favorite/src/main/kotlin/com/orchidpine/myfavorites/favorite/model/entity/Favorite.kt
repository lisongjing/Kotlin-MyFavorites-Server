package com.orchidpine.myfavorites.favorite.model.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(name = "favorites")
data class Favorite(@GeneratedValue(strategy = GenerationType.IDENTITY)
                    @Id var id: Long? = null,
                    var favoriteId: String = "",
                    var userId: String = "",
                    var title: String? = null,
                    var abbreviation: String = "",
                    var dataId: String = "",
                    var public: Boolean = false,
                    @CreatedDate var createTime: Date? = null,
                    @LastModifiedDate var modifiedTime: Date? = null,
                    @Version var version: Int = 0) : java.io.Serializable

