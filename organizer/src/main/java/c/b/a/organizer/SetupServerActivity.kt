package c.b.a.organizer

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_setup_server.*

class SetupServerActivity : AppCompatActivity(), View.OnClickListener {
    private var server : String? = null
    override fun onClick(v: View?) {
        server = et_server.text.toString()
        if (!server!!.isEmpty()) {

            tryConnect(this, server!!, object : ConnectedListener {
                override fun onExecuted(context: Context, error: String?) {
                    if (error == null || error.isEmpty()) {
                        savePreferences()
                        val intent = Intent(this@SetupServerActivity, StartActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    } else {
                        tv_server_error.text = error
                        tv_server_error.visibility = View.VISIBLE
                    }
                }
            })
        } else {
            tv_server_error.text = "Введіть ім'я сервера"
            tv_server_error.visibility = View.VISIBLE
        }
        if (v != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun savePreferences() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = settings.edit()
        editor.putString(SERVER_NAME, server)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_server)

        btn_ok.setOnClickListener(this)
    }
}
