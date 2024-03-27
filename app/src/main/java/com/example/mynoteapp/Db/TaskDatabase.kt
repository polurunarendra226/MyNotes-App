package com.example.mynoteapp.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynoteapp.models.TaskItem


@Database(entities = [TaskItem::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class TaskDatabase():RoomDatabase() {
    abstract fun getobjectsofDao():TaskDao
    companion object{
        @Volatile
        private var instnace:TaskDatabase?=null
        private var Lock= Any()
        operator fun invoke(context: Context)= instnace?: synchronized(Lock){
            instnace?: createDatabase(context).also { instnace=it }
        }
        private fun createDatabase(context: Context)=
            Room.databaseBuilder(context.applicationContext,TaskDatabase::class.java,"task..db").build()


    }

}