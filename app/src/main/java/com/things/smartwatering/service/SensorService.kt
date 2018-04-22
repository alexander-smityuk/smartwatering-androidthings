package com.things.smartwatering.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.things.smartwatering.driver.Bme280SensorDriver
import com.things.smartwatering.driver.SoilMoistureSensorDriver
import com.things.smartwatering.utils.AppConstant

import java.io.IOException

class SensorService : Service() {

    private lateinit var mTemperatureSensorDriver: Bme280SensorDriver
    private lateinit var mSoilMoistureSensorDriver: SoilMoistureSensorDriver

    override fun onCreate() {
        setupSensors()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroySensors()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_NOT_STICKY
    }

    private fun setupSensors() {
        try {
            mTemperatureSensorDriver = Bme280SensorDriver(AppConstant.I2C_BUS)
            mSoilMoistureSensorDriver = SoilMoistureSensorDriver(AppConstant.I2C_BUS)

            mTemperatureSensorDriver.registerTemperatureSensor()
            mTemperatureSensorDriver.registerHumiditySensor()
            mSoilMoistureSensorDriver.registerSoilMoistureSensor()

        } catch (e: IOException) {
            Log.e(AppConstant.TEMP_SERVICE_TAG, "Error configuring sensor", e)
        }
    }

    private fun destroySensors() {
        mTemperatureSensorDriver.unregisterTemperatureSensor()
        mTemperatureSensorDriver.unregisterHumiditySensor()
        mSoilMoistureSensorDriver.unregisterSoilMoistureSensor()

        try {
            mTemperatureSensorDriver.close()
            mSoilMoistureSensorDriver.close()
        } catch (e: IOException) {
            Log.e(AppConstant.TEMP_SERVICE_TAG, "Error closing sensor", e)
        }
    }
}
