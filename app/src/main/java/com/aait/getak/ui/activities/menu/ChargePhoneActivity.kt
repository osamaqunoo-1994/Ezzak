package com.aait.getak.ui.activities.menu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_charge_phone.*
import kotlinx.android.synthetic.main.activity_register_phone.continue_btn
import kotlinx.android.synthetic.main.activity_register_phone.error_txt
import kotlinx.android.synthetic.main.activity_register_phone.etPhone
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel

class ChargePhoneActivity : ParentActivity() {
    private val CONTACT_CODE: Int=1
    private val viewModel: RegisterViewModel by viewModel()


    override val layoutResource: Int
        get() = R.layout.activity_charge_phone

    override fun init() {
        setToolbar(getString(R.string.transfer_charge))

        continue_btn.onClick {
            if (checkPhone()){
                sendRequest()
            }
        }
        iv_contact.onClick {
                           setPermissionsContact {
                               val intent =  Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                               startActivityForResult(intent, CONTACT_CODE)
                           }

        }


    }

    private fun sendRequest() {
        addDisposable(repo.transferCharge(intent.getIntExtra("country_id",0).toString(),etPhone.text.toString(),
            intent.getStringExtra("amount")!!,mPrefs!!.token!!
            ).subscribeWithLoading({
            showProgressDialog()
        },{
            if (it.value=="1"){
                val intent = Intent(this, TransSuccessActivity::class.java)
                startActivity(intent)
            }
            else{
                toasty(it.msg!!,2)
            }

        },{
            toasty(it.message!!,2)
        },{
            hideProgressDialog()
        }))
    }



    @SuppressLint("CheckResult")
    private fun checkPhone():Boolean{
        var isCorrect=false
        viewModel.checkPhone(etPhone)!!.subscribe {
            if (!it) {
                error_txt.visibility= View.VISIBLE
                error_txt.text=getString(R.string.check_phone)
            }
            else {
                error_txt.visibility= View.GONE
                isCorrect=true
            }

        }
        return isCorrect
    }


    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==CONTACT_CODE && resultCode== Activity.RESULT_OK){
            val contactData = data?.data
            val c =  managedQuery(contactData, null, null, null, null)
            if (c.moveToFirst()) {


                val id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))

                val hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone.equals("1",true)) {
                    val phones = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                        null, null)
                    phones?.moveToFirst()
                    val cNumber = phones?.getString(phones.getColumnIndex("data1"))
                    etPhone.setText(cNumber)
                }


            }
    }

}
}
