package com.aait.getak.ui.activities.menu

import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_share.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ShareActivity : ParentActivity() {
    private var share_code: String?=""
    private var balance: String?=""
    override val layoutResource: Int
        get() = R.layout.activity_share

    override fun init() {
        setToolbar(getString(R.string.free_trips))
        addDisposable(repo.invitation(mPrefs!!.token!!).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){
                share_code=it.data!!.shareCode
                balance=it.data!!.inviteClientBalance
                obtain.text = String.format(getString(R.string.price_invite), balance+" "+it.data?.currency)
                obtain_msg.text=String.format(getString(R.string.invite_friends),balance+" "+it.data?.currency)
                code.setText(it.data!!.shareCode)
            }
            else{
                toasty(it.msg!!,2)
            }
        },{
            hideProgressDialog()
            toasty(getString(R.string.error_connection),2)
        },{
            hideProgressDialog()
        }))

        share_lay.onClick {
            Util.ShareApp(this@ShareActivity,share_code)
        }
        save_btn.onClick {
            Util.copy(this@ShareActivity,share_code.toString())
        }
    }

}
