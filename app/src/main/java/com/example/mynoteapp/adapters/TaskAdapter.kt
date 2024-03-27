package com.example.mynoteapp.adapters


import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.example.mynoteapp.R
import com.example.mynoteapp.Utils.setUpDialog
import com.example.mynoteapp.models.TaskItem
import com.example.mynoteapp.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputEditText

import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    var context: Context,var viewmodel:TaskViewModel,
    private val deletecallback:(type:String,postion:Int,task:TaskItem) -> Unit):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {


    inner class TaskViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        var title = itemview.findViewById<TextView>(R.id.title)
        var desc = itemview.findViewById<TextView>(R.id.desc)
        var date = itemview.findViewById<TextView>(R.id.date)
        var delete = itemview.findViewById<ImageView>(R.id.iv_delete)
        var edit = itemview.findViewById<ImageView>(R.id.iv_edit)

    }
    private val differcallback = object :DiffUtil.ItemCallback<TaskItem>(){
        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem.description == newItem.description
        }

        override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem == newItem
        }
    }
    var differ = AsyncListDiffer(this@TaskAdapter,differcallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
       //return list.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      //var currenttask = list[position]
        var currenttask = differ.currentList[position]
        var simpledateformat = SimpleDateFormat("dd:MMM:YYYY HH:mm a", Locale.getDefault())
        holder.apply {
            title.text = currenttask.name
            desc.text = currenttask.description
            date.text = simpledateformat.format(currenttask.date)
            delete.setOnClickListener {
                deletecallback("delete",holder.adapterPosition,currenttask)
            }
            edit.setOnClickListener {
               deletecallback("update",holder.adapterPosition,currenttask)

            }

        }
    }
}