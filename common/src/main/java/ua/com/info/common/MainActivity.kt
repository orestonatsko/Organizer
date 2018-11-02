package ua.com.info.common

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList

import ua.com.info.common.menu.Initializer
import ua.com.info.common.menu.MainMenu
import ua.com.info.data.DataBase

/**
 * Створено V.M 30.03.2015.
 */


class MainActivity : BaseActivity() {

    private val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
                // Login.logout();
                finish()
            DialogInterface.BUTTON_NEGATIVE -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        currentInstance = this
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        val b = intent.extras
        if (b != null) {
            val reset = b.getBoolean("reset")
            if (reset)
                clear()
        }
        if (initializer != null)
            initializer!!.initialize(this)
        connectDB()
        val viewPager = findViewById<View>(R.id.flip) as ViewPager
        val pagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)

        viewPager.adapter = pagerAdapter
        // viewPager.setOnPageChangeListener(null);

        //        syncState = (SyncState) findViewById(R.id.SyncState);
        //        connectionState = (ConnectionState) findViewById(R.id.ConnectionState);
        //        syncState.setConnectionState(connectionState);
        //        gpsState = (GPSState) findViewById(R.id.GPSState);

        //        ImageView exit = (ImageView) findViewById(R.id.Exit);
        //        if (Login.isMultiUserMode())
        //            exit.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View view) {
        //                    Login.logout();
        //                    MainActivity.this.finish();
        //                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
        //                    intent.putExtra(StartActivity.MODE, SyncResult.eResult.none);
        //                    MainActivity.this.startActivity(intent);
        //                }
        //            });
        //        else
        //            exit.setVisibility(View.GONE);
                pageSelector.setPager(viewPager);

    }

    private fun checkDB() {
        if (!DataBase.db.isConnected) {
            msgReconnect(this)
        } else {
            connecting_message.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1) {
            connectDB()
        } else {
            System.exit(1)
        }
    }

    private fun connectDB() {
        connecting_message.visibility = View.VISIBLE
        DataBase.db.openConnection()
    }

    public override fun onResume() {
        super.onResume()
        checkDB()
        //        syncState.Refresh();
        //        connectionState.refreshState();
        //        gpsState.Refresh();
    }

    private inner class MyFragmentPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return Fragment.instantiate(this@MainActivity, pages[position].name)
        }

        override fun getCount(): Int {
            return pages.size
        }
    }

    override fun onBackPressed() {
        if (pages.size > 1) {
            val viewPager = findViewById<View>(R.id.flip) as ViewPager
            if (viewPager.currentItem > 0) {
                viewPager.setCurrentItem(0, false)
                return
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Завершити роботу з програмою?").setNegativeButton("Ні", dialogClickListener).setPositiveButton("Так", dialogClickListener).show()
    }

    companion object {

        var initializer: Initializer? = object : Initializer() { // По замовчування тільки головне меню
            override fun init(activity: BaseActivity) {
                MainActivity.addPage(MainMenu::class.java)
            }
        }

        private val pages = ArrayList<Class<*>>()

        fun addPage(fragment: Class<*>) {
            pages.add(fragment)
        }

        fun clear() {
            pages.clear()
            if (initializer != null) initializer!!.clear()
            if (MainMenu.initializer != null) MainMenu.initializer!!.clear()
        }

        //private SyncState syncState;
        //private ConnectionState connectionState;
        //private GPSState gpsState;

        private var currentInstance: MainActivity? = null

        private fun getThrowableInfo(e: Throwable?): String {
            if (e == null) return ""
            val ret = StringBuilder(e.message + "\n")
            for (el in e.stackTrace) {
                ret.append(el.toString()).append("\n")
            }
            ret.append("\n")
            ret.append(getThrowableInfo(e.cause))
            return ret.toString()
        }

        fun handleUncaughtException(e: Throwable) {
            try {
                //            String msg = getThrowableInfo(e);
                //            ContentValues cv = new ContentValues();
                //            cv.put("id", -IntDate.Now());
                //            cv.put("Date", IntDate.Now());
                //            cv.put("Exception", ";\nVersion"+ Utils.VersionCode + ":\n" + msg);
                //            cv.put("Status", 1);
                //            DbHelper.mdb.replace("AndroidExceptions", null, cv);
                MainActivity.restartApplication(MainActivity.currentInstance!!)
            } catch (ex: Exception) {
                //Якщо вже тут помилка, то просто нічого не робимо...
            }

            System.exit(1)
        }

        private fun restartApplication(c: Context) {
            val ma = MainActivity.currentInstance
            val bc = ma!!.baseContext
            val i = bc.packageManager.getLaunchIntentForPackage(bc.packageName)
            i!!.putExtra("restart", true)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            c.startActivity(i)
        }
    }
}
