package c.b.a.organizer

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_task.*
import ua.com.info.data.*
import java.util.*

class TaskActivity : AppCompatActivity() {
    private var taskName: String? = null
    private var taskDescription: String? = null
    private var taskCode: Int = 0
    private var taskStage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val extras = intent.extras
        if (extras != null) {
            taskCode = extras.getInt("task")

            val params = Parameters("Код", taskCode)

            val pName = Parameter("Заголовок", Direction.output, Types.VarChar)
            params.add(pName)

            val pDescription = Parameter("Опис", Direction.output, Types.VarChar)
            params.add(pDescription)

            val pStage = Parameter("Стадія", Direction.output, Types.VarChar)
            params.add(pStage)

            DataBase.db.Execute("Органайзер.Завдання_Зчитати", params, IDataBase.Listener { result ->
                if (result.isOk) {
                    val vars = result.variables
                    taskName = vars!!.getString("Заголовок")
                    taskDescription = vars.getString("Опис")
                    taskStage = vars.getInt("Стадія")!!
                    if (taskStage == 1) {
                        btn_task_start.visibility = View.GONE
                        btn_task_postpone.visibility = View.VISIBLE
                        btn_task_finish.visibility = View.VISIBLE
                    } else if (taskStage == 3) {
                        btn_task_start.visibility = View.GONE
                        btn_task_postpone.visibility = View.GONE
                        btn_task_finish.visibility = View.GONE
                    }
                    refreshText()

                } else if (result.status == Status.Error) {
                    Log.e(LOG_TAG, result.error())
                }
            })

            val l = View.OnClickListener { view ->
                val reportIntent = Intent(this@TaskActivity, TaskReportActivity::class.java)
                reportIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
                when (view.id) {
                    R.id.btn_task_postpone -> {
                        reportIntent.putExtra("taskCode", taskCode)
                        reportIntent.putExtra("title", "Відкладена робота")
                        startActivity(reportIntent)
                        finish()
                    }
                    R.id.btn_task_finish -> {
                        reportIntent.putExtra("taskCode", taskCode)
                        reportIntent.putExtra("title", "Виконана робота")
                        startActivity(reportIntent)
                        finish()
                    }
                    R.id.btn_task_start -> {
                        DataBase.db.Execute("Органайзер.Розпочато_Записати", Parameters("Задача", taskCode)) {
                            result -> Log.d(LOG_TAG, result.status.toString()) }
                        val refreshIntent = Intent()
                        refreshIntent.putExtra("refresh", 1)
                        setResult(Activity.RESULT_OK, refreshIntent)
                        finish()
                    }
                }//                            scheduleAlarm();
            }
            btn_task_finish.setOnClickListener(l)
            btn_task_postpone.setOnClickListener(l)
            btn_task_start.setOnClickListener(l)

        }

    }

    private fun refreshText() {
        tv_name.text = taskName
        tv_description.text = taskDescription
    }

    fun scheduleAlarm() {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 52)

        val intent = Intent(this, ReportReceiver::class.java)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                PendingIntent.getBroadcast(this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT))

        Toast.makeText(this, "Alarm Scheduled on 17:52", Toast.LENGTH_LONG).show()
    }
}
