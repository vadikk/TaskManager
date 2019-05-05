package com.example.managingtask.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managingtask.data.model.TaskModel

class TaskRecyclerAdapter(
    private val detailClick: (taskID: Int) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    var taskList = mutableListOf<TaskModel>()

    fun addAllToList(list: List<TaskModel>) {
        taskList.clear()
        taskList.addAll(list)
        notifyItemRangeInserted(itemCount, list.size)
    }

    fun clear() {
        taskList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder = TaskViewHolder.create(parent)

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var model = taskList[holder.adapterPosition]

        holder.bind(model) {
            detailClick(model.id.toInt())
        }
    }

    override fun getItemCount(): Int = taskList.size

    override fun onViewRecycled(holder: TaskViewHolder) {
        super.onViewRecycled(holder)
        holder.release()
    }
}