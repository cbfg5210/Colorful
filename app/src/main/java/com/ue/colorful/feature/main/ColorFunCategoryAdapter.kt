package com.ue.colorful.feature.main

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.constant.FunFlags
import com.ue.colorful.model.ColorFunCategory
import com.ue.colorful.model.ColorFunction
import com.ue.fingercoloring.feature.main.MainListActivity
import kotlinx.android.synthetic.main.item_color_fun_category.view.*

/**
 * Created by hawk on 2017/12/14.
 */
internal class ColorFunCategoryAdapter(private val activity: Activity, items: List<ColorFunCategory>) : DelegationAdapter<ColorFunCategory>(), OnDelegateClickListener {

    init {
        if (items != null) this.items.addAll(items)

        val delegate = ColorFunCategoryDelegate(activity)
        delegate.onDelegateClickListener = this
        this.addDelegate(delegate)
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        val tag = view.tag as? Int ?: return
        if (items[position].funs[tag].funFlag == FunFlags.GAME_FG_COLORING) MainListActivity.start(activity)
        else ContainerActivity.start(activity, items[position].funs[tag])
    }

    /**
     * Delegate
     */
    private class ColorFunCategoryDelegate(private val activity: Activity) : BaseAdapterDelegate<ColorFunCategory>(activity, R.layout.item_color_fun_category) {
        private val padding5 = activity.resources.getDimension(R.dimen.widget_size_5).toInt()
        private val padding10 = activity.resources.getDimension(R.dimen.widget_size_10).toInt()
        private val textSize = activity.resources.getDimension(R.dimen.font_size_18)

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: ColorFunCategory): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ColorFunCategory, payloads: List<Any>) {
            holder as ViewHolder

            holder.tvCatName.text = item.catName
            holder.tvCatName.setBackgroundColor(Color.parseColor("#${item.colorHex}"))

            var count = 0
            var func: ColorFunction
            for (i in item.funs.indices) {
                func = item.funs[i]
                val textView = AppCompatTextView(activity)
                textView.setPadding(padding5, padding10, padding5, padding10)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                val drawable = StateListDrawable()
                drawable.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(Color.parseColor("#99${item.colorHex}")))
                drawable.addState(intArrayOf(), ColorDrawable(Color.parseColor("#CC${item.colorHex}")))
                textView.setBackgroundDrawable(drawable)

                textView.text = func.funName
                textView.tag = i
                textView.setOnClickListener { onDelegateClickListener?.onClick(textView, holder.adapterPosition) }
                holder.vgCatContainer.addView(textView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

                count++

                if (count < item.funs.size) {
                    val divider = View(activity)
                    divider.setBackgroundColor(Color.parseColor("#${item.colorHex}"))
                    holder.vgCatContainer.addView(divider, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2))
                }
            }
        }

        private class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val vgCatContainer = itemView.vgCatContainer!!
            val tvCatName = itemView.tvCatName!!
        }
    }
}