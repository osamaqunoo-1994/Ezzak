package com.aait.getak.ui.activities.menu

import android.content.Intent
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.activities.core.MapPreviewActivity
import kotlinx.android.synthetic.main.activity_trans_success.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class TransSuccessActivity : ParentActivity() {
    override val layoutResource: Int
        get() = R.layout.activity_trans_success

    override fun init() {
        back_btn.onClick {
            val intent = Intent(this@TransSuccessActivity, MapPreviewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }


}
