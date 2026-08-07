package com.anuragkanojiya.myhealthpassport.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class HealthWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = HealthChartWidget
}