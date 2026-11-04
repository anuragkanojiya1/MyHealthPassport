package com.anuragkanojiya.myhealthpassport.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider
import com.anuragkanojiya.myhealthpassport.domain.model.UserHealthData
import com.anuragkanojiya.myhealthpassport.domain.repository.HealthRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object HealthChartWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun healthRepository(): HealthRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(appContext, WidgetEntryPoint::class.java)
        val repository = entryPoint.healthRepository()
        
        val latestData = repository.getLatestHealthData()

        provideContent {
            HealthChartContent(latestData)
        }
    }
}

@Composable
fun HealthChartContent(data: UserHealthData?) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2E))
            .cornerRadius(16.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        // Header
        Text(
            text = "📊 MyHealth Stats",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorProvider(day = Color.White, night = Color.White)
            )
        )

        Spacer(GlanceModifier.height(6.dp))

        // Content
        data?.let {
            // Blood Pressure
            Text(
                text = "🩺 BP: ${it.systolicBP}/${it.diastolicBP} mmHg",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = ColorProvider(day = Color(0xFFFF6B6B), night = Color(0xFFFF6B6B))
                )
            )

            Spacer(GlanceModifier.height(4.dp))

            // Sugar
            Text(
                text = "🍬 Sugar: ${it.bloodSugarLevel} mg/dL",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = ColorProvider(day = Color(0xFFFFD93D), night = Color(0xFFFFD93D))
                )
            )

            Spacer(GlanceModifier.height(4.dp))

            // Medications
            val meds = it.medications.split(",").map { m -> m.trim() }.filter { m -> m.isNotEmpty() }.take(2)
            if (meds.isNotEmpty()) {
                Text(
                    text = "💊 Meds: ${meds.joinToString()}",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = ColorProvider(day = Color(0xFF6BCB77), night = Color(0xFF6BCB77))
                    )
                )
            }
        } ?: Text(
            text = "No health data found.",
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = ColorProvider(day = Color.LightGray, night = Color.LightGray)
            )
        )
    }
}
