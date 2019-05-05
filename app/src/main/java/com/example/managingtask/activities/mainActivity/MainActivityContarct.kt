package com.example.managingtask.activities.mainActivity

import com.example.managingtask.data.model.TaskModel

interface MainActivityContarct {

    interface View {
        fun updateUI(list: List<TaskModel>, totalCount: Int)
        fun showSortedList(list: List<TaskModel>)
    }

    interface Presenter {
        fun init(view: View)
        fun destroy()
        fun getTaskList(page: Int, sort: String)
        fun sortBy(sorted: MainActivityPresenter.SortedBy, sortTitle: String)
        fun clearList()
    }
}