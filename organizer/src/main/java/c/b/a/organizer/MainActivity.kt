package c.b.a.organizer

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val NUM_PAGES = 3
var firstLoad: Boolean = false
const val LOG_TAG = "ORGANIZER"

class MainActivity : AppCompatActivity() {

    private lateinit var mPagerAdapter: PagerAdapter
    var fragments = ArrayList<TaskListFragment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstLoad = true

        task_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val title = fragments[position].title
                setTitle(title)
            }
        })
        mPagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        task_pager.adapter = mPagerAdapter

        fillFragments()
    }

    private fun fillFragments() {
        val fragment1 = TaskListFragment.newInstance("Органайзер.Заплановано_Зчитати", "Усі завдання")
        fragment1.loadListener = object : TaskListFragment.LoadListener {
            override fun started() {
                notTouchableScreen(true)

            }

            override fun completed() {
                notTouchableScreen(false)
            }
        }
        fragments.add(fragment1)

        val fragment2 = TaskListFragment.newInstance("Органайзер.Поточні_Зчитати", "Поточні завдання")
        fragment2.loadListener = object : TaskListFragment.LoadListener {
            override fun started() {
                notTouchableScreen(true)
            }

            override fun completed() {
                if (firstLoad) {
                    if (fragments[1].hasTasks()) {
                        task_pager.currentItem = 1
                    }
                    firstLoad = false
                }
                notTouchableScreen(false)
            }
        }
        fragments.add(fragment2)

        val fragment3 = TaskListFragment.newInstance("Органайзер.Виконано_Зчитати", "Виконані завдання")
        fragment3.loadListener = object : TaskListFragment.LoadListener {
            override fun started() {
                notTouchableScreen(true)
            }

            override fun completed() {
                notTouchableScreen(false)
            }
        }
        fragments.add(fragment3)
    }

    private fun notTouchableScreen(notTouchable: Boolean) {
        if (notTouchable) {
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_organizer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                val position = task_pager.currentItem
                fragments[position].loadData()
            }
            R.id.menu_change_server -> {
                val intent = Intent(this, SetupServerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (task_pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            task_pager.currentItem = task_pager.currentItem - 1
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return NUM_PAGES
        }
    }

}
