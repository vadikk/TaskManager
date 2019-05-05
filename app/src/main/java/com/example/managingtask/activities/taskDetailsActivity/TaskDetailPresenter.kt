package com.example.managingtask.activities.taskDetailsActivity

import android.util.Log
import com.example.managingtask.data.ITaskRepository
import com.example.managingtask.data.model.TaskModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class TaskDetailPresenter @Inject constructor(
   private val taskRepository: ITaskRepository
): TaskDetailActivityContract.Presenter {

    private var view: TaskDetailActivityContract.View? = null
    private var taskID = -1
    private var taskDetailDisposable: Disposable? = null
    private var deleteTaskDisposable: Disposable? = null
    private var updateTaskDisposable: Disposable? = null
    private var taskModel: TaskModel? = null

    override fun init(view: TaskDetailActivityContract.View) {
        this.view = view
    }

    override fun destroy() {
        taskDetailDisposable?.dispose()
        deleteTaskDisposable?.dispose()
        updateTaskDisposable?.dispose()
        view = null
    }

    override fun passTaskID(id: Int) {
        taskID = id
    }

    override fun getTaskDetail() {
//        if(taskDetailDisposable != null && taskDetailDisposable?.isDisposed != null) return

        taskDetailDisposable = taskRepository.getTaskDetail(taskID)
            .subscribe({
                taskModel = it.task
                view?.updateUI(TaskOperation.Detail ,taskModel!!)
            },{
                Log.d("TAG", "TaskDetail error - ${it.localizedMessage}")
            })
    }

    override fun deleteTask() {
        if (deleteTaskDisposable != null && deleteTaskDisposable?.isDisposed != null) return

        deleteTaskDisposable = taskRepository.deleteTask(taskID).subscribe({
            view?.closeView()
        },{
            Log.d("TAG", "DeleteTask error - ${it.localizedMessage}")
        })

    }

    override fun editTask() {
        view?.updateUI(TaskOperation.Edit, taskModel!!)
    }

    override fun updateTask(title: String, dueBy: String, priority: String) {
//        if (updateTaskDisposable != null && updateTaskDisposable?.isDisposed != null) return

        updateTaskDisposable = taskRepository.updateTask(taskID, title, dueBy, priority)
            .subscribe({
//                view?.updateUI(TaskOperation.Detail, taskModel!!)
                view?.closeEditLayout()
            },{
                Log.d("TAG", "UpdateTask error - ${it.localizedMessage}")
            })
    }
}