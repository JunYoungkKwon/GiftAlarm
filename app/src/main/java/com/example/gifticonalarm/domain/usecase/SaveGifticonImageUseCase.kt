package com.example.gifticonalarm.domain.usecase

import com.example.gifticonalarm.domain.repository.ImageStorageRepository
import javax.inject.Inject

/**
 * 선택한 이미지를 앱 내부 저장소에 영구 저장한다.
 */
class SaveGifticonImageUseCase @Inject constructor(
    private val imageStorageRepository: ImageStorageRepository
) {
    suspend operator fun invoke(sourceUri: String): String {
        return imageStorageRepository.persistImage(sourceUri)
    }
}

