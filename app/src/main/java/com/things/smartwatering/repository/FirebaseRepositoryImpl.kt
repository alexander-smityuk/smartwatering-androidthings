package com.things.smartwatering.repository

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.things.smartwatering.model.DataInfo

class FirebaseRepositoryImpl : FirebaseRepository {

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseRef: DatabaseReference = mDatabase.reference

    override fun putDataInfo(info: DataInfo) {
        mDatabaseRef.child("data").setValue(info)
    }

    override fun getStatusData(listener: ValueEventListener) {
        mDatabaseRef.child("status").addValueEventListener(listener)
    }

    override fun getEventData(listener: ChildEventListener) {
        mDatabaseRef.child("events").addChildEventListener(listener)
    }
}