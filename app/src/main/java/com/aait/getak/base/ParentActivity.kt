package com.aait.getak.base


import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cc.cloudist.acplibrary.ACProgressBaseDialog
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.aait.getak.R
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.network.repository.other_repos.RemoteRepos
import com.aait.getak.utils.DialogUtil
import com.aait.getak.utils.GlobalPreferences
import com.aait.getak.utils.Util
import com.aait.getak.utils.runOnMain
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.toolbar_normal.*
import org.jetbrains.anko.findOptional
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.ext.android.inject
import java.util.*

abstract class ParentActivity : AppCompatActivity() {
     val repo: RemoteRepos by inject()
    protected lateinit var mContext: Context

    private val compositeDisposable = CompositeDisposable()
    val isLoading = MutableLiveData<Boolean>()
    open var states: MutableLiveData<Resource<Any>>? = MutableLiveData()
    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)
    private var mDialog: Dialog? = null

    private fun dispose() = compositeDisposable.dispose()
    private var menuId: Int = 0
    internal var mPrefs: GlobalPreferences? = null
     abstract val layoutResource: Int

    internal open var isFullScreen: Boolean = false

     open val isEnableBack: Boolean = false
      var mProg: RelativeLayout?=null
     var savedInstanceState: Bundle? = null
     var progressDialog: ACProgressBaseDialog? = null
     var mEmpty: RelativeLayout?=null
     var mSwipe: SwipeRefreshLayout?=null
//     var mainLay: RelativeLayout?=null
     var mConn: RelativeLayout? = null


    private fun initViews() {
        mConn = findOptional(R.id.lay_no_internet)
        mEmpty= findOptional(R.id.lay_empty)
        mSwipe= findOptional(R.id.swipe)
//        mainLay=findOptional(R.id.main_lay)
        mProg= findOptional(R.id.lay_progress)

    }

    override fun onStop() {
        hideProgressDialog()
        super.onStop()
    }
    override fun onDestroy() {
        dispose()
        super.onDestroy()

    }

    protected fun show_progress() {

        /*if (mainLay != null) {
            mainLay?.setVisibility(View.VISIBLE)
        }*/
        mProg!!.visibility = View.VISIBLE
        mConn!!.visibility = View.GONE
        mEmpty!!.visibility = View.GONE
       // mSwipe!!.visibility = View.GONE
    }

    protected fun show_success_results() {
      /*  if (mainLay != null) {
            mainLay?.visibility = View.GONE
        }*/
        mProg!!.visibility = View.GONE
        mConn!!.visibility = View.GONE
       // mSwipe!!.visibility = View.GONE
        mEmpty!!.visibility = View.GONE
    }

    protected fun show_error() {
      /*  if (mainLay != null) {
            mainLay?.visibility = View.VISIBLE
        }*/
        mProg!!.visibility = View.GONE
        mConn!!.visibility = View.VISIBLE
      //  mSwipe!!.visibility = View.VISIBLE
        mEmpty!!.visibility = View.GONE
    }

    protected fun show_empty() {
       /* if (mainLay != null) {
            mainLay?.visibility = View.VISIBLE
        }*/
        mProg!!.visibility = View.GONE
        mConn!!.visibility = View.GONE
        //mSwipe!!.visibility = View.GONE
        mEmpty!!.visibility = View.VISIBLE

    }

    fun View.disable() {
        alpha=0.5f
        isEnabled=false
        isClickable = false
    }
    fun View.enable() {
        alpha=1.0f
        isEnabled=true
        isClickable = true
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        mContext = this


        mPrefs = GlobalPreferences(applicationContext)
        mPrefs!!.lang?.let { resources.setLanguage(it) }
        if (isFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        if (hideInputeType()) {
            hideInputtype()
        }
        // set layout resources
        setContentView(layoutResource)
        this.savedInstanceState = savedInstanceState
        initViews()


        init()

    }
    fun Resources.setLanguage(lang:String){
// Change locale settings in the app.
        val dm = this.displayMetrics
        val conf = this.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale( Locale(lang))
        } // API 17+ only.
        this.updateConfiguration(conf, dm)
    }

     fun setToolbar(title:String="") {
        iv_back.onClick {
            onBackPressed()
        }
        title_toolbar.text=title
    }


    protected fun has_dataBinding(): Boolean {
        return false
    }

    protected abstract fun init()


    protected  fun hideInputeType(): Boolean {return false}

    @SuppressLint("CheckResult")
    fun setPermissionsCamera(onCameraGranted:()-> Unit){
        val rxPermissions= RxPermissions(this)
        rxPermissions
            .request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

            )
            .subscribe { granted ->
                if (granted) {
                    onCameraGranted()
                }
                else {

                }
            }
    }

    @SuppressLint("CheckResult")
    fun setPermissionsPhone(onPhoneGranted:()-> Unit){
        val rxPermissions= RxPermissions(this)
        rxPermissions
            .request(
                Manifest.permission.CALL_PHONE

            )
            .subscribe { granted ->
                if (granted) {
                    onPhoneGranted()
                }
                else {

                }
            }
    }
    @SuppressLint("CheckResult")
    fun setPermissionsContact(onContactPicked:()-> Unit){
        val rxPermissions= RxPermissions(this)
        rxPermissions
            .request(
                Manifest.permission.READ_CONTACTS

            )
            .subscribe { granted ->
                if (granted) {
                    onContactPicked()
                }
                else {

                }
            }
    }

    @SuppressLint("CheckResult")
    fun setPermissionsLocation(onLocationGranted:(granted:Boolean)-> Unit){
        val rxPermissions= RxPermissions(this)
        rxPermissions
            .request(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .subscribe { granted ->
                    onLocationGranted(granted)
            }
    }

    fun createOptionsMenu(menuId: Int) {
        this.menuId = menuId
        invalidateOptionsMenu()
    }


    /**
     * function is used to create a menu
     **/
    fun removeOptionsMenu() {
        menuId = 0
        invalidateOptionsMenu()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuId != 0) {
            menuInflater.inflate(menuId, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }


    fun hideInputtype() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

    }

    // to override typekit in all activities we use
    override fun attachBaseContext(newBase: Context) {
        if (mPrefs == null) {
            mPrefs = GlobalPreferences(newBase)
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            //ContextWrapper wrap = CalligraphyContextWrapper.wrap(newBase)

            val context = Util.changeLang(mPrefs!!.lang!!, newBase)
            super.attachBaseContext(context)
            //super.attachBaseContext(Util.changeLang(new GlobalPreferences(newBase).getLang(),newBase));
        } else {
            Util.setConfig(mPrefs?.lang!!, newBase)
            super.attachBaseContext(newBase)
        }


    }


    protected fun <T> Single<T>.subscribeWithLoading(onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it}) : Disposable {
        return runOnMain{
            states!!.postValue(Resource.loading())
        }
            .doOnSubscribe { isLoading.value = true }
            .doOnSuccess {
                onSuccess(it) }
            .doOnError{ onError(it) }
            .doFinally{ isLoading.value = false}
            .subscribe()
    }
    protected fun <T> Observable<T>.subscribeWithLoading(onLoad: () -> Unit,onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it}, onComplete:()->Unit
    ) : Disposable {
        return runOnMain{
            states!!.postValue(Resource.loading())
        }
            .doOnSubscribe {
                onLoad()
            }

            .subscribe({
               // states!!.postValue(Resource.success(it as Any) )
                onSuccess(it)
            },{
                //states!!.postValue(Resource.success(it as Throwable) )
                onError(it)
            },{
                onComplete()
            }
        )
    }


    fun showProgressDialog(message: String?=null) {
        progressDialog = ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.GRAY)
            .fadeColor(Color.WHITE)
            .build()
        progressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.hide()
            progressDialog!!.dismiss()
        }
    }



    fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }
    internal fun onSwipe(onSwipeBack:()-> Unit){
        mSwipe?.setOnRefreshListener {
            mSwipe!!.isNestedScrollingEnabled=false
            mSwipe!!.isRefreshing=false
            onSwipeBack()

        }
    }



}


