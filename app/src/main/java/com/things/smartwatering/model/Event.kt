package com.things.smartwatering.model

data class Event(val name: String = "",
                 val dateTime: Long = 0,
                 val isDone: Boolean = false)