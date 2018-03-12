package com.things.smartwatering.model

data class DataMessage(val time: Long,
                       val temperature: Float,
                       val humidity: Float,
                       val pressure: Float)

