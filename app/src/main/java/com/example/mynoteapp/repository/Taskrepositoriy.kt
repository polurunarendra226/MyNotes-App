package com.example.mynoteapp.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynoteapp.Db.TaskDatabase
import com.example.mynoteapp.Utils.Resource
import com.example.mynoteapp.models.TaskItem
import javax.inject.Inject
import com.example.mynoteapp.Utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Taskrepositoriy(var database: TaskDatabase) {


    fun upsert(item: TaskItem)=MutableLiveData<Resource<Long>>().apply {
        postValue(Loading())
        try {
CoroutineScope(Dispatchers.IO).launch {
   var result = database.getobjectsofDao().upsert(item)
    postValue(Success(result))
}
        }catch (e:Exception){
postValue(Error(e.message.toString()))
        }
    }

    suspend fun delete(item: TaskItem)= database.getobjectsofDao().Delete(item)

    fun getAllItems()=database.getobjectsofDao().getAllItems()




//    fun getsearchTaskItems(query:String)=MutableLiveData<Resource<LiveData<List<TaskItem>>>>().apply {
//        postValue(Loading())
//        try {
//            CoroutineScope(Dispatchers.IO).launch {
//                var result = database.getobjectsofDao().getsearchTaskItems("%${query}%")
//                postValue(Success(result))
//            }
//        }catch (e:Exception){
//            postValue(Error(e.message.toString()))
//        }
//    }
    fun getsearchTaskItems2(query:String)= database.getobjectsofDao().getsearchTaskItems(query)
}