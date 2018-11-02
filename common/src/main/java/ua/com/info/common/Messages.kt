package ua.com.info.common

import android.content.Context
import android.content.Intent

/**
 *  Створено oor 14.06.2018.
 */
//

fun msgQuestion(context: Context, question: String) {
    val intent = Intent(context, QuestionMessage::class.java)
    intent.putExtra("question", question)
    context.startActivity(intent)
}

fun msgError(context: Context, errorMsg : String) {
    val intent = Intent(context, ErrorMessage::class.java)
    intent.putExtra("errorMsg", errorMsg)
    context.startActivity(intent)
}

fun msgReconnect(activity: BaseActivity){
    val intent = Intent(activity, ReconnectMessage::class.java)
    activity.startActivityForResult(intent, 1)
}

fun msgInfo(context: Context) {
    val intent = Intent(context, InfoMessage::class.java)
    context.startActivity(intent)
}
