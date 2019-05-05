package com.example.managingtask.activities.taskDetailsActivity

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.managingtask.R
import com.example.managingtask.dagger.App
import com.example.managingtask.data.model.TaskModel
import kotlinx.android.synthetic.main.detail_task_layout.*
import java.util.*
import javax.inject.Inject

enum class TaskOperation{
    Detail, Edit
}

class TaskDetailActivity : AppCompatActivity(),
                            TaskDetailActivityContract.View,
                            View.OnClickListener{

    companion object {
        const val TASK_ID = "task_id"
    }

    @Inject
    lateinit var presenter: TaskDetailActivityContract.Presenter
    private var taskOperation = TaskOperation.Detail
    private var priority: String? = ""
    private var dateValue: String? = null
    private var taskModel: TaskModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_task_layout)

        App.appComponent.inject(this)
        presenter.init(this)

        val bundle = intent.extras
        if (bundle != null){
            presenter.passTaskID(bundle.getInt(TASK_ID))
        }

        arrowBack.setOnClickListener(this)
        deleteBtn.setOnClickListener(this)
        editBtn.setOnClickListener(this)
        highPriorityBtn.setOnClickListener(this)
        mediumPriorityBtn.setOnClickListener(this)
        lowPriorityBtn.setOnClickListener(this)
        calendarBtn.setOnClickListener(this)
        updateTaskBtn.setOnClickListener(this)

        presenter.getTaskDetail()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.arrowBack -> {
                if (taskOperation == TaskOperation.Detail) onBackPressed()
                else {
                    taskOperation = TaskOperation.Detail
                    closeEditView()
                }
            }
            R.id.deleteBtn -> presenter.deleteTask()
            R.id.editBtn -> {
                taskOperation = TaskOperation.Edit
                closeDetailLayout()
                presenter.editTask()
            }
            R.id.highPriorityBtn ->{
                priority = resources.getString(R.string.high)

                highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
            }
            R.id.mediumPriorityBtn ->{
                priority = resources.getString(R.string.medium)

                highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
            }
            R.id.lowPriorityBtn ->{
                priority = resources.getString(R.string.low)

                highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
            }
            R.id.calendarBtn ->{
                showDatePickerDialog(taskModel!!)
            }
            R.id.updateTaskBtn -> presenter.updateTask(titleEditText.text.trim().toString(), dateValue!! ,priority!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun updateUI(taskOperation: TaskOperation,model: TaskModel) {

        taskModel = model
        priority = model.priority
        dateValue = model.dueBy

        if (taskOperation == TaskOperation.Detail){
            closeEditView()
            titleTask.text = model.title

            val simpleFormat = SimpleDateFormat("MMMM d, yyyy")
            val dateFormat = simpleFormat.format(Date(model.dueBy.toLong().times( 1000L)))
            dateText.text = dateFormat
            priorityValue.text = model.priority
        }else{
            titleEditText.setText(model.title)
            when(model.priority){
                resources.getString(R.string.high) -> highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                resources.getString(R.string.medium) -> mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                resources.getString(R.string.low) -> lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
            }

        }

    }

    override fun closeView() {
        finish()
    }

    private fun showDatePickerDialog(model: TaskModel){
        val calendar = Calendar.getInstance().apply {
            timeInMillis = model.dueBy.toLong().times(1000L)
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            getPickDate(calendar, year, month, dayOfMonth)
        }, year, month, dayOfMonth).show()
    }

    private fun getPickDate(calendar: Calendar, year: Int, month: Int, dayOfMonth: Int){
        val dateCalendar =  calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        //convert time from milliseconds to seconds(Unix-time)
        dateValue = dateCalendar.timeInMillis.div(1000L).toString()
    }

    override fun closeEditLayout() {
        taskOperation = TaskOperation.Detail
        closeEditView()
        presenter.getTaskDetail()
    }

    private fun closeEditView(){
        editBtn.visibility = View.VISIBLE
        toolbarTitle.text = resources.getString(R.string.task_details)
        taskDetailLayout.visibility = View.VISIBLE
        editTaskLayout.visibility = View.INVISIBLE
        deleteBtn.visibility = View.VISIBLE
        updateTaskBtn.visibility = View.INVISIBLE
    }

    private fun closeDetailLayout(){
        editBtn.visibility = View.INVISIBLE
        toolbarTitle.text = resources.getString(R.string.edit_task)
        taskDetailLayout.visibility = View.INVISIBLE
        editTaskLayout.visibility = View.VISIBLE
        deleteBtn.visibility = View.INVISIBLE
        updateTaskBtn.visibility = View.VISIBLE
    }
}
