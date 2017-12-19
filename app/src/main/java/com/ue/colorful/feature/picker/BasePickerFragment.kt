package com.ue.colorful.feature.picker

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.ue.colorful.event.ContainerCallback

/**
 * Created by hawk on 2017/12/15.
 */
abstract class BasePickerFragment(private val layoutRes: Int, private val menuRes: Int) : Fragment() {
    protected lateinit var rootView: View
    protected var containerCallbck: ContainerCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        rootView = inflater.inflate(layoutRes, container, false)
        return rootView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ContainerCallback) containerCallbck = context
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        if (menuRes > 0) inflater.inflate(menuRes, menu)
    }
}