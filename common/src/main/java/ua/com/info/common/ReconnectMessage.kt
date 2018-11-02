package ua.com.info.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_reconnect_message.*

class ReconnectMessage : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reconnect_message)
        setFinishOnTouchOutside(false)
        btn_repeat.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_repeat -> {
                setResult(1)
                finish()
            }
            R.id.btn_cancel -> {
                setResult(0)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        setResult(0)
        Log.d("MY_LOG", "onBackPressed button was clicked")
        return
    }

}
