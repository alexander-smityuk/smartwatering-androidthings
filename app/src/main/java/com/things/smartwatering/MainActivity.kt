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
import com.things.smartwatering.driver.ads1115.Ads1115
import com.things.smartwatering.driver.ads1115.Ads1115Device
import com.things.smartwatering.repository.FirebaseRepository
import com.things.smartwatering.service.TemperaturePressureService
import com.things.smartwatering.utils.AppConstant

class MainActivity : Activity() {

    private lateinit var mSensorManager: SensorManager

    private val mFireBaseRepository: FirebaseRepository = FirebaseRepository()

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
        //startTemperaturePressureRequest()

        val device = Ads1115.Factory().newAds1115(AppConstant.I2C_BUS, 0x48, Ads1115.Gain.TWO_THIRDS)
        println(device.readVoltage(Ads1115.Channel.ZERO))
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
                event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    mFireBaseRepository.setTime(System.currentTimeMillis())
                    mFireBaseRepository.setTemperature(event.values[0])
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Temperature : ${"%.2f".format(event.values[0])}")
                }
                event.sensor.type == Sensor.TYPE_PRESSURE -> Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Pressure : ${"%.2f".format(event.values[0])}")
                event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY -> Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Humidity : ${"%.2f".format(event.values[0])}")
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Log.i(AppConstant.MAIN_ACTIVITY_TAG, "sensor accuracy changed: " + accuracy)
        }
    }

}
