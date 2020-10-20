package com.orchidpine.myfavorites.favorite.repository.impl

import com.orchidpine.myfavorites.favorite.model.entity.FavoriteData
import com.orchidpine.myfavorites.favorite.repository.FavoriteDataRepository
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class FavoriteDataRepositoryImpl(private val mongoTemplate: MongoTemplate) : FavoriteDataRepository {
    override fun insertFavoriteData(favoriteData: FavoriteData): FavoriteData {
        return mongoTemplate.save(favoriteData)
    }

    override fun getFavoriteData(dataId: String): FavoriteData? {
        return mongoTemplate.findOne(Query(Criteria.where("dataId").isEqualTo(dataId)), FavoriteData::class.java)
    }

    override fun updateFavoriteData(favoriteData: FavoriteData): FavoriteData? {
        return mongoTemplate.findAndReplace(Query(Criteria.where("dataId").isEqualTo(favoriteData.dataId)), favoriteData, FindAndReplaceOptions.options().returnNew())
    }

    override fun deleteFavoriteData(dataId: String): FavoriteData? {
        return mongoTemplate.findAndRemove(Query(Criteria.where("dataId").isEqualTo(dataId)), FavoriteData::class.java)
    }
}