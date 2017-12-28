package com.ue.fingercoloring.feature.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
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
    private lateinit var adapter: LocalPaintAdapter
    private var localWorks: List<LocalWork>? = null
    private var disposable: Disposable? = null
    private var newWorkPath = ""
    private var pickedWorkPos = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_works, container, false)

        adapter = LocalPaintAdapter(activity, localWorks)
        rootView.userpaintlist.adapter = adapter
        rootView.userpaintlist.setEmptyView(rootView.emptylay_paintlist)

        return rootView
    }

    private fun loadLocalWorks() {
        disposable = Observable
                .create(ObservableOnSubscribe<List<LocalWork>> { e ->
                    val results = FileUtils.obtainLocalImages()
                    if (results != null) e.onNext(results)
                    e.onComplete()
                })
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mLocalWorks ->
                    adapter.items.clear()
                    adapter.items.addAll(mLocalWorks)
                    adapter.notifyDataSetChanged()

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
            loadLocalWorks()
            return
        }
        if (!TextUtils.isEmpty(newWorkPath)) {
            //添加了新作品
            return
        }
        if (pickedWorkPos >= 0) {
            //从作品列表进入了编辑页，更新该项
            return
        }
    }

    companion object {

        fun newInstance(): WorksFragment {
            return WorksFragment()
        }
    }
}
