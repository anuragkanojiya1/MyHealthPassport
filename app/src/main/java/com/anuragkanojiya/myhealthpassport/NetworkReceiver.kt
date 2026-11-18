package com.anuragkanojiya.myhealthpassport

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NetworkReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val safeContext = context ?: return
            Toast.makeText(safeContext, safeContext.getString(R.string.network_airplane_mode), Toast.LENGTH_LONG).show()
        }
    }
}