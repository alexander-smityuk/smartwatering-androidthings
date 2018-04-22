package com.things.smartwatering

import android.app.Activity
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.DynamicSensorCallback
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.things.smartwatering.driver.pump.Pump
import com.things.smartwatering.model.DataInfo
import com.things.smartwatering.repository.FirebaseRepository
import com.things.smartwatering.repository.FirebaseRepositoryImpl
import com.things.smartwatering.service.SensorService
import com.things.smartwatering.utils.AppConstant

class MainActivity : Activity() {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mFireBaseRepository: FirebaseRepository
    private lateinit var waterPump: Pump
    private var dataInfo: DataInfo = DataInfo()
    private lateinit var mSensorEventListener: SensorsEventListener

    private val mDynamicSensorCallback = object : DynamicSensorCallback() {
        override fun onDynamicSensorConnected(sensor: Sensor) {
            when {
                sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Temperature sensor connected")
                    mSensorEventListener = SensorsEventListener()
                    mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                }
                sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Humidity sensor connected")
                    mSensorEventListener = SensorsEventListener()
                    mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
                }
                sensor.type == AppConstant.TYPE_SOIL_MOISTURE -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Soil moisture sensor connected")
                    mSensorEventListener = SensorsEventListener()
                    mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFireBaseRepository = FirebaseRepositoryImpl()

        startSensorRequest()
        //waterPump = WaterPump(PUMP_GPIO_PIN)
    }

    override fun onStop() {
        super.onStop()
        Nearby.getConnectionsClient(this).stopAdvertising()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSensorRequest()
        waterPump.close()
    }

    private fun startSensorRequest() {
        startService(Intent(this, SensorService::class.java))
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mSensorManager.registerDynamicSensorCallback(mDynamicSensorCallback)
    }

    private fun stopSensorRequest() {
        stopService(Intent(this, SensorService::class.java))
        mSensorManager.unregisterDynamicSensorCallback(mDynamicSensorCallback)
        mSensorManager.unregisterListener(mSensorEventListener)
    }

    private inner class SensorsEventListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when {
                event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Temperature : ${"%.2f".format(event.values[0])}")
                    dataInfo.temperature = event.values[0]
                }
                event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Humidity : ${"%.2f".format(event.values[0])}")
                    dataInfo.humidity = event.values[0]
                }
                event.sensor.type == AppConstant.TYPE_SOIL_MOISTURE -> {
                    Log.i(AppConstant.MAIN_ACTIVITY_TAG, "Soil Moisture : ${"%.2f".format(event.values[0])}")
                    dataInfo.soilMoisture = event.values[0]
                }
            }

            Handler().postDelayed({mFireBaseRepository.putDataInfo(dataInfo)}, 1000)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Log.i(AppConstant.MAIN_ACTIVITY_TAG, "sensor accuracy changed: " + accuracy)
        }
    }

    private fun startAdvertising() {
        Nearby.getConnectionsClient(this).startAdvertising(
                "Raspberry Pi",
                "com.things.smartwatering",
                connectionLifecycleCallback,
                AdvertisingOptions(Strategy.P2P_STAR))
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionResult(endPointId: String?, p1: ConnectionResolution?) {
        }

        override fun onDisconnected(endPointId: String?) {
        }

        override fun onConnectionInitiated(p0: String?, p1: ConnectionInfo?) {
            Nearby.getConnectionsClient(this@MainActivity).acceptConnection(p0
                    ?: "", object : PayloadCallback() {
                override fun onPayloadReceived(endPointId: String?, p1: Payload?) {
                    Log.d("PAYLOAD", String(p1!!.asBytes()!!))
                    Nearby.getConnectionsClient(this@MainActivity)
                            .sendPayload(p0!!, p1)
                }

                override fun onPayloadTransferUpdate(endPointId: String?, p1: PayloadTransferUpdate?) {

                }
            })
        }
    }
}
