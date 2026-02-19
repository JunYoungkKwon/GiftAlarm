package com.example.gifticonalarm.data.repository

import androidx.lifecycle.asFlow
import com.example.gifticonalarm.data.local.dao.GifticonDao
import com.example.gifticonalarm.data.mapper.toDomain
import com.example.gifticonalarm.data.mapper.toEntity
import com.example.gifticonalarm.domain.model.Gifticon
import com.example.gifticonalarm.domain.repository.GifticonRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room DAO 기반 기프티콘 저장소 구현체.
 */
class GifticonRepositoryImpl @Inject constructor(
    private val gifticonDao: GifticonDao
) : GifticonRepository {

    override fun observeAllGifticons(): Flow<List<Gifticon>> {
        return gifticonDao.getAllGifticons().asFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeGifticonById(id: Long): Flow<Gifticon?> {
        return gifticonDao.getGifticonById(id).asFlow().map { it?.toDomain() }
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

    override fun observeExpiringGifticons(daysThreshold: Int): Flow<List<Gifticon>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysThreshold)
        val thresholdDate = calendar.timeInMillis

        return gifticonDao.getExpiringGifticons(thresholdDate).asFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
