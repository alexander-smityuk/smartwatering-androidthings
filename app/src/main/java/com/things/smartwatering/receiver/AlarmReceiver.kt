package com.things.smartwatering.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.things.smartwatering.driver.pump.Pump
import com.things.smartwatering.driver.pump.WaterPump
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    private val waterPump: Pump = WaterPump.getInstance()

    override fun onReceive(context: Context?, intent: Intent?) {
        waterPump.water(10L, TimeUnit.SECONDS)
    }
}