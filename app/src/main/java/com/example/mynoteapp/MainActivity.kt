package com.example.mynoteapp

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynoteapp.Db.TaskDatabase
import com.example.mynoteapp.Utils.setUpDialog
import com.example.mynoteapp.Utils.status
import com.example.mynoteapp.Utils.validateinputs
import com.example.mynoteapp.adapters.TaskAdapter
import com.example.mynoteapp.databinding.ActivityMainBinding
import com.example.mynoteapp.models.TaskItem
import com.example.mynoteapp.repository.Taskrepositoriy
import com.example.mynoteapp.viewmodels.TaskViewModel
import com.example.mynoteapp.viewmodels.ViewmodelFactory
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import java.util.UUID


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel:TaskViewModel
    private lateinit var  adapter:TaskAdapter




    private val newtaskdialog:Dialog by lazy{
    Dialog(this).apply {
        setUpDialog(R.layout.task_insert_view) }}

    private val loadingdialog:Dialog by lazy {
        Dialog(this).apply { setUpDialog(R.layout.loading_dialog_layout) } }

    private val updatetaskdialog:Dialog by lazy {
    Dialog(this).apply {
        setUpDialog(R.layout.task_update_view)} }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)







        var new_task_name = newtaskdialog.findViewById<TextInputEditText>(R.id.new_task_name)
        var new_task_description = newtaskdialog.findViewById<TextInputEditText>(R.id.new_description)
        var updated_task_name = updatetaskdialog.findViewById<TextInputEditText>(R.id.updated_task_name)
        var updated_task_description = updatetaskdialog.findViewById<TextInputEditText>(R.id.updated_task_desc)
        var update_button = updatetaskdialog.findViewById<Button>(R.id.btn_update)
        updated_task_name.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateinputs(updated_task_name)
            }

        })
        updated_task_description.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateinputs(updated_task_description)
            }

        })

        var database = TaskDatabase(this)
        val repository = Taskrepositoriy(database)
        var factory = ViewmodelFactory(repository)
        viewmodel = ViewModelProvider(this,factory).get(TaskViewModel::class.java)
        //adapter = TaskAdapter(this,listOf(),viewmodel)
        adapter= TaskAdapter(this,viewmodel,{
            type,postion, task ->
            if (type =="delete"){
                viewmodel.delete(task)
                restoreDeletedTask(task)
                adapter.notifyDataSetChanged()
            }else if (type == "update"){
                updated_task_name.setText(task.name)
                updated_task_description.setText(task.description)
                update_button.setOnClickListener {
                    if (validateinputs(updated_task_name) && validateinputs(updated_task_description)){
                        var updated_task = TaskItem(updated_task_name.text.toString(),updated_task_description.text.toString(),Date())
                        updatetaskdialog.dismiss()
                        viewmodel.delete(task)
                        viewmodel.upsert(updated_task)
                        adapter.notifyDataSetChanged()
                    }
                }
                updatetaskdialog.show()
            }


        })

        binding.rvNote.adapter = adapter
         binding.rvNote.layoutManager = LinearLayoutManager(this)
      //  binding.rvNote.layoutManager = GridLayoutManager(this,2)

        viewmodel.getAllItems().observe(this, Observer {
            adapter.differ.submitList(it)
            adapter.notifyDataSetChanged()
        })





        var task_insert_close_image = newtaskdialog.findViewById<ImageView>(R.id.iv_new_close)
        var task_update_close_image = updatetaskdialog.findViewById<ImageView>(R.id.iv_update_close)
        task_insert_close_image.setOnClickListener { newtaskdialog.dismiss() }
        task_update_close_image.setOnClickListener { updatetaskdialog.dismiss() }

        var save_button = newtaskdialog.findViewById<Button>(R.id.btn_save)



           //validating new task name and description

        new_task_name.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
           validateinputs(new_task_name)
            }

        })
        new_task_description.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateinputs(new_task_description)
            }

        })
        //validating updated task name and description




        binding.floatingAdd.setOnClickListener {
            newtaskdialog.show()
        }



        save_button.setOnClickListener {
            if (validateinputs(new_task_name) && validateinputs(new_task_description)){
                var task = TaskItem(new_task_name.text.toString(),new_task_description.text.toString(),Date())
                newtaskdialog.dismiss()
                new_task_name.text = null
                new_task_description.text = null
                viewmodel.upsert(task)

            }

        }
        getsearchitems()


     }

       fun getsearchitems(){
        binding.taskSerach.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(searchquery: Editable?) {
                if (searchquery.toString().trim().isNotEmpty()){
                    viewmodel.getsearchTaskItems(searchquery.toString()).observe(this@MainActivity,
                        Observer {
                            adapter.differ.submitList(it)
                            adapter.notifyDataSetChanged()
                        })
                }else{
                    viewmodel.getAllItems().observe(this@MainActivity, Observer {
                        adapter.differ.submitList(it)
                        adapter.notifyDataSetChanged()
                    })
                }
            }
        })
        binding.taskSerach.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){

                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun restoreDeletedTask(deletedtask:TaskItem){
        var snackbar = Snackbar.make(binding.root,"Deleted ${deletedtask.name}",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            viewmodel.upsert(deletedtask)
        }
        snackbar.show()
    }

}