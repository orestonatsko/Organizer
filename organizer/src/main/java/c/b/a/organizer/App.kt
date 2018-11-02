package c.b.a.organizer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
import ua.com.info.data.*
import ua.com.info.sqldb.SQLDataBase

const val CMD_READ_USER = "Безпека.ЗчитатиКористувача"
var employerCode: Int = 0
var employerName: String? = null
var DEVICE_ID: String? = null

interface ConnectedListener {
    /**
     * Виконується коли запит виконано
     */
    fun onExecuted(context: Context, error: String?)
}

fun tryConnect(context: Context, server: String, listener: ConnectedListener?) {
    if (DataBase.db != null)
        DataBase.db.close()
    DataBase.db = SQLDataBase(server, "V2012", "Органайзер_temp", "Андроїд", "hdh~{sfcik!mYTw_Oxzovl%tmsFT7_&#$!~<<iejfgebp{Bg")
    DataBase.db.openConnection()
    if ((DataBase.db as SQLDataBase).isConnected) {
        val params = Parameters()
        val devCode = Parameter("КодПристрою", DEVICE_ID, Direction.input, Types.VarChar)
        params.add(devCode)
        val eCode = Parameter("КодПрацівника", Direction.output, Types.Int)
        params.add(eCode)
        val eName = Parameter("ПІБ", Direction.output, Types.VarChar)
        params.add(eName)
        DataBase.db.Execute(CMD_READ_USER, params) { result ->
            if(result.isOk) {
                val error = result.error()
                if (error == null || error.isEmpty()) {
                    employerCode = result.variables!!.getInt("КодПрацівника")!!
                    employerName = result.variables!!.getString("ПІБ")
                }
                listener?.onExecuted(context, error)
            }
        }
    } else {
        listener!!.onExecuted(context,"Помилка входу на сервер \nВведіть коректне ім'я сервера")
    }
}

class App : Application() {

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()
        DEVICE_ID = Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)
    }


}