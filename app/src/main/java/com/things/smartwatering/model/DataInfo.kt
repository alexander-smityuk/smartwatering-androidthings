package com.things.smartwatering.model

data class DataInfo (var time: Long = 0L,
                     var temperature: Float = 0f,
                     var humidity: Float = 0f,
                     var soilMoisture: Float = 0f)

