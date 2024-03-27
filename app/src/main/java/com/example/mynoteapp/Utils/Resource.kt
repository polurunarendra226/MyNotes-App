package com.example.mynoteapp.Utils

sealed class Resource<T>(var status: status,var data:T?=null,var message:String?=null) {

    class Success<T>(data:T?):Resource<T>(status.SUCCESS,data)
    class Error<T>(message: String,data:T?=null):Resource<T>(status.ERROR,data,message)
    class Loading<T>():Resource<T>(status.LOADING)
}