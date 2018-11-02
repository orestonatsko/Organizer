package ua.com.info.common

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_question_message.*

class QuestionMessage : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_message)
        val questionText = intent.getStringExtra("question")
        question_message.text = questionText
        setFinishOnTouchOutside(false)
        btn_yes.setOnClickListener(this)
        btn_no.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
      when(v?.id){ //todo : insert actions
          R.id.btn_yes -> {

          }
          R.id.btn_no -> {

          }
      }
    }
    override fun onBackPressed() {
        Log.d("MY_LOG", "onBackPressed button was clicked")
        return
    }
}
