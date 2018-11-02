package ua.com.info.common.menu

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import ua.com.info.common.R
import ua.com.info.common.Utils
import ua.com.info.common.getScreenSize
import java.util.*


/**
 * Створено v.m 31.03.2015.
 */

class ButtonMenu(context: Context, attrs: AttributeSet) : GridView(context, attrs), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private var btnHeight: Int = 0
    private var onActionListener: OnActionListener? = null

    private var onBeforeStartActivityListener: OnBeforeStartActivityListener? = null

    private var menuDescription: MenuDescription? = null
    private var itemHeight: Int = 0
    private var ts: Float = 0F

    interface OnActionListener {
        fun canDo(): Boolean
    }

    interface OnBeforeStartActivityListener {
        fun setupIntent(intent: Intent)
    }

    fun setOnActionListener(listener: OnActionListener) {
        onActionListener = listener
    }

    fun setOnBeforeStartActivityListener(listener: OnBeforeStartActivityListener) {
        onBeforeStartActivityListener = listener
    }


    private inner class MenuAdapter(context: Context, elements: ArrayList<MenuDescription.MenuGroup>, private val resource: Int) : ArrayAdapter<MenuDescription.MenuGroup>(context, resource, elements) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            if (view == null) {
                view = inflater.inflate(resource, null)
            }
            val btnLayout: LinearLayout = view as LinearLayout
            val itemHeight: Int = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                (getScreenSize(context).y * 0.25).toInt()
            else
                (getScreenSize(context).y * 0.39).toInt()

            btnLayout.layoutParams = LayoutParams(GridView.AUTO_FIT, itemHeight)

            val item = getItem(position)

            //-----------------------------
            fillButtons(view, position, inflater)
            //------------------------------
            val txt = view.findViewById<View>(android.R.id.text1) as TextView
            val itemIcon = view.findViewById<ImageView>(R.id.item_icon)
            val btnTitle = item.name
            when(btnTitle){
                "Видача товару"->{
                    itemIcon.setImageResource(R.drawable.product_deliv)
                }
                "Отримання товару"->{
                    itemIcon.setImageResource(R.drawable.product_receive)
                }
                "Залишки товару"->{
                    itemIcon.setImageResource(R.drawable.product_remmance)
                }
                else ->
                {
                    itemIcon.setImageResource(R.drawable.info)

                }
            }
            txt.text = btnTitle
            if (ts != 0f)
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, ts)
            else
                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            return view
        }

        @SuppressLint("InflateParams")
        fun fillButtons(view: View, position: Int, inflater: LayoutInflater) {
            val subItems = getItem(position).subItems
            val layout = view.findViewById<LinearLayout>(R.id.ll)
            if (layout.childCount > 0) {
                layout.removeAllViews()
                layout.layoutParams.height = 0
            }
            if (subItems.size > 1) {
                val parent = parent as ViewGroup
                val btnHeight = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    (parent.height * 0.11).toInt()
                else
                    (parent.height * 0.20).toInt()
                layout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, btnHeight, 1f)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                for ((index, s) in subItems.withIndex()) {
                    val btn = inflater.inflate(R.layout.image_document, null) as ImageView
                    btn.setImageResource(s.iconId)
                    btn.layoutParams = params
                    if (index > 0) {
                        val line = View(context)
                        val p = LinearLayout.LayoutParams(verticalSpacing, LayoutParams.MATCH_PARENT)
                        line.layoutParams = p
                        line.setBackgroundColor(ContextCompat.getColor(context, R.color.app_background))
                        layout.addView(line)
                    }

                    layout.addView(btn)
                    if (s.onClickListener != null)
                        btn.setOnClickListener(s.onClickListener)
                    else
                        btn.setOnClickListener {
                            if (!s.onClick(context))
                                startAction(s.action, s.requestCode)
                        }
                }
            }
        }
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonMenu, 0, 0)
        try {
            val h = ta.getDimension(R.styleable.ButtonMenu_itemHeight, 0f)
            if (h != 0f) {
                val scale = getContext().resources.displayMetrics.density
                itemHeight = (h * scale + 0.5f).toInt()
            }
            ts = ta.getDimension(R.styleable.ButtonMenu_fontSize, 0f)
        } finally {
            ta.recycle()
        }
        btnHeight = getScreenSize(context).y / 6
    }

    fun setMenuDescription(menuDescription: MenuDescription) {
        this.menuDescription = menuDescription
    }

    fun setItems() {
        adapter = MenuAdapter(context, menuDescription!!.groups, R.layout.item_main_menu)
        onItemClickListener = this
        onItemLongClickListener = this
        val numColumns: Int
        numColumns = if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            3
        else {
            val size = menuDescription!!.groups.size
            when {
                size <= 3 -> 3
                size == 4 -> 4
                size <= 9 -> 3
                else -> 4
            }
        }
        setNumColumns(numColumns)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        onClick(position, false)
    }

    override fun onItemLongClick(adapterView: AdapterView<*>, view: View, position: Int, l: Long): Boolean {
        onClick(position, true)
        return true
    }

    private fun startAction(action: Class<*>?, requestCode: Int) {
        if (action == null) return
        Utils.menuActivityClass = action
        if (onActionListener != null && !onActionListener!!.canDo()) return

        val intent = Intent(context, action)
        if (onBeforeStartActivityListener != null)
            onBeforeStartActivityListener!!.setupIntent(intent)

        val context = context
        if (context is Activity && requestCode != 0)
            context.startActivityForResult(intent, requestCode)
        else if (intent.resolveActivityInfo(context.packageManager, 0) != null)
            context.startActivity(intent)
    }

    private fun onClick(position: Int, longClick: Boolean) {
        val group = menuDescription!!.groups[position]
        val index = 0
        val item = group.subItems[index]
        if (!item.onClick(context))
            startAction(item.action, item.requestCode)
    }

    private fun showSubMenu(group: MenuDescription.MenuGroup) {
        val d = Dialog(context)
        d.setContentView(R.layout.dialog_sub_menu)
        val adapter = ArrayAdapter(context, R.layout.item_main_menu, android.R.id.text1, group.subItems)// todo: change the layout
        val listView = d.findViewById<View>(android.R.id.list) as ListView
        listView.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { adapterView, view, index, l ->
            d.dismiss()
            startAction(adapter.getItem(index)!!.action, adapter.getItem(index)!!.requestCode)
        }

        val lp = listView.layoutParams as LinearLayout.LayoutParams

        val w = lp.width + lp.leftMargin + lp.rightMargin + 15
        d.setTitle(group.name)
        d.show()
        d.window!!.setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
