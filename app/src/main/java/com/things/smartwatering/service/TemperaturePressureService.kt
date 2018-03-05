package com.things.smartwatering.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.things.smartwatering.driver.Bme280SensorDriver

import java.io.IOException

private val TAG = TemperaturePressureService::class.java.simpleName
private val I2C_BUS = "I2C1"

class TemperaturePressureService : Service() {
    private lateinit var mTemperatureSensorDriver: Bme280SensorDriver

    override fun onCreate() {
        setupTemperaturePressureSensor()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyTemperaturePressureSensor()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_NOT_STICKY
    }

    private fun setupTemperaturePressureSensor() {
        try {
            mTemperatureSensorDriver = Bme280SensorDriver(I2C_BUS)
            mTemperatureSensorDriver.registerTemperatureSensor()
            mTemperatureSensorDriver.registerPressureSensor()
            mTemperatureSensorDriver.registerHumiditySensor()
        } catch (e: IOException) {
            Log.e(TAG, "Error configuring sensor", e)
        }
    }

    private fun destroyTemperaturePressureSensor() {
        mTemperatureSensorDriver.unregisterTemperatureSensor()
        mTemperatureSensorDriver.unregisterPressureSensor()
        mTemperatureSensorDriver.registerHumiditySensor()
        try {
            mTemperatureSensorDriver.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing sensor", e)
        }
    }
}
