package com.example.myhealthpassport.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.myhealthpassport.Composables.UserHealthData
import com.example.myhealthpassport.ViewModels.HealthViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.runBlocking

object HealthChartWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val viewModel = HealthViewModel()
        val latestData = runBlocking {
            viewModel.getLatestHealthData(context)
        }

        provideContent {
            HealthChartContent(latestData)
        }
    }
}

@Composable
fun HealthChartContent(data: UserHealthData?) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E2E)) // still fine here, GlanceModifier uses Color directly
            .cornerRadius(16.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        // Header
        Text(
            text = "📊 MyHealth Stats",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = ColorProvider(Color.White) // ✅ Glance-compatible
            )
        )

        Spacer(GlanceModifier.height(10.dp))

        // Content
        data?.let {
            // Blood Pressure
            Text(
                text = "🩺 BP: ${it.systolicBP}/${it.diastolicBP} mmHg",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = ColorProvider(Color(0xFFFF6B6B)) // ✅ soft red
                )
            )

            Spacer(GlanceModifier.height(6.dp))

            // Sugar
            Text(
                text = "🍬 Sugar: ${it.bloodSugarLevel} mg/dL",
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = ColorProvider(Color(0xFFFFD93D)) // ✅ warm yellow
                )
            )

            Spacer(GlanceModifier.height(6.dp))

            // Medications
            val meds = it.medications.split(",").map { m -> m.trim() }.take(2)
            if (meds.isNotEmpty()) {
                Text(
                    text = "💊 Meds: ${meds.joinToString()}",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = ColorProvider(Color(0xFF6BCB77)) // ✅ green
                    )
                )
            }
        } ?: Text(
            text = "No health data found.",
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = ColorProvider(Color.LightGray) // ✅ light gray
            )
        )
    }
}
