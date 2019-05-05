package com.example.managingtask.activities.createTaskActivity

import android.app.*
import android.app.AlarmManager.RTC_WAKEUP
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.managingtask.R
import com.example.managingtask.dagger.App
import com.example.managingtask.util.AlarmManagerReceiver
import com.example.managingtask.util.AlarmManagerReceiver.Companion.TASK_TITLE
import kotlinx.android.synthetic.main.create_task_layout.*
import java.util.*
import javax.inject.Inject

class CreateTaskActivity : AppCompatActivity(),
                            CreateTaskActivityContract.View,
                            View.OnClickListener{

    companion object {
        const val ALARM_CODE = 1
    }


    private var priority: String? = ""

    @Inject
    lateinit var presenter: CreateTaskActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task_layout)

        App.appComponent.inject(this)
        presenter.init(this)

        arrowBack.setOnClickListener { onBackPressed() }
        highPriorityBtn.setOnClickListener(this)
        mediumPriorityBtn.setOnClickListener(this)
        lowPriorityBtn.setOnClickListener(this)
        dateBtn.setOnClickListener(this)
        createEventBtn.setOnClickListener (this)
        timeBtn.setOnClickListener(this)

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.passTaskText(s.toString())
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun goToNextView(dueBy:String, title: String) {
        val intent = Intent(this, AlarmManagerReceiver::class.java).apply {
            putExtra(TASK_TITLE, title)
        }
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, ALARM_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.apply {
            setExact(RTC_WAKEUP, dueBy.toLong()*1000, pendingIntent)
        }

        finish()
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.highPriorityBtn ->{
                priority = resources.getString(R.string.high)
                presenter.passPriority(resources.getString(R.string.high))

                highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
            }
            R.id.mediumPriorityBtn ->{
                priority = resources.getString(R.string.medium)
                presenter.passPriority(resources.getString(R.string.medium))

                highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
            }
            R.id.lowPriorityBtn ->{
                priority = resources.getString(R.string.low)
                presenter.passPriority(resources.getString(R.string.low))

                highPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                mediumPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.btnColor))
                lowPriorityBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
            }
            R.id.dateBtn ->{
                val calendar = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    presenter.passDateValue(year, month, dayOfMonth)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.createEventBtn ->{
                if (titleEditText.text.trim().toString().isNotEmpty() && priority?.isNotEmpty()!!){
                    presenter.createTask(titleEditText.text.trim().toString(), priority!!)
                }
            }
            R.id.timeBtn -> {
                val calendar = Calendar.getInstance()
                val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                   presenter.passTimeValue(hourOfDay, minute)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }
        }
    }
}
