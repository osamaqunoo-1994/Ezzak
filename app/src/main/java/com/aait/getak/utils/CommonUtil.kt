package com.aait.getak.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aait.getak.BuildConfig
import com.aait.getak.R
import com.aait.getak.app.AppController


import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.IOException
import java.net.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommonUtil {
    fun showParElevation(
        showHide: Boolean,
        app_bar: AppBarLayout,
        elevation: Float
    ) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            if (showHide) {
                app_bar.elevation = elevation
            } else {
                app_bar.elevation = 0.0.toFloat()
            }
        }
    }

    companion object {
        var isALog = true
        fun onPrintLog(o: Any?) {
            if (isALog) {
                Log.e("Response >>>>", Gson().toJson(o))
            }
        }

        fun PrintLogE(print: String?) {
            if (BuildConfig.DEBUG) {
                if (print != null) {
                    Log.e(AppController.TAG, print)
                }
            }
            if (print != null) {
                Log.e(AppController.TAG, print)
            }
        }

        fun hideKeyboardFrom(context: Context, view: View) {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun makeURL(
            sourceLat: Double,
            sourceLog: Double,
            destLat: Double,
            destLog: Double
        ): String {
            val urlString = StringBuilder()
            urlString.append("http://maps.googleapis.com/maps/api/directions/json")
            urlString.append("?origin=") // from
            urlString.append(java.lang.Double.toString(sourceLat))
            urlString.append(",")
            urlString.append(java.lang.Double.toString(sourceLog))
            urlString.append("&destination=") // to
            urlString.append(java.lang.Double.toString(destLat))
            urlString.append(",")
            urlString.append(java.lang.Double.toString(destLog))
            urlString.append("&sensor=false&mode=driving&alternatives=true")
            return urlString.toString()
        }

        val language: String
            get() = Locale.getDefault().displayLanguage

        @SuppressLint("MissingPermission")
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
            //        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        return networkInfo != null && networkInfo.isConnected();
        }

        fun requestFocus(view: View, window: Window) {
            if (view.requestFocus()) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }

        // make text view with single line and scrolling horizontal
        fun scrollTextInTextView(textView: TextView) {
            textView.ellipsize = TextUtils.TruncateAt.MARQUEE
            textView.setSingleLine(true)
            textView.marqueeRepeatLimit = 1
            textView.isSelected = true
        }

        // check he SDK version of the software currently running on this hardware device.
        fun canMakeSmores(): Boolean {
            return VERSION.SDK_INT > VERSION_CODES.LOLLIPOP_MR1
        }

        fun createDrawableFromView(mContext: Context , view: View): Bitmap? {
            val displayMetrics = DisplayMetrics()
            (mContext as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            view.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
            view.buildDrawingCache()
            val bitmap = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }

        fun getCurrentAddress(context: Context, addressLat: Double, addressLng: Double): String? {
            var myAddress: String? = null
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses =
                    geocoder.getFromLocation(addressLat, addressLng, 1)
                if (addresses.size > 0) {
                    myAddress = addresses[0].locality
//                    myAddress = addresses[0].getAddressLine(0)
//                    if (myAddress == null) {
//                        myAddress =
//                            addresses[0].adminArea + " - " + addresses[0].countryName
//                    }
                } else {
                    makeToast(
                        context,
                        context.getString(R.string.connection_error)
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return myAddress
        }

        fun canGetLocation(context: Context): Boolean {
            return isLocationEnabled(
                context
            ) // application context
        }

        fun replaceArabicNumbers(original: String): String {
            return original.replace("0", "٠").replace("1", "١").replace("2", "٢").replace("3", "٣")
                .replace("4", "٤").replace("5", "٥").replace("6", "٦")
                .replace("7", "٧").replace("8", "٨").replace("9", "٩")
        }

        fun parseDateFormatToDisplayForUser(myDate: String?): String? {
            val inputPattern = "dd-MM-yyyy"
            val outputPattern = "dd-MM-yyyy"
            val inputFormat =
                SimpleDateFormat(inputPattern, Locale.ENGLISH)
            val outputFormat =
                SimpleDateFormat(outputPattern, Locale("ar", "EG"))
            val date: Date
            var str: String? = null
            try {
                date = inputFormat.parse(myDate)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun parseDateFormatToSend(myDate: String?): String? {
            val inputPattern = "dd-MM-yyyy"
            val outputPattern = "dd-MM-yyyy"
            val inputFormat =
                SimpleDateFormat(outputPattern, Locale("ar", "EG"))
            val outputFormat =
                SimpleDateFormat(inputPattern, Locale.ENGLISH)
            val date: Date
            var str: String? = null
            try {
                date = inputFormat.parse(myDate)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun parseTimeFormatToDisplayForUser(time: String ,inputPattern: String ,outputPattern: String ): String? {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
            val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)
            val date: Date
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun parseTimeFormatToSend(time: String?): String? {
            val inputPattern = "hh:mm a"
            val outputPattern = "HH:mm a"
            //        SimpleDateFormat inputFormat = new SimpleDateFormat(outputPattern,new Locale("ar","EG"));
            val inputFormat =
                SimpleDateFormat(outputPattern, Locale.ENGLISH)
            val outputFormat =
                SimpleDateFormat(inputPattern, Locale.ENGLISH)
            val date: Date
            var str: String? = null
            try {
                date = inputFormat.parse(time)
                str = outputFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return str
        }

        fun isLocationEnabled(context: Context): Boolean {
            var locationMode = 0
            val locationProviders: String
            return if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(
                        context.contentResolver,
                        Settings.Secure.LOCATION_MODE
                    )
                } catch (e: SettingNotFoundException) {
                    e.printStackTrace()
                }
                locationMode != Settings.Secure.LOCATION_MODE_OFF
            } else {
                locationProviders = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.LOCATION_MODE
                )
                !TextUtils.isEmpty(locationProviders)
            }
        }

        fun ShareApp(context: Context) {
            val sendIntent = Intent()
            val appPackageName = context.packageName
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Hey check Takafol app at: https://play.google.com/store/apps/details?id=$appPackageName"
            )
            sendIntent.type = "text/plain"
            sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(sendIntent)
        }

        fun RateApp(context: AppCompatActivity) {
            val appPackageName =
                context.packageName // getPackageName() from Context or Activity object
            try {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        fun handleException(context: Context, t: Throwable?): Int {
            return if (t is SocketTimeoutException) {
                makeToast(
                    context,
                    R.string.time_out_error
                )
                R.string.time_out_error
            } else if (t is UnknownHostException) {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            } else if (t is ConnectException) {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            } else if (t is NoRouteToHostException) {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            } else if (t is PortUnreachableException) {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            } else if (t is UnknownServiceException) {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            } else if (t is BindException) {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            } else {
                makeToast(
                    context,
                    R.string.connection_error
                )
                R.string.connection_error
            }
        }

        fun makeToast(context: Context, msgId: Int) {
//            val toaster = Toaster(context)
//            toaster.makeToast(context.getString(msgId))
        }

        fun makeToast(context: Context?, msg: String?) {
//            val toaster = Toaster(context!!)
//            toaster.makeToast(msg)
        }

        fun showSnackBar(mContext: Context, coordinatorLayout: View, message: String, ColorRes: Int , bgColor : Int) {
            val snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
            snackbar.setActionTextColor(Color.RED)
            val sbView = snackbar.view
            sbView.setBackgroundColor(mContext.resources.getColor(bgColor))
            val textView = sbView.findViewById(R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(mContext, ColorRes))
            snackbar.show()
        }


        fun setConfig(language: String?, context: Context) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
        }

//        fun chooseDateFromCalender(context: Context?, textView: TextView) {
//            val c = Calendar.getInstance()
//            val year = c[Calendar.YEAR]
//            val month = c[Calendar.MONTH]
//            val day = c[Calendar.DAY_OF_MONTH]
//            val dlg: DatePickerDialog
//            dlg = DatePickerDialog(
//                context!!,
//                R.style.Calendar,
//                OnDateSetListener { view, year1, month1, dayOfMonth ->
//                    val cal = Calendar.getInstance()
//                    cal.timeInMillis = 0
//                    cal[year1, month1, dayOfMonth, 0, 0] = 0
//                    val chosenDate = cal.time
//                    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
//                    textView.text = sdf.format(chosenDate)
//                }, year, month, day
//            )
//            dlg.window!!.attributes.windowAnimations = R.style.DialogAnimation
//            dlg.show()
//        }

//        fun chooseTimeFromPicker(
//            context: Context?,
//            textView: TextView
//        ) {
//            val myCalender = Calendar.getInstance()
//            val hour = myCalender[Calendar.HOUR_OF_DAY]
//            val minute = myCalender[Calendar.MINUTE]
//            val tlg =
//                TimePickerDialog(
//                    context,
//                    R.style.TimePickerTheme,
//                    OnTimeSetListener { view, hourOfDay, minute1 ->
//                        val cal = Calendar.getInstance()
//                        cal[0, 0, 0, hourOfDay] = minute1
//                        val choseTime = cal.time
//                        val sdf =
//                            SimpleDateFormat("HH:mm a", Locale.ENGLISH)
//                        Log.e("<<<time", sdf.format(choseTime))
//                        textView.text = sdf.format(choseTime)
//                    },
//                    hour,
//                    minute,
//                    false
//                )
//            tlg.updateTime(hour, minute)
//            tlg.window!!.attributes.windowAnimations = R.style.DialogAnimation
//            tlg.show()
//        }

        fun getFormattedTime(date: String?): String {
            var parse: Date? = null
            val sdf =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            try {
                parse = sdf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val calendar = Calendar.getInstance()
            calendar.time = parse
            val updated = calendar.timeInMillis
            return DateUtils.getRelativeTimeSpanString(
                updated,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        }

        // Glide url
//    public static DrawableRequestBuilder<String> loadImage(Context context, String posterPath) {
//        return Glide
//                .with(context)
//                .load(posterPath)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//    }
        fun openWhatsAppContact(context: AppCompatActivity, number: String) {
            val uri =
                Uri.parse("https://api.whatsapp.com/send?phone=$number")
            val mWhatsAppIntent = Intent(Intent.ACTION_VIEW, uri)
            //        mWhatsAppIntent.setPackage("com.whatsapp");
            context.startActivity(mWhatsAppIntent)
        }

        fun goToWebsite(context: Context, link: String?) {
            val uri =
                Uri.parse(link) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        }

        fun getStakenumbers(context: Context): Int {
            val m = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningTaskInfoList =
                m.getRunningTasks(10)
            val itr: Iterator<RunningTaskInfo> = runningTaskInfoList.iterator()
            var numOfActivities = 0
            while (itr.hasNext()) {
                val runningTaskInfo = itr.next()
                val id = runningTaskInfo.id
                val desc = runningTaskInfo.description
                numOfActivities = runningTaskInfo.numActivities
                val topActivity = runningTaskInfo.topActivity
                    ?.shortClassName
                //            CommonUtil.PrintLogE("Activities number : " + numOfActivities + " Top Activies : " + topActivity);
                return numOfActivities
            }
            return numOfActivities
        }

        fun setStrokInText(textView: TextView) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        @RequiresApi(api = VERSION_CODES.KITKAT)
        fun getPathFromUri(context: Context, uri: Uri): String? {
            val isKitKat = VERSION.SDK_INT >= VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(
                    context,
                    uri
                )
            ) { // ExternalStorageProvider
                if (isExternalStorageDocument(
                        uri
                    )
                ) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(
                        uri
                    )
                ) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        null,
                        null
                    )
                } else if (isMediaDocument(
                        uri
                    )
                ) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        selection,
                        selectionArgs
                    )
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                return if (isGooglePhotosUri(
                        uri
                    )
                ) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver
                    .query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }
    }
}