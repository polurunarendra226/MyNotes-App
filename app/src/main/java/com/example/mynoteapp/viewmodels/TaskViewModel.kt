package com.example.mynoteapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mynoteapp.models.TaskItem
import com.example.mynoteapp.repository.Taskrepositoriy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(var repository:Taskrepositoriy) :ViewModel(){

    fun upsert(item: TaskItem)=repository.upsert(item)
    fun delete(item: TaskItem)= CoroutineScope(Dispatchers.IO).launch {
        repository.delete(item)
    }
    fun getAllItems()= repository.getAllItems()

    fun getsearchTaskItems(query:String) = repository.getsearchTaskItems2(query)
    //fun getsearchTaskItems2(query:String) = repository.getsearchTaskItems2(query)

}