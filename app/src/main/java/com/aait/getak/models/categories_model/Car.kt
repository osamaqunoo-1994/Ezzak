package com.aait.getak.models.categories_model


import com.google.gson.annotations.SerializedName

data class Car(
    var currency: String?,
    @SerializedName("expected_price")
    var expectedPrice: String?,
    var id: Int?,
    var image: String?,
    @SerializedName("max_weight")
    var maxWeight: String?,
    var name: String?,
    @SerializedName("num_persons")
    var numPersons: Int?,
    @SerializedName("price_id")
    var priceId: Int?,
    var type: String?,
    @SerializedName("have_coupon")
    var haveCoupon:String = "false",
    @SerializedName("price_after_coupon")
    var priceAfterCoupon:String = ""
){
    override fun toString(): String {
        return name!!
    }
}