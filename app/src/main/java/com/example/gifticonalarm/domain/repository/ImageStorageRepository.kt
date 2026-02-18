package com.example.gifticonalarm.domain.repository

/**
 * 이미지 파일 영구 저장을 담당하는 저장소 인터페이스.
 */
interface ImageStorageRepository {
    /**
     * 전달받은 URI의 이미지를 앱 내부 저장소로 복사하고, 저장된 파일 URI를 반환한다.
     */
    suspend fun persistImage(sourceUri: String): String
}

