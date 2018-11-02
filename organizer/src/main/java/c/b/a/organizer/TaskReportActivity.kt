package c.b.a.organizer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_task_report.*
import ua.com.info.data.*

class TaskReportActivity : AppCompatActivity() {
    private var title: String? = null
    private lateinit var prms: Parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_report)

        title = intent.getStringExtra("title")
        tv_tittle_report.text = title

        val l = View.OnClickListener {
            val report = et_report.text.toString()
            if (!report.isEmpty()) {
                val taskCode = intent.extras!!.getInt("taskCode")
                prms = Parameters()
                prms.add(Parameter("КодПрацівника", employerCode, Direction.input, Types.Int))
                prms.add(Parameter("Задача", taskCode, Direction.input, Types.Int))
                prms.add(Parameter("Звіт", report, Direction.input, Types.VarChar))
                val intent = Intent()
                when (title) {
                    "Відкладена робота" -> {
                        DataBase.db.Execute("Органайзер.Відкладено_Записати", prms) { result -> Log.d("DB", result.status.toString()) }
                        intent.putExtra("refresh", 1)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    "Виконана робота" -> {
                        DataBase.db.Execute("Органайзер.Виконано_Записати", prms) { result -> Log.d("DB", result.status.toString()) }
                        intent.putExtra("refresh", 2)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
        btn_report_send.setOnClickListener(l)
    }
}
