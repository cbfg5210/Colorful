package com.ue.colorful.feature.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.tencent.bugly.beta.Beta
import com.ue.aboutpage.AboutPageView
import com.ue.aboutpage.DetailItem
import com.ue.colorful.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        title = getString(R.string.about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        apvAboutPage.setShareContent(getString(R.string.shareContent, getString(R.string.appDesc)))
        apvAboutPage.setFaqItems(faqItems)
        apvAboutPage.setVerNoteItems(verNoteItems)

        apvAboutPage.toggleAppDescDetail()

        apvAboutPage.setAboutItemClickListener(object : AboutPageView.OnAboutItemClickListener {
            override fun onVersionClicked() {
                Beta.checkUpgrade(true, false)
            }

            override fun onSupportClicked() {
                showSupportDialog()
            }
        })
    }

    private val verNoteItems: List<DetailItem>
        get() {
            val items = ArrayList<DetailItem>()
            items.add(DetailItem(getString(R.string.ver_0d6), getString(R.string.verNote_0d6)))
            items.add(DetailItem(getString(R.string.ver_0d5), getString(R.string.verNote_0d5)))
            return items
        }

    private val faqItems: List<DetailItem>
        get() {
            val items = ArrayList<DetailItem>()
            val faqQues = resources.getStringArray(R.array.faqQues)
            val faqAnss = resources.getStringArray(R.array.faqAnss)
            for (i in faqQues.indices) items.add(DetailItem(faqQues[i], faqAnss[i]))
            return items
        }

    private fun showSupportDialog() {
        val supportDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.support))
                .setMessage(getString(R.string.support_tip))
                .setPositiveButton(getString(R.string.sure), null)
                .create()

        supportDialog.setOnDismissListener({ _ ->
            {
                //ADManager.showInterstitialAd(this@AboutActivity, supportInterstitialAd)
            }
        })

        supportDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
