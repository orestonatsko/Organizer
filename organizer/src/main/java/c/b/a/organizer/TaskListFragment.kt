package c.b.a.organizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import ua.com.info.data.*

const val BUNDLE_CMD = "ServerProcedure"
const val BUNDLE_TITLE = "fragmentTitle"

class TaskListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    companion object {
        fun newInstance(command: String, title: String): TaskListFragment {
            val fragment = TaskListFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_TITLE, title)
            bundle.putString(BUNDLE_CMD, command)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val taskRefreshRequest = 2

    private  var taskAdapter: TaskAdapter? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    var loadListener: LoadListener? = null
    var title: String? = null
    private var table: Table? = null
    private var cmd: String? = null
    private lateinit var progress : ProgressBar

    interface LoadListener {
        fun started()
        fun completed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            cmd = args.getString(BUNDLE_CMD)
            title = args.getString(BUNDLE_TITLE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container,
                false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        progress = view.findViewById(R.id.pb_load_tasks)
        loadData()

        val listener = AdapterView.OnItemClickListener { _, _, i, _ ->
            val row = table!![i]
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra("task", row.getValue("Код") as Int)
            startActivityForResult(intent, taskRefreshRequest)
        }
        view.list.onItemClickListener = listener

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.swipe_refresh_color_1),
                ContextCompat.getColor(context!!, R.color.swipe_refresh_color_2),
                ContextCompat.getColor(context!!, R.color.swipe_refresh_color_3),
                ContextCompat.getColor(context!!, R.color.swipe_refresh_color_4))
        swipeRefreshLayout.setOnRefreshListener(this)

        return view
    }

    fun hasTasks(): Boolean {
        return table != null && table!!.size > 0
    }


    fun loadData() {
        progress.visibility = View.VISIBLE

        loadListener!!.started()

        DataBase.db.getTable(cmd, Parameters("КодПрацівника", employerCode, Direction.input, Types.Int)) { result ->
            if (result.isOk) {
                table = result.table
                if (table != null) {
                    taskAdapter = TaskAdapter(context!!, table!!)
                    list.adapter = taskAdapter
                }
            }
            progress.visibility = View.INVISIBLE
            loadListener!!.completed()

        }
    }


    override fun onRefresh() {
        android.os.Handler().postDelayed({ swipeRefreshLayout.isRefreshing = false }, 1000)
        loadData()
    }

    override fun onDetach() {
        super.onDetach()
        loadListener = null
    }

    inner class TaskAdapter(private val ctx: Context,var  table: Table) : ArrayAdapter<Row>(ctx, R.layout.task_item, table) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            val row = getItem(position)

            if (view == null) {
                val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.task_item, parent, false)
            }
            val task = view!!.findViewById<TextView>(R.id.tv_task)
            val desc = view.findViewById<TextView>(R.id.tv_description)
            if (row != null) {
                task.text = row.getValue("Заголовок").toString()
                desc.text = row.getValue("Опис").toString()
            } else run { Log.e(LOG_TAG, "Row is NULL") }

            return view
        }
    }
}
