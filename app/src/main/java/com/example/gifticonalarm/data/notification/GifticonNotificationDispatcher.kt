package com.example.gifticonalarm.data.notification

import android.annotation.SuppressLint
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.gifticonalarm.MainActivity
import com.example.gifticonalarm.R
import com.example.gifticonalarm.domain.model.Gifticon
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 만료 알림 알림(Notification) 표시를 담당한다.
 */
class GifticonNotificationDispatcher @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    @SuppressLint("MissingPermission")
    fun notifyExpiringGifticons(gifticons: List<Gifticon>) {
        if (gifticons.isEmpty()) return
        if (!canPostNotification()) return

        ensureChannel()

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (gifticons.size == 1) {
            "기프티콘 만료 알림"
        } else {
            "만료 예정 기프티콘 ${gifticons.size}개"
        }
        val message = if (gifticons.size == 1) {
            val item = gifticons.first()
            "${item.brand} ${item.name}의 유효기간이 임박했어요."
        } else {
            "설정한 만료 주기에 해당하는 기프티콘이 있어요."
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existingChannel = manager.getNotificationChannel(CHANNEL_ID)
        if (existingChannel != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "기프티콘 만료 알림",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "유효기간 만료 예정 기프티콘 알림"
        }
        manager.createNotificationChannel(channel)
    }

    private fun canPostNotification(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private companion object {
        const val CHANNEL_ID = "gifticon_expiry_channel"
        const val NOTIFICATION_ID = 10001
    }
}
