package com.example.managingtask.activities.mainActivity

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.example.managingtask.data.ITaskRepository
import com.example.managingtask.data.model.TaskModel
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(
    private val taskRepository: ITaskRepository
) : MainActivityContarct.Presenter {

    enum class SortedBy {
        NAME, PRIORITY, DATE
    }

    private var view: MainActivityContarct.View? = null
    private var getTaskDisposable: Disposable? = null
    private var taskList = mutableListOf<TaskModel>()
    private var sortedEnum = SortedBy.NAME

    override fun init(view: MainActivityContarct.View) {
        this.view = view
    }

    override fun destroy() {
        getTaskDisposable?.dispose()
        view = null
    }

    override fun clearList() {
        taskList.clear()
    }

    override fun getTaskList(page: Int, sort: String) {
//        if (getTaskDisposable != null && getTaskDisposable?.isDisposed != true) return

        getTaskDisposable = taskRepository.getTaskList(page, sort)
            .subscribe({
                taskList.addAll(it.taskList)
                view?.updateUI(taskList, it.metaModel.count)
            }, {
                Log.d("TAG", "taskListError - ${it.localizedMessage}")
            })
    }

    override fun sortBy(sorted: SortedBy, sortTitle: String) {
        sortedEnum = sorted
        val sortedList = mutableListOf<TaskModel>()

        when (sortedEnum) {
            SortedBy.NAME -> {
                sortedList.addAll(taskList.filter {
                    it.title.contains(sortTitle, true)
                })
            }
            SortedBy.DATE -> {
                val simpleFormat = SimpleDateFormat("MMMM d, yyyy")
                val dateSortFormat = simpleFormat.format(Date(sortTitle.toLong().times(1000L)))
                sortedList.addAll(taskList.filter {
                    val dateFormat = simpleFormat.format(Date(it.dueBy.toLong().times(1000L)))
                    dateFormat.contains(dateSortFormat, true)
                })
            }
            SortedBy.PRIORITY -> {
                sortedList.addAll(taskList.filter {
                    it.priority.contains(sortTitle, true)
                })
            }
        }

        view?.showSortedList(sortedList)
    }
}