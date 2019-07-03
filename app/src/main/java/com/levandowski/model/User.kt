package com.levandowski.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class User: Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "token")
    var token: String = ""

    @ColumnInfo(name = "email")
    var email: String = ""

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "phone")
    var phone: String = ""

    @ColumnInfo(name = "photo")
    var photoURL: String = ""

    @ColumnInfo(name = "time_stamp")
    var timeStamp: Long = 0
}