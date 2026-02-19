package com.example.gifticonalarm.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.gifticonalarm.data.local.dao.GifticonDao
import com.example.gifticonalarm.data.mapper.toDomain
import com.example.gifticonalarm.data.mapper.toEntity
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GifticonRepositoryImpl @Inject constructor(
    private val gifticonDao: GifticonDao
) : GifticonRepository {

    override fun getAllGifticons(): LiveData<List<Gifticon>> {
        return gifticonDao.getAllGifticons().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getGifticonById(id: Long): LiveData<Gifticon?> {
        return gifticonDao.getGifticonById(id).map { it?.toDomain() }
    }

    override suspend fun insertGifticon(gifticon: Gifticon): Long {
        val now = Date()
        return gifticonDao.insertGifticon(gifticon.copy(lastModifiedAt = now).toEntity())
    }

    override suspend fun updateGifticon(gifticon: Gifticon) {
        val now = Date()
        gifticonDao.updateGifticon(gifticon.copy(lastModifiedAt = now).toEntity())
    }

    override suspend fun deleteGifticon(gifticon: Gifticon) {
        gifticonDao.deleteGifticon(gifticon.toEntity())
    }

    override fun getExpiringGifticons(daysThreshold: Int): LiveData<List<Gifticon>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysThreshold)
        val thresholdDate = calendar.timeInMillis

        return gifticonDao.getExpiringGifticons(thresholdDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
