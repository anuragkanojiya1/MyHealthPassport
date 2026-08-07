package com.anuragkanojiya.myhealthpassport.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.anuragkanojiya.myhealthpassport.MainActivity
import com.anuragkanojiya.myhealthpassport.R
import com.anuragkanojiya.myhealthpassport.domain.repository.HealthRepository
import com.anuragkanojiya.myhealthpassport.domain.usecase.GeminiAnalysisUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File

@HiltWorker
class GeminiWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val geminiAnalysisUseCase: GeminiAnalysisUseCase,
    private val healthRepository: HealthRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val CHANNEL_ID = "gemini_tasks"
        private const val FOREGROUND_NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        val prompt = inputData.getString("prompt") ?: return Result.failure()
        val medicalID = inputData.getString("medicalID") ?: return Result.failure()
        val imagePath = inputData.getString("imagePath")

        // 1. Show "Processing" notification
        setForeground(createForegroundInfo("AI is processing your request..."))

        val analysisResult = if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            if (bitmap != null) {
                val res = geminiAnalysisUseCase.analyzeImage(bitmap, prompt)
                // Clean up temp file
                File(imagePath).delete()
                res
            } else {
                geminiAnalysisUseCase.analyzeData(prompt)
            }
        } else {
            geminiAnalysisUseCase.analyzeData(prompt)
        }

        return analysisResult.fold(
            onSuccess = { result ->
                // 2. Save result to Firestore
                healthRepository.retrieveHealthData(medicalID).onSuccess { data ->
                    healthRepository.saveHealthData(data.copy(aiInsight = result))
                }

                // 3. Update notification with results
                showCompletionNotification("Analysis Complete", result)

                Result.success()
            },
            onFailure = {
                showCompletionNotification("Analysis Failed", "Something went wrong while processing.")
                if (runAttemptCount < 3) Result.retry() else Result.failure()
            }
        )
    }

    private fun createForegroundInfo(message: String): ForegroundInfo {
        createNotificationChannel()

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setContentTitle("Health AI Processing")
            .setTicker("Health AI Processing")
            .setContentText(message)
            .setSmallIcon(R.drawable.healthcare)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Foreground service requirement
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(FOREGROUND_NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            ForegroundInfo(FOREGROUND_NOTIFICATION_ID, notification)
        }
    }

    private fun showCompletionNotification(title: String, message: String) {
        // Use the system launch intent to resume the app exactly as it was
        val intent = appContext.packageManager.getLaunchIntentForPackage(appContext.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        } ?: Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        
        val pendingIntent = PendingIntent.getActivity(
            appContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(R.drawable.healthcare)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Use a unique ID for completion so it's not cancelled by WorkManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Health AI Tasks",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for AI health data processing"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
