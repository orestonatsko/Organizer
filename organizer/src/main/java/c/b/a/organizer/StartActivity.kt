package c.b.a.organizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_start.*
import java.text.DateFormat
import java.util.*

var server: String? = null
const val SERVER_NAME = "server"
const val PREFS_NAME = "serverPreferences"
private val PICK_SERVER_REQUEST = 1

class StartActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        btn_repeat.visibility = View.GONE
        pb_server_connecting.visibility = View.VISIBLE;
        tv_message.setTextColor(ContextCompat.getColor(this, R.color.ТекстПриглушений));
        tv_message.text = "Під'єднання до сервера";
        setupServer();
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        setCurrentDate()
        btn_repeat.setOnClickListener(this)

        if (loadPreferences()) {
            tryConnect(this, server!!, object : ConnectedListener {
                override fun onExecuted(context: Context, error: String?) {
                    if (error == null || error.isEmpty()) {
                        if (employerCode != 0) {
                            startMain()
                        } else {
                            showRepeatBtn()
                            tv_message.setTextColor(ContextCompat.getColor(context, R.color.ФонПомилки))
                            tv_message.text = "Вхід Заборонено!"
                        }
                    } else {
                        showRepeatBtn()
                        tv_message.setTextColor(ContextCompat.getColor(context, R.color.ФонПомилки))
                        tv_message.text = error
                    }
                }
            })
        } else {
            setupServer()
        }
    }

    private fun showRepeatBtn() {
        pb_server_connecting.visibility = View.GONE
        btn_repeat.visibility = View.VISIBLE
    }

    private fun setCurrentDate() {
        val date = Calendar.getInstance().time
        val formatter = DateFormat.getDateInstance()
        tv_date.text = formatter.format(date)
    }

    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    fun clearPreferences() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }

    private fun loadPreferences(): Boolean {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        server = settings.getString(SERVER_NAME, null)
        return server != null
    }

    private fun setupServer() {
        val intent = Intent(this, SetupServerActivity::class.java)
//        val intent = intentTo(SetupServerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) //todo: доробити
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        //        startActivityForResult(intent, PICK_SERVER_REQUEST);
        startActivity(intent)
        finish()
    }

    private fun intentTo(cls: Class<out AppCompatActivity>): Intent {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        return intent
    }
}
