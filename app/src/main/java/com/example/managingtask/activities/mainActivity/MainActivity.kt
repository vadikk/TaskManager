package com.example.managingtask.activities.mainActivity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.managingtask.R
import com.example.managingtask.activities.createTaskActivity.CreateTaskActivity
import com.example.managingtask.activities.taskDetailsActivity.TaskDetailActivity
import com.example.managingtask.dagger.App
import com.example.managingtask.data.model.TaskModel
import com.example.managingtask.recycler.TaskRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
                    MainActivityContarct.View{

    @Inject
    lateinit var presenter: MainActivityContarct.Presenter
    lateinit var taskAdapter: TaskRecyclerAdapter

    private var isSorted = false
    private var dateCalendar: Calendar? = null

    private var isLoading = false
    private var isFiltered = false
    private var page = -1
    private var totalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        App.appComponent.inject(this)
        presenter.init(this)

        taskAdapter = TaskRecyclerAdapter{
            goToDetailTaskView(it)
        }

        val manager = LinearLayoutManager(this).apply {
            orientation = RecyclerView.VERTICAL
        }

        taskRecycler.apply {
            adapter = taskAdapter
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(context, manager.orientation))
        }

        page = 1
        taskAdapter.clear()
        presenter.getTaskList(page, "")

        taskRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener (){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (isLoading) return

                val lastVisibleItemCount = manager.findLastVisibleItemPosition()
                val allLoadedItemsCount = taskAdapter.itemCount

                val loadShouldStartPosition = allLoadedItemsCount * 0.8
                if (loadShouldStartPosition <= lastVisibleItemCount && allLoadedItemsCount < totalCount) {
                    page++
                    isLoading = true
                }

                if (isLoading && !isFiltered)
                    presenter.getTaskList(page, "")
            }
        })

        fab.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }

        swipeRefresh.setOnRefreshListener {
            taskAdapter.clear()
            presenter.clearList()
            page = 1
            isFiltered = false
            presenter.getTaskList(1, "")
            swipeRefresh.isRefreshing = false
        }

        sortBtn.setOnClickListener {
            isSorted = !isSorted

            if (isSorted){
                taskAdapter.taskList.sortBy { it.title }
                taskAdapter.notifyDataSetChanged()


            } else{
                taskAdapter.taskList.sortByDescending { it.title }
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.sortByName ->{
                showSortDialog(MainActivityPresenter.SortedBy.NAME)
            }
            R.id.sortByPriority ->{
                showSortDialog(MainActivityPresenter.SortedBy.PRIORITY)
            }
            R.id.sortByDate ->{
                showDateDialog(MainActivityPresenter.SortedBy.DATE)
            }
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onRestart() {
        super.onRestart()
        taskAdapter.clear()
        page = 1
        isFiltered = false
        presenter.clearList()
        presenter.getTaskList(1, "")
    }

    override fun updateUI(list: List<TaskModel>, totalCount: Int) {
        isLoading = false
        this.totalCount = totalCount
        taskAdapter.addAllToList(list)
    }

    override fun showSortedList(list: List<TaskModel>) {
        isFiltered = true
        taskAdapter.clear()
        taskAdapter.addAllToList(list)
    }

    private fun goToDetailTaskView(id: Int){
        val intent = Intent(this, TaskDetailActivity::class.java).apply {
            putExtra(TaskDetailActivity.TASK_ID, id)
        }
        startActivity(intent)
    }

    private fun showSortDialog(sortedEnum: MainActivityPresenter.SortedBy){
        val ediTextView = LayoutInflater.from(this).inflate(R.layout.custom_edittext_layout, null, false)
        val editText = ediTextView.findViewById<EditText>(R.id.sortByEditText)

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.sort_by)
            .setView(ediTextView)
            .setCancelable(false)
            .setPositiveButton("OK") { dialogInterface, arg1 ->
                presenter.sortBy(sortedEnum, editText.text.trim().toString())
                dialogInterface.cancel()
            }
            .setNegativeButton("Cancel") { dialogInterface, arg1 ->
                dialogInterface.cancel()
            }
            .create()
            .show()
    }

    private fun showDateDialog(sortedEnum: MainActivityPresenter.SortedBy){
        val ediTextView = LayoutInflater.from(this).inflate(R.layout.custom_date_picker_layout, null, false)
        val dateBtn = ediTextView.findViewById<Button>(R.id.dateBtn)

        dateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                setTime(year, month, dayOfMonth)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.sort_by)
            .setView(ediTextView)
            .setCancelable(false)
            .setPositiveButton("OK") { dialogInterface, arg1 ->
                val time = dateCalendar?.timeInMillis?.div(1000L).toString()

                presenter.sortBy(sortedEnum, time)
                dialogInterface.cancel()
            }
            .setNegativeButton("Cancel") { dialogInterface, arg1 ->
                dialogInterface.cancel()
            }
            .create()
            .show()
    }

    private fun setTime(year: Int, month:Int, dayOfMonth: Int){
        dateCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }
}
