package com.things.smartwatering.utils

import com.things.smartwatering.MainActivity
import com.things.smartwatering.service.TemperaturePressureService


object AppConstant {

    val MAIN_ACTIVITY_TAG = MainActivity::class.java.simpleName!!
    val TEMP_SERVICE_TAG = TemperaturePressureService::class.java.simpleName!!

    const val I2C_BUS = "I2C1"
}