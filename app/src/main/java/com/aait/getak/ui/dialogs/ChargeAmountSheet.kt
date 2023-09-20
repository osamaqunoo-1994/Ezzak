package com.aait.getak.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aait.getak.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_charge_amount.*

class ChargeAmountSheet( private val onChooseType:(amount:String)->Unit) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_charge_amount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_charge?.setOnClickListener {
            if(!et_charge_value?.text.isNullOrEmpty())
                onChooseType.invoke(et_charge_value.text.toString())
        }
    }
}