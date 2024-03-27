package com.example.mynoteapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "task_items")
data class TaskItem(
    @ColumnInfo(name = "Task_name")
    var name:String,
    @ColumnInfo(name = "Task_description")
    var description:String,
    var date: Date
){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}