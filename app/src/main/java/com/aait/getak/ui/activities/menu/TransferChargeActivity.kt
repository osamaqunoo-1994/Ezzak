package com.aait.getak.ui.activities.menu

import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.cities_model.CityModel
import com.aait.getak.models.countries_model.Country
import com.aait.getak.utils.Util
import com.aait.getak.utils.setMyAdapter
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_transfer_charge.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class TransferChargeActivity : ParentActivity() {
    private var country_id_: Int?=0
    override val layoutResource: Int
        get() = R.layout.activity_transfer_charge
    var keys:List<Country> = ArrayList()

    override fun onResume() {
        super.onResume()
        sendRequest()
    }

    override fun init() {
        setToolbar(getString(R.string.transfer_charge))
        setActions()
    }

    private fun sendRequest() {
        addDisposable(repo.getBalance(mPrefs!!.token!!)
            .subscribeWithLoading({
                showProgressDialog()
            },{
                if (it.value=="1"){
                    my_charge_tv.text= String.format(getString(R.string.available_charge,it.data!!.balance))
                    keys =it.data!!.countries
                    keys.let {
                        if(it.isNotEmpty()){
                            getKeys()

                        }
                    }
                }
                else{
                    toasty(it.msg!!,2)
                }
            }
                ,{
                    hideProgressDialog()
                    toasty(it.message!!,2)
                    Log.e("error",it.message!!)
                },
                {
                    hideProgressDialog()
                }))
    }


    private fun setActions() {
        continue_btn.onClick {
            val country = keys.find {
                 atv_codes.text.toString() == it.currency!!

            }

            if (Util.checkError(etValue,this@TransferChargeActivity,getString(R.string.empty_field))){
                    val intent = Intent(applicationContext, ChargePhoneActivity::class.java)
                    intent.putExtra("country_id",country!!.id)
                    intent.putExtra("amount",etValue.text.toString())
                    startActivity(intent)


            }
        }


    }



    fun getKeys(
        choice: Int? = 0,
        data: List<CityModel>?=null
    ){

        if (keys.isNotEmpty()) {
            country_id_ = keys[0].id
        }
        setSpinnerCodes(keys.map { it.currency.toString() /*+ " (${it.key}) "*/ })

    }

    private fun setSpinnerCodes(keys: List<String>) {
        //set keys for spinner
        val keysAdapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, keys)
        if (keys.isNotEmpty()) {
            atv_codes.setMyAdapter(keys, keysAdapter)
        }
    }


}
