package com.aait.getak.models.order_details_model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("app_percentage")
    var appPercentage: Double?,
    var currency: String?,
    var date: String?,
    var distance: String?,
    var id: Int?,
    @SerializedName("initial_wait")
    var initialWait: String?,
    @SerializedName("open_counter")
    var initial: Float?,
    @SerializedName("payment_type")
    var paymentType: String?="cash",
    var period: String?,
    var starting: String?,
    @SerializedName("moving_price")
    var moving: String?,
    @SerializedName("wait_price")
    var waiting: String?,
    var subtotal: String?,
    var vat: String?="0",
    //total=subtotal+VAT-("promo code")
    @SerializedName("price")
    var total : Float?,
    @SerializedName("name")
    var captin_name:String?,
    var captain_id:Int?,
    @SerializedName("avatar")
    var captin_img:String?,
    @SerializedName("rate")
    var captin_rate:Float?,
    @SerializedName("car")
    var car_model:String?,
    @SerializedName("car_number")
    var car_plate:String?,
    @SerializedName("added_balance")
    var pocket:String?,
    var rush_hour_percentage:String?,
    var paid_visa:String,

    var paid_cash:String,
    var start_lat:Double,
    var start_long:Double,
    var start_address:String,
    var conversation_id:String,
    var total_required_price:String?,
    var end_lat:Double,
    var end_long:Double,
    var end_address:String="",
    var paid_balance:String,
    var coupon_discount:Float,
    var msg:String?="",
    var order_id:Int?=0,
    var status:String?=null,
    var has_captain:Boolean?=false,

)