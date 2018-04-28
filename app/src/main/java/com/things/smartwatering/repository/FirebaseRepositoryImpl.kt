package com.things.smartwatering.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.things.smartwatering.model.DataInfo

class FirebaseRepositoryImpl : FirebaseRepository {

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseRef: DatabaseReference = mDatabase.reference

    override fun putDataInfo(info: DataInfo) {
        mDatabaseRef.child("data").setValue(info)
    }
}