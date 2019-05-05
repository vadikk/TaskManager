package com.example.managingtask.recycler

import android.icu.text.SimpleDateFormat
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managingtask.R
import com.example.managingtask.data.model.TaskModel
import kotlinx.android.synthetic.main.task_layout.view.*
import java.util.*

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
            return TaskViewHolder(view)
        }
    }

    fun bind(model: TaskModel, detailblock: () -> Unit) {
        itemView.nameTask.text = model.title
        itemView.priorityText.text = model.priority

        val simpleFormat = SimpleDateFormat("MMMM d, yyyy")
        val dateFormat = simpleFormat.format(Date(model.dueBy.toLong().times(1000L)))

        val dateSpannable = SpannableStringBuilder(itemView.resources.getString(R.string.due_to)).apply {
            val textLength = length
            insert(textLength, " " + dateFormat)
        }

        itemView.dueToTextView.text = dateSpannable

        itemView.setOnClickListener { detailblock() }
    }

    fun release() = itemView.goToDetailInfo.setOnClickListener(null)
}