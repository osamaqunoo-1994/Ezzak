package com.aait.getak.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aait.getak.R
import com.aait.getak.models.payment.PaymentWaysData
import com.aait.getak.ui.adapters.PaymentWaysAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.my_wallet_full.*

class PaymentBottomSheet(private val paymentWaysList:MutableList<PaymentWaysData>, private val onChooseType:(type:String)->Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_payment_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPaymentAdapter()
    }

    private fun setPaymentAdapter(){
        val removedAppleValue = paymentWaysList.find { it.type?.contains("APPLE") == true }
        paymentWaysList.remove(removedAppleValue)
        val paymentWaysAdapter = PaymentWaysAdapter(requireContext(),paymentWaysList){
            dialog?.dismiss()
            onChooseType(it)
        }
        rv_payment_ways.adapter = paymentWaysAdapter
    }

}