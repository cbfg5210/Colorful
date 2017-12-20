package com.ue.colorful.feature.main

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.ColorFunCategory
import com.ue.colorful.model.ColorFunction
import kotlinx.android.synthetic.main.item_color_fun_category.view.*

/**
 * Created by hawk on 2017/12/14.
 */
internal class ColorFunCategoryAdapter(private val activity: Activity, items: List<ColorFunCategory>) : DelegationAdapter<ColorFunCategory>(), OnDelegateClickListener {

    init {
        this.items = items

        val delegate = ColorFunCategoryDelegate(activity)
        delegate.setOnDelegateClickListener(this)
        this.addDelegate(delegate)
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        if (view.tag == null) {
            return
        }
        val tag = view.tag
        if (!(tag is Int)) {
            return;
        }
        ContainerActivity.start(activity, items[position].funs[tag])
    }

    /**
     * Delegate
     */
    private class ColorFunCategoryDelegate(private val activity: Activity) : BaseAdapterDelegate<ColorFunCategory>(activity, R.layout.item_color_fun_category) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: ColorFunCategory): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ColorFunCategory, payloads: List<*>) {
            holder as ViewHolder

            holder.tvCatName.text = item.catName
            holder.tvCatName.setBackgroundColor(Color.parseColor("#${item.colorHex}"))

            var count = 0
            val funs = item.funs
            var func: ColorFunction
            for (i in funs.indices) {
                func = funs.get(i)
                val textView = AppCompatTextView(activity)
                textView.setPadding(20, 40, 20, 40)
                textView.textSize = 17F
                val drawable = StateListDrawable()
                drawable.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(Color.parseColor("#99${item.colorHex}")))
                drawable.addState(intArrayOf(), ColorDrawable(Color.parseColor("#CC${item.colorHex}")))
                textView.setBackgroundDrawable(drawable)

                textView.text = func.funName
                textView.tag = i
                textView.setOnClickListener { onDelegateClickListener?.onClick(textView, holder.adapterPosition) }
                holder.vgCatContainer.addView(textView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))

                count++

                if (count < funs.size) {
                    val divider = View(activity)
                    divider.setBackgroundColor(Color.parseColor("#${item.colorHex}"))
                    holder.vgCatContainer.addView(divider, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2))
                }
            }
        }

        private class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val vgCatContainer = itemView.vgCatContainer
            val tvCatName = itemView.tvCatName
        }
    }
}