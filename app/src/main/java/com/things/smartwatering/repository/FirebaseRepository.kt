package com.things.smartwatering.repository

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.ValueEventListener
import com.things.smartwatering.model.DataInfo


interface FirebaseRepository {
    fun putDataInfo(info: DataInfo)

    fun getStatusData(listener: ValueEventListener)

    fun getEventData(listener: ChildEventListener)
}