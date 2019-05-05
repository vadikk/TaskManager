package com.example.managingtask.activities.createTaskActivity

import android.util.Log
import com.example.managingtask.data.ITaskRepository
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class CreateTaskPresenter @Inject constructor(
    private val taskRepository: ITaskRepository
) : CreateTaskActivityContract.Presenter {

    private var view: CreateTaskActivityContract.View? = null
    private var createTaskDisposable: Disposable? = null
    private var year = -1
    private var month = -1
    private var dayOfMonth = -1
    private var hourOfDay = -1
    private var minute = -1
    private var textTask = ""
    private var priority = ""

    override fun init(view: CreateTaskActivityContract.View) {
        this.view = view
    }

    override fun destroy() {
        createTaskDisposable?.dispose()
        view = null
    }

    override fun passDateValue(year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
    }

    override fun passTimeValue(hourOfDay: Int, minute: Int) {
        this.hourOfDay = hourOfDay
        this.minute = minute
    }

    override fun passTaskText(string: String) {
        this.textTask = string
    }

    override fun passPriority(string: String) {
        this.priority = string
    }

    override fun createTask(title: String, priority: String) {
        if (createTaskDisposable != null && createTaskDisposable?.isDisposed != null) return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)

        }

        //convert time from milliseconds to seconds(Unix-time)
        val time = calendar.timeInMillis.div(1000L).toString()

        if (year != -1 && month != -1 && dayOfMonth != -1 && hourOfDay != -1 && minute != -1 && textTask != "" && priority != "") {
            createTaskDisposable = taskRepository.createTask(title, time, priority)
                .subscribe({
                    view?.goToNextView(time, title)
                }, {
                    Log.d("TAG", "CreateTaskError - ${it.localizedMessage}")
                })
        }


    }


}