package com.things.smartwatering.utils

import com.things.smartwatering.MainActivity
import com.things.smartwatering.service.SensorService


object AppConstant {

    val MAIN_ACTIVITY_TAG = MainActivity::class.java.simpleName!!
    val TEMP_SERVICE_TAG = SensorService::class.java.simpleName!!

    const val I2C_BUS = "I2C1"

    const val PUMP_GPIO_PIN = "BCM10"

    const val TYPE_SOIL_MOISTURE = 0x10000
}