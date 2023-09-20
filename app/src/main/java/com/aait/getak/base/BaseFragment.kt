package com.aait.getak.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import cc.cloudist.acplibrary.ACProgressBaseDialog

import com.aait.getak.R
import com.aait.getak.utils.GlobalPreferences

import com.aait.getak.listeners.OnSwipeFinish
/*import com.wang.avi.AVLoadingIndicatorView*/
import org.jetbrains.anko.findOptional
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.network.repository.other_repos.RemoteRepos
import com.aait.getak.utils.runOnMain
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject


abstract class BaseFragment : Fragment() {
    private var dialog: ProgressDialog? = null
    val repo: RemoteRepos by inject()
    private val compositeDisposable = CompositeDisposable()
    protected val isLoading = MutableLiveData<Boolean>()
    open var states: MutableLiveData<Resource<Any>>? = MutableLiveData()
    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    private fun dispose() = compositeDisposable.dispose()

    lateinit var mPrefs: GlobalPreferences
    var isMprefsInitialized=false
internal fun onSwipe(onSwipeFinish: OnSwipeFinish){
    mSwipe?.setOnRefreshListener {
        mSwipe!!.isNestedScrollingEnabled=false
        mSwipe!!.isRefreshing=false
        onSwipeFinish.onFinish()
    }
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
    protected fun <T> Observable<T>.subscribeWithLoadRec(onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it}) : Disposable {
        return runOnMain{
            //states!!.postValue(Resource.loading())
            hideProgressDialog()
        }
            .doOnSubscribe {
                isLoading.value = true
            }
            .doOnError{
                isLoading.value = false
                //onError(it)
            }
            .doOnTerminate {
                isLoading.value=false
            }
            .subscribe({
                onSuccess(it)
            },{
                onError(it)
            })
    }
    protected fun <T> Observable<T>.subscribeWithLoading(onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it}
    ) : Disposable {
        return runOnMain{
            states!!.postValue(Resource.loading())
        }
            .doOnSubscribe {
                showProgressDialog()
            }
            .doOnTerminate {
                hideProgressDialog()
            }
            .doOnError {
              //  hideProgressDialog()
                onError(it)
            }
            .subscribe({
                onSuccess(it)
            },{
                onError(it)
            }
            )

    }
    protected fun <T> Observable<T>.subscribeWithLoading(onLoad: () -> Unit,onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it}, onComplete:()->Unit
    ) : Disposable {
        return runOnMain {
            states!!.postValue(Resource.loading())
        }
            .doOnSubscribe {
                onLoad()
            }
            .subscribe({
                // states!!.postValue(Resource.success(it as Any) )
                onSuccess(it)
            }, {
                //states!!.postValue(Resource.success(it as Throwable) )
                onError(it)
            }, {
                onComplete()
            }
            )
    }
    override fun onStop() {
        hideProgressDialog()
        dispose()
        super.onStop()
    }

    internal abstract val viewId: Int
    internal fun show_progress() {
        if (mainLay != null) {
            mainLay!!.visibility = View.VISIBLE
        }
        mProg!!.visibility = View.VISIBLE
        mConn!!.visibility = View.GONE
        mEmpty!!.visibility = View.GONE
    }

    internal fun show_success_results() {
        if (mainLay != null) {
            mainLay!!.visibility = View.GONE
        }
        mProg!!.visibility = View.GONE
        mConn!!.visibility = View.GONE
        mEmpty!!.visibility = View.GONE
    }

    internal fun show_error() {
        if (mainLay != null) {
            mainLay!!.visibility = View.VISIBLE
        }
        mProg!!.visibility = View.GONE
        mConn!!.visibility = View.VISIBLE
        mEmpty!!.visibility = View.GONE
    }

    internal fun show_empty() {
        if (mainLay != null) {
            mainLay!!.visibility = View.VISIBLE
        }
        mProg!!.visibility = View.GONE
        mConn!!.visibility = View.GONE
        mEmpty!!.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(viewId, container, false)
        // globalPreferences = new GlobalPreferences(getActivity());
        this.savedInstanceState = savedInstanceState
        //toaster = new Toaster(getActivity());
        //customeProgressDialog = new CustomeProgressDialog(getActivity(), "");
        initViews(view)
        mPrefs = GlobalPreferences(context!!)
        isMprefsInitialized=::mPrefs.isInitialized
        init(view)
        //calligrapher = new Calligrapher(getActivity());
        //calligrapher.setFont(getActivity(),"fonts/cairo_regular.ttf",true);
        return view
    }



      var mProg: RelativeLayout?=null
      var savedInstanceState: Bundle? = null
    /*var progressDialog: AlertDialog? = null*/
    var progressDialog: ACProgressBaseDialog? = null
      var mEmpty: RelativeLayout?=null
      var mSwipe: SwipeRefreshLayout?=null
      var mainLay: RelativeLayout?=null
      var mConn: RelativeLayout?=null

    private fun initViews(view: View) {
       with(view){
           mConn= this.findOptional(R.id.lay_no_internet)
           mEmpty= this.findOptional(R.id.lay_empty)
           mSwipe= this.findOptional(R.id.swipe)
           mainLay= this.findOptional(R.id.main_lay)
           mProg= this.findOptional(R.id.lay_progress)
       }

    }
    internal fun onSwipe(onSwipeBack:()-> Unit){
        mSwipe?.setOnRefreshListener {
            mSwipe!!.isNestedScrollingEnabled=false
            mSwipe!!.isRefreshing=false
            onSwipeBack()

        }
    }

    internal abstract fun init(view: View)

    internal fun SetTitle():String {
        return ""
    }

    fun makeSnack(view: View, msg: Int) {
        var snackbar: Snackbar? = null
        val finalSnackbar = snackbar

        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE).setAction("تم") {
            //  finalSnackbar.dismiss();
        }
        snackbar.setActionTextColor(context!!.resources.getColor(R.color.colorPrimary))
        // Changing action button text color
        val sbView = snackbar.view
        val textView = sbView.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.YELLOW)
        snackbar.show()

    }


     fun showProgressDialog(message: String?=null) {
         progressDialog = ACProgressFlower.Builder(activity!!)
             .direction(ACProgressConstant.DIRECT_CLOCKWISE)
             .themeColor(Color.GRAY)
             .fadeColor(Color.WHITE)
             .build()
         progressDialog!!.show()


     }



    internal fun hideProgressDialog() {
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
    fun Activity.hideKeyboard() {
        if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}//public GlobalPreferences globalPreferences;