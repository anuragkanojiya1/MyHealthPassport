package com.example.myhealthpassport.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class HealthWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = HealthChartWidget
}