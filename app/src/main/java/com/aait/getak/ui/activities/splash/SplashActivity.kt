package com.aait.getak.ui.activities.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.MutableLiveData
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.cities_model.CityModel
import com.aait.getak.models.countries_model.Country
import com.aait.getak.models.register_model.UserModel
import com.aait.getak.ui.activities.auth.RegisterActivity
import com.aait.getak.ui.activities.auth.SignUpActivity
import com.aait.getak.ui.activities.core.MapPreviewActivity
import com.aait.getak.ui.activities.core.TrackingActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.GlobalPreferences
import com.aait.getak.utils.MyUtils
import com.aait.getak.utils.Util
import com.aait.getak.utils.toasty
import com.bumptech.glide.request.RequestOptions
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONException
import org.koin.android.viewmodel.ext.android.viewModel


class SplashActivity : ParentActivity() {
    private lateinit var mId: String
    private lateinit var mFirst_name: String
    private lateinit var mLast_name: String
    private lateinit var mEmail: String
    private val RC_SIGN_IN: Int=1111
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var gso: GoogleSignInOptions? = null
    private var callbackManager: CallbackManager? = null

    private val viewModel:RegisterViewModel by viewModel()


    companion object{
         var keys:List<Country> = ArrayList()
        var city:List<CityModel> = ArrayList()
        var id = MutableLiveData<Int>()
        var ride_accepted=false
        var dpHeight: Float=0f

    }
    override val layoutResource: Int
        get() = R.layout.activity_splash

    override var isFullScreen: Boolean = false
        get() = false
    override fun init() {
        //LoginManager.getInstance().logOut()

        MyUtils.setStatusBarTrans(window)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Util.changeLang(mPrefs!!.lang!!, applicationContext)
        } else {
            Util.setConfig(mPrefs!!.lang!!, this)
        }
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        // resources.displayMetrics.density
        dpHeight = outMetrics.heightPixels / density
        Log.e("dp", dpHeight.toString())
        continue_btn.onClick {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
        Reg_btn.onClick {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }

        if (mPrefs!!.isLogin){
            btns.visibility= View.GONE

            sendRequest()
            checkRideExist()
        }
        else{
            btns.visibility=View.VISIBLE
            setFaceBook()
            setGoogle()
        }
    }




    private fun checkRideExist() {
        repo.getCurrentOrder(mPrefs!!.token!!).subscribeWithLoading({
        },{
//            if (it.device_exists){
//            }
//            else{
//                GlobalPreferences(this).logout()
//                var intent = Intent(this, SplashActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//            }

            if (it.value=="1"){
                it.data?.has_captain?.let {status->
                    ride_accepted=status
                }
                //  id.postValue(it.data?.id)
                if (it.data?.id!! > 0){
                    checkNavigationRide(it.data?.id)
                }
                else{
                    goMap()
                }
            }
            else if (it.value=="0"){
                if (it.code==419) {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            }




        },{
            Log.e("error",it.message!!)
        },{
            Log.e("checked","true")
        })

    }

    private fun goMap() {
        val options = ActivityOptionsCompat.
        makeSceneTransitionAnimation(this, logo, "transition")
        val revealX =(logo.x + logo.width / 2).toInt()
        val revealY =  (logo.y + logo.height / 2).toInt()
        val intent =  Intent(this, MapPreviewActivity::class.java)
        intent.putExtra(MapPreviewActivity.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(MapPreviewActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        ActivityCompat.startActivity(this, intent, options.toBundle())
        finish()
    }

    private fun checkNavigationRide(order_Id:Int?=null) {
        val isAcceptedRide=ride_accepted
        goTracking(isAcceptedRide,order_Id)
    }



    private fun goTracking(isAcceptedRide:Boolean=false,order_Id: Int?) {
        val options = ActivityOptionsCompat.
            makeSceneTransitionAnimation(this, logo, "transition")
        val revealX =(logo.x + logo.width / 2).toInt()
        val revealY =  (logo.y + logo.height / 2).toInt()
        val intent =  Intent(this,TrackingActivity::class.java)
        intent.putExtra(MapPreviewActivity.EXTRA_CIRCULAR_REVEAL_X, revealX)
        intent.putExtra(MapPreviewActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY)
        intent.putExtra("order_id", order_Id)
        intent.putExtra("isAcceptedRide",isAcceptedRide)
        ActivityCompat.startActivity(this, intent, options.toBundle())
        finish()
    }
//    private lateinit var urwayPayment: UrwayPayment

    private fun confirmPayment() {
//        urwayPayment=UrwayPayment()
//        urwayPayment.makepaymentService(
//            "1", this, "1", "SAR", "", "trip", "3860", ""
//            , "", "", mPrefs?.user?.phone.toString() + "@getak.com", "", "", "", "", "SA",
//            "3860", "A", mPrefs?.user?.card_token + "", "0"
//        )

    }


    private fun setGoogle() {
         gso =  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestId()
        .requestProfile()
        .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso!!)
        logoutGoogle()
        sign_in_button.onClick {
            signIn()
        }
    }

    private fun logoutGoogle() {
        mGoogleSignInClient?.signOut()
    }

    private fun signIn() {
    val signInIntent = mGoogleSignInClient?.signInIntent
    startActivityForResult(signInIntent, RC_SIGN_IN)
}

    var tokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            Log.e("current_access","${currentAccessToken?.userId.toString()}")
            if (currentAccessToken!=null) {
                loadUserProfile(currentAccessToken)
            }
        }

    }




    private fun setFaceBook() {
        val   applicationSignature = FacebookSdk.getApplicationSignature(applicationContext)
        Log.e("sig",applicationSignature)
        checkLoginStatus()
         callbackManager = CallbackManager.Factory.create()
         face_btn.setPermissions(listOf("email","public_profile"))

         face_btn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
               override fun onSuccess(result: LoginResult?) {


               }

               override fun onCancel() {

               }

               override fun onError(error: FacebookException?) {
                   Log.e("error",error?.message!!)
               }
           })

    }

    private fun logout() {

        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                graphResponse:GraphResponse? -> LoginManager.getInstance().logOut()
        }
        ).executeAsync()

    }

    private fun loadUserProfile(newAccessToken: AccessToken) {
        val request =
            GraphRequest.newMeRequest(newAccessToken) { `object`, response ->
                try {

                     var first_name = `object`.getString("first_name")
                    var last_name = `object`.getString("last_name")
                    var email = `object`.getString("email")
                     var id = `object`.getString("id")
                    val image_url =
                        "https://graph.facebook.com/$id/picture?type=normal"
                    val requestOptions = RequestOptions()
                    requestOptions.dontAnimate()

                    Log.e("first_name",first_name+" "+id)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.parameters = parameters

        request.setCallback {
            if(it!=null && it.jsonObject!=null){
                var first_name = it.jsonObject.getString("first_name")
                var last_name = it.jsonObject.getString("last_name")
                var email = it.jsonObject.getString("email")
                var id = it.jsonObject.getString("id")

                FirebaseMessaging.getInstance().token.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val deviceId = it.result
                        sendRequestCheckUserStatus(id, "$first_name $last_name", email,deviceId)
                    }
                }
            }
        }



         request.executeAsync()



    }

    private fun checkLoginStatus() {
      if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken())
        }
    }


    private fun sendRequest() {
        addDisposable(repo.updateUserLocale(mPrefs!!.token!!).subscribeWithLoading({

        },{
            if (it.value=="1"){
                Log.e("done","done")
            }
            else{
                Log.e("error",it.msg!!)
            }
        },{
            Log.e("error",it.message!!)
        },{

        }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.ƒ
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }


    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        task?.addOnSuccessListener {
                Log.e("task","${task.result!!.displayName}"+",${task.result!!.id}")
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    val deviceId = it.result
                    sendRequestCheckUserStatus(
                        task.result!!.id.toString(),
                        task.result!!.displayName,
                        task.result!!.email,
                        deviceId
                    )
                }
            }
        }
        task?.addOnFailureListener {
            //toasty(it.message!!,2)
            Log.e("error",it.message.toString())
        }

    }

    private fun sendRequestCheckUserStatus(socialId:String,name:String?=null,email:String?=null,deviceId:String){
        //RegisterActivity.socialId=socialId
        repo.checkUserSign(socialId,name,email,deviceId).subscribeWithLoading({
//            showProgressDialog()
        },{
            if (it.value=="1"){
                it.data?.let {user->
//                    hideProgressDialog()
                    when{
                        user.registered_social!! && user.phone_registered!!->{
                            storeUser(user)
                            storeSession()
                            if (!user.card_token.isNullOrBlank()) mPrefs?.isPaidVisa=true
                            //LoginManager.getInstance().logOut()
                            goHome()
                        }
                        user.registered_social!! && !user.phone_registered!!->{
                            goRegister(socialId,name,email)
                        }
                        else->{
                            goRegister(socialId,name,email)
                        }

                    }

                }
            }
            else{
                toasty(it.msg!!,2)
            }
        },{
            hideProgressDialog()
            Log.e("errorService",it.message!!)
            Log.e("errorService",it.cause?.message!!)
        },{

        })
    }

    private fun goRegister(socialId: String, name: String?, email: String?) {
        RegisterActivity.socialId=socialId
        if (name != null) {
            RegisterActivity.name=name
        }
        if (email != null) {
            RegisterActivity.email=email
        }
        val intent=Intent(this,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun storeUser(user: UserModel){
        mPrefs?.storeUser(user)
        mPrefs?.token="Bearer ${user.token}"
    }
    private fun storeSession(){
        mPrefs?.storeSession(true)
    }
    private fun goHome() {
        val intent = Intent(this, MapPreviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    override fun onDestroy() {
        super.onDestroy()
        logout()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
