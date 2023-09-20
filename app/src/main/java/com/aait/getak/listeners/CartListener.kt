package com.aait.getak.listeners

import com.aait.getak.models.store_details_model.ProductX


interface CartListener {
    fun onPlus(count:Int,current_count:Float,product: ProductX)
    fun onMinus(count:Int,current_count:Float,product: ProductX)
}