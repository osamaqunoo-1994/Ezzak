package com.aait.getak.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.aait.getak.models.categories_model.Car
import com.aait.getak.ui.adapters.CustomSpinnerAdapter
import com.aait.getak.ui.dialogs.MyAlertDialog
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.runOnMain(runLoad:()->Unit): Single<T> = subscribeOn(Schedulers.io())
    .doOnSubscribe {
    runLoad()
}
    .observeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.runOnMain(runLoad:()->Unit): Observable<T> = subscribeOn(Schedulers.io())
    .doOnSubscribe {
        runLoad()
    }
    .observeOn(AndroidSchedulers.mainThread())

fun AppCompatAutoCompleteTextView.setMyAdapter(keys: List<*>, arrayAdapter: ArrayAdapter<*>,isHint:Boolean?=false):String
    {
    var selection=keys[0] as String
    isCursorVisible = false
    if (!isHint!!) {
        setText(selection)
    }

    setAdapter(arrayAdapter)
    onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        this@setMyAdapter.showDropDown()
        this@setMyAdapter.error = null
        selection = parent.getItemAtPosition(position) as String

    }
    setOnClickListener{
        this@setMyAdapter.showDropDown()
    }
    return selection

}
fun AppCompatAutoCompleteTextView.setCustomAdapter(keys: List<Car>, arrayAdapter: CustomSpinnerAdapter):Car?
{
    var car:Car?=null
    var selection=keys[0].name as String
    isCursorVisible = false
    error = null
    //setText(selection)
    setAdapter(arrayAdapter)
    onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        this@setCustomAdapter.showDropDown()
        //setText(keys[position].name)
        this@setCustomAdapter.error = null
         car = parent.getItemAtPosition(position) as Car

    }
    setOnClickListener{
        this@setCustomAdapter.showDropDown()
    }
    return car

}



fun checkLocation(context: Context):Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.isGpsEnabled():Boolean{
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

}

fun FragmentManager.showGpsAlert(ctx:Activity){
   /* val builder = AlertDialog.Builder(ctx)
    builder.setTitle(ctx.getString(R.string.alert))
    builder.setMessage(R.string.gps_msg)
    builder.setPositiveButton(R.string.ok) { dialogInterface, i -> ctx.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
    builder.setNegativeButton(R.string.cancel
    ) { dialogInterface, i -> dialogInterface.dismiss() }
    val alertDialog = builder.create()
    alertDialog.show()*/
    val dialog = MyAlertDialog{
        if (it==1){
            ctx.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),Constant.GPS)
        }
    }
    dialog.show(this,"logout")


}

fun Activity.hideKeyboard() {
    hideKeyboard((if (currentFocus == null) View(this) else currentFocus)!!)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.forwardMap(lat:String,lng:String) {
    /*https://www.google.com/maps/dir/?api=1&query=$lat,$lng*/
    //www.google.com/maps/search/?api=1&query=47.5951518,-122.3316393&query_place_id=ChIJKxjxuaNqkFQR3CK6O1HNNqY
    val url = "https://google.com/maps/search/?api=1&query=$lat,$lng"/*"https://maps.google.com//?daddr=$lat,$lng"*/
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

@SuppressLint("CheckResult")
fun Context.toasty(msg:String,choice:Int?=1) {
    if (choice == 1) {
        Toasty.success(this, msg).show()
    } else {
        Toasty.error(this, msg).show()
    }

fun RotateAnimation.rotation(img:ImageView,fromAngle:Float,toAngle:Float){
        RotateAnimation(fromAngle,toAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            .also {
                duration = 1000
                interpolator = LinearInterpolator() as Interpolator?
            }
    }


}
