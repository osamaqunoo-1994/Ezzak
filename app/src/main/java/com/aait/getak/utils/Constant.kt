package com.aait.getak.utils

import com.aait.getak.BuildConfig

object Constant {
    val SEARCH = "searchquery"
    val EDITPROFILE = "editprofile"
    val PAYMENT_URL="https://cp.ezzk.sa/payment"
    val BASE_URL="https://cp.ezzk.sa"
    val HELP_URL   ="https://cp.ezzk.sa/payment"
    val TERMS_URL   ="https://cp.ezzk.sa/conditions/en"
    val APP_URL   ="https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    val GPS=9000
    val ZOOM = 14f
    //location
    const val SUCCESS_RESULT = 0
    const val FAILURE_RESULT = 1
    const val PACKAGE_NAME = "com.google.android.gms.location.sample.locationaddress"
    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
    const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
    const val LOCATION_DATA_EXTRA = "${PACKAGE_NAME}.LOCATION_DATA_EXTRA"

    const  val CAMERA_MAX_FILE_SIZE_BYTE = 2 * 1024 * 1024
    const  val REQUEST_CODE_TAKE_PHOTO = 101
    const  val REQUEST_CODE_PICK_PHOTO = 102
    object UserType {
        val INVESTOR = 1
        val NON_INVESTOR = 2
    }

    object PermissionCode {
        val STORAGE = 1
        val CAMERA = 8
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 105

    }

    object RequestCode {
        var GETLOCATION = 500
        val PHOTO_CHOOSE = 3
        val GPSEnabling = 300
        val Call = 100
        val Take_PICTURE = 9


    }

}
