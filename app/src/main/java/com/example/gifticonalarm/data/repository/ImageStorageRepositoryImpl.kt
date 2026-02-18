package com.example.gifticonalarm.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.example.gifticonalarm.domain.repository.ImageStorageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

/**
 * 선택한 이미지를 앱 내부 저장소로 복사해 안정적인 파일 URI를 제공한다.
 */
class ImageStorageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageStorageRepository {

    override suspend fun persistImage(sourceUri: String): String = withContext(Dispatchers.IO) {
        if (sourceUri.startsWith("file://")) return@withContext sourceUri

        val source = sourceUri.toUri()
        val extension = resolveExtension(source)
        val destinationDir = File(context.filesDir, "gifticon_images").apply { mkdirs() }
        val destinationFile = File(
            destinationDir,
            "gifticon_${System.currentTimeMillis()}.$extension"
        )

        context.contentResolver.openInputStream(source)?.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: throw FileNotFoundException("이미지 스트림을 열 수 없습니다: $sourceUri")

        Uri.fromFile(destinationFile).toString()
    }

    private fun resolveExtension(uri: Uri): String {
        val mimeType = context.contentResolver.getType(uri)
        val resolved = mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
        return if (resolved.isNullOrBlank()) "jpg" else resolved
    }
}
