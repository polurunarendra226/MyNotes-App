package com.example.mynoteapp.Utils

import android.app.Dialog
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText


enum class status{
    SUCCESS,ERROR,LOADING
}

fun Dialog.setUpDialog(layoutId:Int){
    setContentView(layoutId)
    window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
    setCancelable(false)
}
fun validateinputs(editText: TextInputEditText):Boolean{
    return  when {
        editText.text.toString().trim().isNullOrBlank() -> {
            editText.error = "Required"
            false
        }else ->{
            editText.error = null
            true
        }
    }
}
fun clearinputs(editText: TextInputEditText){
    editText.text = null
    editText.error = null
}