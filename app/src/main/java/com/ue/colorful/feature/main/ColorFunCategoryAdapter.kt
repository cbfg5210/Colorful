package com.ue.colorful.feature.main

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.google.android.flexbox.FlexboxLayout
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.ColorFunCategory
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
        Toast.makeText(activity, "item click", Toast.LENGTH_SHORT).show()
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

            if (holder.adapterPosition == 0) {
                holder.tvCatName.setBackgroundColor(Color.parseColor("#99000000"))
                holder.vgFunctions.setBackgroundColor(Color.parseColor("#7F000000"))
            } else {
                holder.tvCatName.setBackgroundColor(Color.parseColor("#7F000000"))
                holder.vgFunctions.setBackgroundColor(Color.parseColor("#66000000"))
            }

            for (func in item.funs) {
                val textView = AppCompatTextView(activity)
                textView.setPadding(20, 20, 20, 20)
                textView.textSize = 17F
                textView.text = func.funName
                textView.tag = func.funFlag
                textView.setOnClickListener { onDelegateClickListener?.onClick(textView, holder.adapterPosition) }
                holder.vgFunctions.addView(textView, FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT))
            }
        }

        private class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvCatName = itemView.tvCatName
            val vgFunctions = itemView.vgFunctions
        }
    }
}