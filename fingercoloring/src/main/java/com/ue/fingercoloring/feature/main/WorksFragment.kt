package com.ue.fingercoloring.feature.main

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ue.fingercoloring.R
import com.ue.fingercoloring.model.LocalWork
import com.ue.fingercoloring.util.FileUtils
import com.ue.fingercoloring.util.RxJavaUtils
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
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
    private var hasExternalPermissions = false

    companion object {
        private val REQ_PERMISSION = 10
        fun newInstance(): WorksFragment {
            return WorksFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_works, container, false)

        adapter = LocalPaintAdapter(activity, localWorks)
        rootView.userpaintlist.adapter = adapter
        rootView.userpaintlist.setEmptyView(rootView.emptylay_paintlist)

        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkExternalPermissions()
    }

    private fun checkExternalPermissions() {
        AndPermission.with(this)
                .requestCode(REQ_PERMISSION)
                .permission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .rationale { requestCode, rationale -> AndPermission.rationaleDialog(context, rationale).show() }
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
                        if (requestCode == REQ_PERMISSION) {
                            hasExternalPermissions = true
                            onResume()
                        }
                    }

                    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
                        Toast.makeText(context, R.string.no_external_permission, Toast.LENGTH_SHORT).show()
                    }
                })
                .start()
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
                }, { t ->
                    Toast.makeText(context, getString(R.string.read_data_error, t.message), Toast.LENGTH_SHORT).show()
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        RxJavaUtils.dispose(disposable)
    }

    override fun onResume() {
        super.onResume()

        if (!hasExternalPermissions) return

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
}
