package com.ue.fingercoloring.controller.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.fingercoloring.R
import com.ue.fingercoloring.model.LocalWork
import com.ue.fingercoloring.util.FileUtils
import com.ue.fingercoloring.util.RxJavaUtils
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_works.view.*
import kotlinx.android.synthetic.main.view_emptylist.view.*

/**
 * Created by Swifty.Wang on 2015/8/18.
 */
class WorksFragment : Fragment() {
    private var adapter: LocalPaintAdapter? = null
    private var localWorks: List<LocalWork>? = null
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_works, container, false)

        adapter = LocalPaintAdapter(activity, localWorks)
        rootView.userpaintlist.adapter = adapter
        rootView.userpaintlist.setEmptyView(rootView.emptylay_paintlist)

        return rootView
    }

    private fun loadLocalPaints() {
        disposable = Observable
                .create(ObservableOnSubscribe<List<LocalWork>> { e ->
                    val results = FileUtils.obtainLocalImages()
                    if (results != null) e.onNext(results)
                    e.onComplete()
                })
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mLocalWorks ->
                    adapter!!.items.clear()
                    adapter!!.items.addAll(mLocalWorks)
                    adapter!!.notifyDataSetChanged()

                    localWorks = mLocalWorks
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        RxJavaUtils.dispose(disposable)
    }

    override fun onResume() {
        super.onResume()
        if (localWorks == null) {
            loadLocalPaints()
        }
    }

    companion object {

        fun newInstance(): WorksFragment {
            return WorksFragment()
        }
    }
}
