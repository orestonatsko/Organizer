package ua.com.info.common

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_error_message.*

class InfoMessage : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_message)
        setFinishOnTouchOutside(false)

        btn_ok.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){ //todo : insert actions
            R.id.btn_ok -> {
                finish()
            }
        }
    }
    override fun onBackPressed() {
        Log.d("MY_LOG", "onBackPressed button was clicked")
        return
    }
}