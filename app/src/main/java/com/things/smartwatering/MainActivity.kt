package com.things.smartwatering

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.content.Intent
import com.things.smartwatering.service.TemperaturePressureService
import com.things.smartwatering.utils.AppConstant

class MainActivity : Activity() {

    private lateinit var mSensorManager: SensorManager

    //var sensor : SoilMoistureSensor = SoilMoistureSensor(AppConstant.I2C_BUS)

    private val mDynamicSensorCallback = object : DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            when {
                sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Temperature sensor connected")
                    mSensorEventListener = TemperaturePressureEventListener()
                    mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                }
                sensor.type == Sensor.TYPE_PRESSURE -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Pressure sensor connected")
                    mSensorEventListener = TemperaturePressureEventListener()
                    mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
                }
                sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Humidity sensor connected")
                    mSensorEventListener = TemperaturePressureEventListener()
                    mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
                }
            }
        }
    }
    private lateinit var mSensorEventListener: TemperaturePressureEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTemperaturePressureRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTemperaturePressureRequest()
    }

    private fun startTemperaturePressureRequest() {
        this.startService(Intent(this, TemperaturePressureService::class.java))
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mSensorManager.registerDynamicSensorCallback(mDynamicSensorCallback)
    }

    private fun stopTemperaturePressureRequest() {
        this.stopService(Intent(this, TemperaturePressureService::class.java))
        mSensorManager.unregisterDynamicSensorCallback(mDynamicSensorCallback)
        mSensorManager.unregisterListener(mSensorEventListener)
    }

    private inner class TemperaturePressureEventListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when {
                event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE -> Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Temperature : ${"%.2f".format(event.values[0])}")
                event.sensor.type == Sensor.TYPE_PRESSURE -> Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Pressure : ${"%.2f".format(event.values[0])}")
                event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY -> Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Humidity : ${"%.2f".format(event.values[0])}")
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Log.i(AppConstant.MAIN_ACTIVITY_TAG, "sensor accuracy changed: " + accuracy)
        }
    }

}
