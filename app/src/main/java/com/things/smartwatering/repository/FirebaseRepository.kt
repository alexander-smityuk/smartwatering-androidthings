package com.things.smartwatering.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseRef: DatabaseReference = mDatabase.getReference("data").push()

    fun setTime(time: Long) {
        mDatabaseRef.child("time").setValue(time)
    }

    fun setTemperature(temperature: Float) {
        mDatabaseRef.child("temperature").setValue(temperature)
    }
}