package com.aait.getak.ui.activities.auth

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import com.aait.getak.R
import com.aait.getak.base.ParentActivity
import com.aait.getak.models.SigninResponse
import com.aait.getak.models.UploadImageResponse
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.network.remote_db.RetroWeb
import com.aait.getak.ui.activities.settings.ContactUsActivity
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.utils.CommonUtil
import com.aait.getak.utils.Validation
import com.aait.getak.utils.toasty
import kotlinx.android.synthetic.main.activity_login.*


import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.poup_done_msg.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel


class SignUpActivity : ParentActivity() {
    private val viewModel: RegisterViewModel by viewModel()

    var deviceId: String? = null
    var android_id: String? = null
    var type_image: String? = ""
    override val layoutResource: Int
        get() = R.layout.activity_signup
    val isEnableToolbar: Boolean
        get() = false

    //     val isFullScreen: Boolean
//        get() = false
    override val isEnableBack: Boolean
        get() = false

    override fun init() {
        initializeComponents()
    }

    fun hideInputType(): Boolean {
        return true
    }

    val isRecycle: Boolean
        get() = false


    var iamge_1_file: File? = null
    var iamge_2_file: File? = null
    var iamge_3_file: File? = null
    var iamge_4_file: File? = null
    var iamge_5_file: File? = null
    var iamge_6_file: File? = null
    var iamge_7_file: File? = null


    var iamge_1_file_name: String? = ""
    var iamge_2_file_name: String? = ""
    var iamge_3_file_name: String? = ""
    var iamge_4_file_name: String? = ""
    var iamge_5_file_name: String? = ""
    var iamge_6_file_name: String? = ""
    var iamge_7_file_name: String? = ""
    var type_req: String? = ""

    var id_of_area = "19";

    fun initializeComponents() {


        android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("android_id", android_id!!)


//        ccp.registerCarrierNumberEditText(txt_phone)
        /*butt_barBack.setOnClickListener {
            finish()
            MyAnimations.animateSlideDown(mContext)
        }*/
//        butt_sigup.setOnClickListener {
//            FirebaseMessaging.getInstance().token.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    deviceId = it.result
//                    validAndSignIn()
//                }
//            }
//        }

        image_1.setOnClickListener {
            type_image = "image_1"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }


        image_2.setOnClickListener {
            type_image = "image_2"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }


        image_3.setOnClickListener {
            type_image = "image_3"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }


        image_4.setOnClickListener {
            type_image = "image_4"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }


        image_5.setOnClickListener {
            type_image = "image_5"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }


        image_6.setOnClickListener {
            type_image = "image_6"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }

        image_7.setOnClickListener {
            type_image = "image_7"


            val builder = com.github.dhaval2404.imagepicker.ImagePicker.Builder(this@SignUpActivity)
            builder.crop() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .start()
        }


        step_1.visibility = View.VISIBLE
        step_2.visibility = View.GONE
        step_3.visibility = View.GONE


        NEXdt_1.setOnClickListener {

            CommonUtil.hideKeyboardFrom(mContext, reg_mobile)


            if (Validation.checkEditTextError(reg_name, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_mobile, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_id, getString(R.string.required)) ||
                Validation.checkEditTextlongnumber(reg_id, getString(R.string.requ_)) ||
                Validation.checkEditTextlongnumber(reg_mobile, getString(R.string.requ_)) ||
                Validation.checkEditTextError(reg_email, getString(R.string.required)) ||
                Validation.checkEditTextError_textBiew(
                    reg_date_of_birth,
                    getString(R.string.required)
                )

            ) {

//                ||
//                Validation.checkEditTextError(reg_code, getString(R.string.required)) ||
//                        Validation.checkEditTextError(reg_code2, getString(R.string.required))
            } else {
                step_1.visibility = View.GONE
                step_2.visibility = View.VISIBLE
                step_3.visibility = View.GONE
            }


        }

        NEXdt_2.setOnClickListener {

            if (Validation.checkFileError(iamge_1_file, txt_1, getString(R.string.required)) ||
                Validation.checkEditTextError_textBiew(
                    iamge_1_file_name,
                    txt_1,
                    getString(R.string.Newimage)
                ) ||
                Validation.checkEditTextError_textBiew(
                    iamge_2_file_name,
                    txt_2,
                    getString(R.string.Newimage)
                ) || Validation.checkEditTextError_textBiew(
                    iamge_3_file_name,
                    txt_3,
                    getString(R.string.Newimage)
                ) || Validation.checkEditTextError_textBiew(
                    iamge_4_file_name,
                    txt_4,
                    getString(R.string.Newimage)
                ) || Validation.checkEditTextError_textBiew(
                    iamge_5_file_name,
                    txt_5,
                    getString(R.string.Newimage)
                ) || Validation.checkEditTextError_textBiew(
                    iamge_6_file_name,
                    txt_6,
                    getString(R.string.Newimage)
                ) ||
                Validation.checkFileError(iamge_2_file, txt_2, getString(R.string.required)) ||
                Validation.checkFileError(iamge_3_file, txt_3, getString(R.string.required)) ||
                Validation.checkFileError(iamge_4_file, txt_4, getString(R.string.required)) ||
                Validation.checkFileError(iamge_5_file, txt_5, getString(R.string.required)) ||
                Validation.checkFileError(iamge_6_file, txt_6, getString(R.string.required))
            ) {


            } else {
                step_1.visibility = View.GONE
                step_2.visibility = View.GONE
                step_3.visibility = View.VISIBLE
            }


        }
        NEXdt_3.setOnClickListener {
            if (Validation.checkFileError(iamge_7_file, txt_7, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_type, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_modal, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_color, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_years, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_bord_number, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_alph_borde, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_number_bord, getString(R.string.required)) ||
                Validation.checkEditTextError(reg_car_type_bord, getString(R.string.required))

            ) {


            } else {

                send()
            }


        }

        back.setOnClickListener {


            if (step_1.visibility == View.VISIBLE) {
                finish()
            } else if (step_2.visibility == View.VISIBLE) {
                step_1.visibility = View.VISIBLE
                step_2.visibility = View.GONE
                step_3.visibility = View.GONE


            } else if (step_3.visibility == View.VISIBLE) {
                step_1.visibility = View.GONE
                step_2.visibility = View.VISIBLE
                step_3.visibility = View.GONE


            }


        }

        var myCalendar: Calendar = Calendar.getInstance();

        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)

                val myFormat = "MM-dd-yyyy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                reg_date_of_birth.setText(dateFormat.format(myCalendar.time))

            }
        reg_date_of_birth.setOnClickListener {
            DatePickerDialog(
                this@SignUpActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()


        }

        val spinnerArray = arrayOf(
            resources.getString(R.string.a1),
            resources.getString(R.string.a2),
            resources.getString(R.string.a3),
            resources.getString(R.string.a4),
            resources.getString(R.string.a5),
            resources.getString(R.string.a6)
        )

        val spinnerArray_ids = arrayOf(
            "19", "10", "23", "22", "25", "24"
        )


        val spinnerArrayAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            spinnerArray
        ) //selected item will look like a spinner set from XML

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        reg_area.setAdapter(spinnerArrayAdapter)


        reg_area.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                id_of_area = spinnerArray_ids[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        setStates()


        butt_forgetPassword.setOnClickListener {
//            startActivity(Intent(applicationContext, PhoneActivity::class.java))
        }

    }

//    private fun validAndSignup() {
////        CommonUtil.hideKeyboardFrom(mContext,txt_password)
////        if (Validation.checkEditTextError(reg_mobile, getString(R.string.required)) ||
////            Validation.checkEditTextError(reg_name, getString(R.string.required))
////        ) {
////            return
////        } else {
//
//
//
////        @Field("sequenceNumber") sequenceNumber: String?,
////        @Field("plateType") plateType: String?
//        if (CommonUtil.isNetworkAvailable(mContext)) {
//            showProgressDialog("")
//            RetroWeb.getClient.captainSignup(
//                mLanguagePrefManager.appLanguage!!,
//                reg_name.text.toString(),
//              "00966"+  reg_mobile.text.toString(),
//                reg_email.text.toString(),
//                reg_date_of_birth.text.toString(),
//                reg_id.text.toString(),
//                id_of_area,
//                reg_code.text.toString(),
//                reg_code2.text.toString(),
//                iamge_1_file_name,
//                iamge_2_file_name,
//                iamge_3_file_name,
//                iamge_4_file_name,
//                iamge_5_file_name,
//                iamge_6_file_name,
//                iamge_7_file_name,
//                reg_car_type.text.toString(),
//                reg_car_modal.text.toString(),
//                reg_car_color.text.toString(),
//                reg_car_years.text.toString(),
//                reg_car_bord_number.text.toString(),
//                reg_car_number_bord.text.toString(),
//                reg_car_alph_borde.text.toString(),
//                reg_car_type_bord.text.toString()
//            )
//                .enqueue(object : Callback<SigninResponse> {
//                    override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
//                        hideProgressDialog()
//                        CommonUtil.handleException(mContext, t)
//                        t.printStackTrace()
//                    }
//
//                    override fun onResponse(
//                        call: Call<SigninResponse>,
//                        response: Response<SigninResponse>
//                    ) {
//                        hideMyProgressBar()
//                        if (response.isSuccessful) {
//                            if (response.body()!!.isValue) {
//
//
//                                Done_Massage()
//
//
////                                mSharedPrefManager.deviceId = deviceId
////                                mSharedPrefManager.deviceUniqueId = android_id
////                                Log.e("DEVICEID2", deviceId!!)
////                                mSharedPrefManager.userAvailable =
////                                    response.body()!!.data!!.available
////                                mSharedPrefManager.userData = response.body()!!.data!!
////                                mSharedPrefManager.loginStatus = true
////                                startActivity(
////                                    Intent(
////                                        mContext,
////                                        Si::class.java
////                                    )
////                                )
////                                MyAnimations.animateSlideUp(mContext)
////                                finishAffinity()
//                            } else {
//                                CommonUtil.showSnackBar(
//                                    mContext,
//                                    main_layout,
//                                    response.body()!!.msg!!,
//                                    R.color.colorRed,
//                                    R.color.colorPrimary
//                                )
//                            }
//                        }
//                    }
//                })
//        } else {
//            CommonUtil.showSnackBar(
//                mContext,
//                main_layout,
//                getString(R.string.no_network_found_please_check_your_connection_and_try_again),
//                R.color.colorPrimary,
//                R.color.colorAccent
//            )
//        }
////        }
//    }
//
//    private fun upload_file(fileUri: Uri, file: File) {
////        CommonUtil.hideKeyboardFrom(mContext,txt_password)
////        if (Validation.checkEditTextError(reg_mobile, getString(R.string.required)) ||
////            Validation.checkEditTextError(reg_name, getString(R.string.required))
////        ) {
////            return
////        } else {
//        val requestFile = RequestBody.create(
//            "multipart/form-data".toMediaTypeOrNull(),
//            file
//        )
////        val requestFile: RequestBody = RequestBody.create(
////            contentResolver.getType(file.toUri())!!.toMediaTypeOrNull(),
////            file
////        )
//        val body: MultipartBody.Part =
//            MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//
//
//
//
//        if (CommonUtil.isNetworkAvailable(mContext)) {
//            showMyProgressBar()
//            RetroWeb.getClient.uploadImage(
//                mLanguagePrefManager.appLanguage!!,
//                body
//
//            ).enqueue(object : Callback<UploadImageResponse> {
//                override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
//                    hideMyProgressBar()
//                    CommonUtil.handleException(mContext, t)
//                    t.printStackTrace()
//                }
//
//                override fun onResponse(
//                    call: Call<UploadImageResponse>,
//                    response: Response<UploadImageResponse>
//                ) {
//                    hideMyProgressBar()
//                    if (response.isSuccessful) {
//                        if (response.body()!!.isValue) {
////                                    mSharedPrefManager.deviceId = deviceId
////                                    mSharedPrefManager.deviceUniqueId = android_id
////                                    Log.e("DEVICEID2", deviceId!!)
////                                    mSharedPrefManager.userAvailable =
////                                        response.body()!!.data!!.available
////                                    mSharedPrefManager.userData = response.body()!!.data!!
////                                    mSharedPrefManager.loginStatus = true
////                                    startActivity(
////                                        Intent(
////                                            mContext,
////                                            ChooseCarActivity::class.java
////                                        )
////                                    )
////                                    MyAnimations.animateSlideUp(mContext)
////                                    finishAffinity()
//
//
//                            if (type_image.toString().equals("image_1")) {
//                                iamge_1_file_name = response.body()?.data?.file.toString()
//
//
//                            } else if (type_image.toString().equals("image_2")) {
//                                iamge_2_file_name = response.body()?.data?.file.toString()
//
//                            } else if (type_image.toString().equals("image_3")) {
//                                iamge_3_file_name = response.body()?.data?.file.toString()
//
//                            } else if (type_image.toString().equals("image_4")) {
//                                iamge_4_file_name = response.body()?.data?.file.toString()
//
//                            } else if (type_image.toString().equals("image_5")) {
//                                iamge_5_file_name = response.body()?.data?.file.toString()
//
//                            } else if (type_image.toString().equals("image_6")) {
//                                iamge_6_file_name = response.body()?.data?.file.toString()
//
//                            } else if (type_image.toString().equals("image_7")) {
//                                iamge_7_file_name = response.body()?.data?.file.toString()
//
//                            }
//
//                        } else {
//                            CommonUtil.showSnackBar(
//                                mContext,
//                                main_layout,
//                                response.body()!!.msg!!,
//                                R.color.colorRed,
//                                R.color.colorPrimary
//                            )
//                        }
//                    }
//                }
//            })
//        } else {
//            CommonUtil.showSnackBar(
//                mContext,
//                main_layout,
//                getString(R.string.no_network_found_please_check_your_connection_and_try_again),
//                R.color.colorPrimary,
//                R.color.colorAccent
//            )
//        }
////        }
//    }

    override fun onBackPressed() {
        if (step_1.visibility == View.VISIBLE) {
            finish()
        } else if (step_2.visibility == View.VISIBLE) {
            step_1.visibility = View.VISIBLE
            step_2.visibility = View.GONE
            step_3.visibility = View.GONE


        } else if (step_3.visibility == View.VISIBLE) {
            step_1.visibility = View.GONE
            step_2.visibility = View.VISIBLE
            step_3.visibility = View.GONE
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (data != null) {
            val uri = data.data

            if (uri != null) {
                val file = File(uri.path)

                sendVerification(uri, file)
                if (type_image.toString().equals("image_1")) {
                    iamge_1_file = file

//                    val myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath())
//                    image_1.setImageBitmap(myBitmap);

                    image_1.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_1.setError(null)

                } else if (type_image.toString().equals("image_2")) {
                    iamge_2_file = file
                    image_2.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_2.setError(null)
                } else if (type_image.toString().equals("image_3")) {
                    iamge_3_file = file
                    image_3.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_3.setError(null)

                } else if (type_image.toString().equals("image_4")) {
                    iamge_4_file = file
                    image_4.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_4.setError(null)

                } else if (type_image.toString().equals("image_5")) {
                    iamge_5_file = file
                    image_5.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_5.setError(null)

                } else if (type_image.toString().equals("image_6")) {
                    iamge_6_file = file
                    image_6.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_6.setError(null)

                } else if (type_image.toString().equals("image_7")) {
                    iamge_7_file = file
                    image_7.setImageDrawable(
                        getResources()
                            .getDrawable(R.drawable.checkk)
                    )
                    txt_7.setError(null)

                }
//                SignUpFragment.get_image(uri);


            }


        }


        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun Done_Massage() {
        val logoutDialog = Dialog(mContext)
        logoutDialog.setContentView(R.layout.poup_done_msg)
        logoutDialog.setCancelable(true)
        logoutDialog.setCanceledOnTouchOutside(true)
        logoutDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        logoutDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        logoutDialog.show()
        logoutDialog.butt_no.setOnClickListener { logoutDialog.dismiss() }
        logoutDialog.butt_yes.setOnClickListener {
//            startActivity(
//                Intent(
//                    mContext,
//                    LoginActivity::class.java
//                )
//            )
//            MyAnimations.animateDiagonal(mContext)
            finish()


        }
    }

    fun sendVerification(fileUri: Uri, file: File) {
        type_req = "image"

        val requestFile = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            file
        )
//        val requestFile: RequestBody = RequestBody.create(
//            contentResolver.getType(file.toUri())!!.toMediaTypeOrNull(),
//            file
//        )
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)




        viewModel.SugnUpUpload(body)
    }

    fun send() {


        println("iamge_1_file_name" + iamge_1_file_name)
        println("iamge_2_file_name" + iamge_2_file_name)
        println("iamge_3_file_name" + iamge_3_file_name)
        println("iamge_4_file_name" + iamge_4_file_name)
        println("iamge_5_file_name" + iamge_5_file_name)
        println("iamge_6_file_name" + iamge_6_file_name)
        println("iamge_7_file_name" + iamge_7_file_name)



        type_req = "send"
        viewModel.SugnUp(
            "",
            reg_name.text.toString(),
            "00966" + reg_mobile.text.toString(),
            reg_email.text.toString(),
            reg_date_of_birth.text.toString(),
            reg_id.text.toString(),
            id_of_area,
            reg_code.text.toString(),
            reg_code2.text.toString(),
            iamge_1_file_name,
            iamge_2_file_name,
            iamge_3_file_name,
            iamge_4_file_name,
            iamge_5_file_name,
            iamge_6_file_name,
            iamge_7_file_name,
            reg_car_type.text.toString(),
            reg_car_modal.text.toString(),
            reg_car_color.text.toString(),
            reg_car_years.text.toString(),
            reg_car_bord_number.text.toString(),
            reg_car_number_bord.text.toString(),
            reg_car_alph_borde.text.toString(),
            reg_car_type_bord.text.toString()
        )

    }

    private fun setStates() {
        viewModel.states!!.observe(this, androidx.lifecycle.Observer {


//            when {

            if (it?.data != null) {
                hideProgressDialog()


                if (type_req.toString().equals("send")) {
                    Done_Massage()

                } else {
                    if(it!=null) {


                        val model = it.data as UploadImageResponse

                        if (type_image.toString().equals("image_1")) {
                            iamge_1_file_name = model?.data?.file.toString()

                            println("iamge_1_file_name" + iamge_1_file_name)
                        } else if (type_image.toString().equals("image_2")) {
                            iamge_2_file_name = model?.data?.file.toString()

                        } else if (type_image.toString().equals("image_3")) {
                            iamge_3_file_name = model?.data?.file.toString()

                        } else if (type_image.toString().equals("image_4")) {
                            iamge_4_file_name = model?.data?.file.toString()

                        } else if (type_image.toString().equals("image_5")) {
                            iamge_5_file_name = model?.data?.file.toString()

                        } else if (type_image.toString().equals("image_6")) {
                            iamge_6_file_name = model?.data?.file.toString()

                        } else if (type_image.toString().equals("image_7")) {
                            iamge_7_file_name = model?.data?.file.toString()

                        }
                    }
                }


            } else {
                println()

            }
//                it?.status == Resource.Status.ERROR -> {
//                    hideProgressDialog()
//                    toasty(it.message!!, 2)
//                }
//                it?.status == Resource.Status.LOADING_FIRST -> {
////                    showProgressDialog()
//                }
//            }


        })
    }


}
