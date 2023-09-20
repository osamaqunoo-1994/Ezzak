package com.aait.getak.utils

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import java.util.*

class LocationAddress : IntentService(IDENTIFIER) {
    private var addressResultReceiver: ResultReceiver? = null
    //handle the address request
    override fun onHandleIntent(intent: Intent?) {
        var msg = ""
        //get result receiver from intent
        addressResultReceiver = intent!!.getParcelableExtra("add_receiver")
        if (addressResultReceiver == null) {
            Log.e(
                "GetAddressIntentService",
                "No receiver, not processing the request further"
            )
            return
        }
        val location = intent.getParcelableExtra<Location>("add_location")
        //send no location error to results receiver
        if (location == null) {
            msg = "No location, can't go further without location"
            sendResultsToReceiver(0, msg,"")
            return
        }
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        } catch (ioException: Exception) {
            Log.e("", "Error in getting address for the location")
        }
        if (addresses == null || addresses.isEmpty()) {
            msg = "No address found for the location"
            sendResultsToReceiver(1, msg,"")
        } else {
            val address = addresses[0]
            val s = address.getAddressLine(0)
            Log.e("address",s)
            Log.e("address2",address.featureName)

            val addressDetails = StringBuffer()
            addressDetails.append(address.featureName)
            addressDetails.append("\n")
            addressDetails.append(address.thoroughfare)
            addressDetails.append("\n")
            addressDetails.append("Locality: ")
            addressDetails.append(address.locality)
            addressDetails.append("\n")
            addressDetails.append("County: ")
            addressDetails.append(address.subAdminArea)
            addressDetails.append("\n")
            addressDetails.append("State: ")
            addressDetails.append(address.adminArea)
            addressDetails.append("\n")
            addressDetails.append("Country: ")
            addressDetails.append(address.countryName)
            addressDetails.append("\n")
            addressDetails.append("Postal Code: ")
            addressDetails.append(address.postalCode)
            addressDetails.append("\n")
            sendResultsToReceiver(2, address.featureName+"",s)
        }
    }


    //to send results to receiver in the source activity
    private fun sendResultsToReceiver(resultCode: Int, title: String,address:String) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("address", address)
        addressResultReceiver!!.send(resultCode, bundle)
    }

    companion object {
        private const val IDENTIFIER = "GetAddressIntentService"
    }
}