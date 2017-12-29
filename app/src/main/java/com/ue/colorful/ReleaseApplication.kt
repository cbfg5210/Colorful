package com.ue.colorful

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.support.v7.app.AppCompatDelegate
import com.tencent.bugly.Bugly
import com.ue.library.util.SPUtils
import com.ue.recommend.util.BmobUtils

/**
 * Created by hawk on 2017/8/23.
 */
class ReleaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 多进程启动会多次调用 Application＃onCreate() 方法，区分进程作初始化操作，能有效减少不必要的开销
        val packageName = packageName
        if (packageName == getProcessName(this)) {// init for main process
            //support svg for TextView drawableLeft
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            // init shared preferences util
            SPUtils.init(this)
            // init bugly
            Bugly.init(this, BuildConfig.BUGLY_APP_ID, false);
            //init bmob
            BmobUtils.getInstance().initBmob(BuildConfig.BMOB_APP_ID, BuildConfig.BMOB_API_KEY);
            // init debug tools
            initDebugTools()
        } else {
            // init for other process
        }
    }

    /**
     * @see DebugAppContext
     */
    open fun initDebugTools() {}

    private fun getProcessName(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infoApp = am.runningAppProcesses ?: return ""
        for (proInfo in infoApp) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return ""
    }
}