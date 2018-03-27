package com.things.smartwatering.repository

import com.things.smartwatering.model.DataInfo


interface FirebaseRepository {
    fun putDataInfo(info: DataInfo)
}