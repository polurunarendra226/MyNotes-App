package com.example.mynoteapp.Db


import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.mynoteapp.models.TaskItem

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item:TaskItem):Long

    @Delete
    suspend fun Delete(item: TaskItem)

    @Query("SELECT * FROM task_items ORDER BY date DESC")
    fun getAllItems(): LiveData<List<TaskItem>>

    @Query("SELECT * FROM task_items  WHERE Task_name LIKE:query ORDER BY date DESC")
    fun getsearchTaskItems(query:String):LiveData<List<TaskItem>>
}