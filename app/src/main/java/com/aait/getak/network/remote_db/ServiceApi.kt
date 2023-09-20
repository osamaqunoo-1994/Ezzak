package com.aait.getak.network.remote_db

import com.aait.getak.models.*
import com.aait.getak.models.ads_model.AdsModel
import com.aait.getak.models.balance_model.BalanceModel
import com.aait.getak.models.bids_model.BidsModel
import com.aait.getak.models.bill_model.BillModel
import com.aait.getak.models.cancel_model.CancelModel
import com.aait.getak.models.captin_model.CaptdeinModel
import com.aait.getak.models.car_filter_model.CarFilterModel
import com.aait.getak.models.categories_model.CategoriesModel
import com.aait.getak.models.chat_model.ChatModel
import com.aait.getak.models.cities_model.CitiesModel
import com.aait.getak.models.countries_model.CountriesModel
import com.aait.getak.models.device_model.DeviceModel
import com.aait.getak.models.expecting_time_model.ExpectedTimeModel
import com.aait.getak.models.later_trip_model.LaterTripModel
import com.aait.getak.models.nearest_trip_model.NearestTripModel
import com.aait.getak.models.notification_model.NotificationModel
import com.aait.getak.models.notifications_api_model.SystemModel
import com.aait.getak.models.notifications_model.NotificationNum
import com.aait.getak.models.order_details_model.OrderDetailsModel
import com.aait.getak.models.payment.PaymentWaysModel
import com.aait.getak.models.prev_trip_model.PrevTripModel
import com.aait.getak.models.register_model.RegisterModel
import com.aait.getak.models.route_model.MyRouteModel
import com.aait.getak.models.store_details_model.StoreDetailsModel
import com.aait.getak.models.terms_model.TermsModel
import com.aait.getak.models.use_balance_model.UseBlanceModel
import com.aait.getak.models.user_locale_model.UserLocaleModel
import com.aait.getak.models.wholde_places_model.WholePlacesModel
import com.aait.getak.models.years_model.YearsModel
import com.aait.getak.utils.Util
//import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ServiceApi {

    @GET("countries")
    fun getCountries(
        @Header("lang") lang:String?=Util.language
    ): Observable<CountriesModel>

    @POST("getCountryCities")
    fun getCities(
        @Query("country_id") country_id:Int,
        @Header("lang") lang:String?= Util.language
    ): Observable<CitiesModel>

    @FormUrlEncoded
    @POST("clientSignUp")
    fun register(
        @Query("phone") phone:String,
        @Query("country_iso") country_iso:String,
        @Query("email") email:String?=null,
        @Field("password") password:String,
        @Query("name") name:String,
        @Query("friend_code") friend_code:String?=null,
        @Query("social_id") social_id:String?=null,
        @Query("device_id") device_id:String,
        @Query("device_type") device_type:String="android",
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ): Observable<RegisterModel>

    @POST("userSignIn")
    fun signIn(
        @Query("phone") phone:String,
        @Query("country_iso") country_iso:String,
        @Query("device_id") device_id:String?,
        @Query("social_id") social_id:String?,
        @Query("lang") lang:String?=Util.language,
        @Query("device_type") device_type:String?="android"
        ):Observable<RegisterModel>


    @Multipart
    @POST("upload_file")
    fun uploadImage(
        @Header("lang") lang: String,
        @Part file: MultipartBody.Part
    ): Observable<UploadImageResponse>
    @FormUrlEncoded
    @POST("captainSignup")
    fun captainSignup(
        @Header("lang") lang: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("birthdate") birthdate: String,
        @Field("identity_number") identity_number: String?,
        @Field("city_id") city_id: String?,
        @Field("friend_code") friend_code: String?,
        @Field("marketing_code") marketing_code: String?,
        @Field("identity_card") identity_card: String?,
        @Field("driving_license") driving_license: String?,
        @Field("car_form") car_form: String?,
        @Field("iban") iban: String?,
        @Field("car_insurance") car_insurance: String?,
        @Field("personal_image") personal_image: String?,
        @Field("car_image") car_image: String?,
        @Field("car_type") car_type: String?,
        @Field("car_model") car_model: String?,
        @Field("car_color") car_color: String?,
        @Field("manufacturing_year") manufacturing_year: String?,
        @Field("car_numbers") car_numbers: String?,
        @Field("car_letters") car_letters: String?,
        @Field("sequenceNumber") sequenceNumber: String?,
        @Field("plateType") plateType: String?
    ): Observable<SigninResponse>



    @POST("accountActivation")
    fun activation(
        @Query("code") code:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<RegisterModel>

    @POST("forgetPassword")
    fun forgetPass(
        @Query("phone") phone:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<RegisterModel>

    @POST("sendActivation")
    fun resendActivation(
        @Header("Authorization") Authorization:String
    ):Observable<RegisterModel>
    @POST("resetPassword")
    fun resetPassword(
        @Query("password") password:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<RegisterModel>

    @POST("clientHomeAds")
    fun getAds():Observable<AdsModel>

    @POST("YourBalance")
    fun balance(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<BlanceModel>

    @POST("myPoints")
    fun points(
    @Header("Authorization") Authorization:String,
    @Header("lang") lang:String?=Util.language
    ):Observable<BlanceModel>

    @POST("years")
    fun years(
    ):Observable<YearsModel>

    @POST("userArchive")
    fun getPreviousTrips(
        /*@Query("year") year:String,*/
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<PrevTripModel>

    @POST("get_notifications")
    fun notifications(
        @Query("page") page:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ): Flowable<Response<SystemModel>>

    @POST("deleteNotification")
    fun delete_notif(
        @Query("id") notification_id:Int,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ): Observable<ResetModel>
    @POST("num_notifications")
    fun notification_numbers(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<NotificationNum>
    @POST("expectedTimeNearestCar")
    fun getExpectedTime(
        @Query("from_lat") from_lat:String,
        @Query("from_long") from_lng:String,
        @Query("car_type_id") car_type_id:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ExpectedTimeModel>
    @POST("nearstores")
    fun nearstores(
        @Query("lat") from_lat:String,
        @Query("long") from_lng:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<WholePlacesModel>

    @POST("searchResturants")
    fun searchStores(
        @Query("lat") from_lat:String?=null,
        @Query("long") from_lng:String?=null,
        @Query("name") name:String,
        @Query("page") page:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Flowable<StoreModel>

    @POST("storeDetails")
    fun storeDetails(
        @Query("store_id") store_id:String,
        @Query("lat") from_lat:String?=null,
        @Query("long") from_lng:String?=null,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<StoreDetailsModel>

    @POST("savePlace")
    fun savePlace(
        @Query("lat") from_lat:String,
        @Query("long") from_lng:String,
        @Query("name") name:String?=null,
        @Query("address") address:String,
        @Query("place_id") place_id:String?=null,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @GET("maps/api/directions/json?sensor=true")
     fun getRouts(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key:String
    ): Observable<MyRouteModel>

    @POST("carTypes")
    fun carTypes(
        @Query("from_lat") from_lat:String,
        @Query("from_long") from_lng:String,
        @Query("to_lat") to_lat:String?=null,
        @Query("to_long") to_lng:String?=null,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language

    ):Observable<CategoriesModel>

    @POST("carType")
    fun carType(
        @Query("service_type") service_type:String,
        @Query("from_lat") from_lat:String,
        @Query("from_long") from_lng:String,
        @Query("to_lat") to_lat:String,
        @Query("to_long") to_lng:String,
        @Query("service_in") service_in:String,
        @Header("Authorization") Authorization:String,
        @Query("order_id") order_id:String?=null,
        @Query("car_type_id") car_type_id:String?=null,
        @Header("lang") lang:String?=Util.language

    ):Observable<CategoriesModel>

    @POST("UserCreateOrder")
    fun createTrip(
        @Query("expected_price") expected_price:String?,
        @Query("expected_period") tripTime:String,
        @Query("price_id") priceId:String,
        @Query("start_address") start_address:String,
        @Query("start_lat") start_lat:String,
        @Query("start_long") start_long:String,
        @Query("end_address") end_address:String?=null,
        @Query("end_lat") end_lat:String?=null,
        @Query("end_long") end_long:String?=null,
        @Query("car_type_id") car_type_id:String,
        @Query("type") type:String?="now",//now later
        @Query("later_order_date") later_order_date:String?=null,
        @Query("later_order_time") later_order_time:String?=null,
        @Query("coupon") coupon:String?=null,
        @Query("notes") notes:String?=null,
        @Query("payment_type") payment_type:String?,
        @Query("gender") gender:String?,
        @Query("captain_type") captain_type:String?,
        @Query("disturb") disturb:String?,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<OrderDetailsModel>

  /*  @Multipart
    @POST("ClientJoinOrder")
    fun joinTrip(
        @Query("order_id") order_id:String,
        @Query("start_address") start_address:String,
        @Query("start_lat") start_lat:String,
        @Query("start_long") start_long:String,
        @Query("end_address") end_address:String,
        @Query("end_lat") end_lat:String,
        @Query("end_long") end_long:String,
        @Query("car_type_id") car_type_id:String,
        @Query("expected_distance") expected_distance:String,
        @Query("expected_period") expected_period:String,
        @Query("expected_price") expected_price:String,
        @Query("payment_type") payment_type:String,
        @Query("num_persons") num_order_persons:String,
        @Query("cheaper_way") cheaper_way:String?=null,
        @Query("type") type:String,
        @Query("later_order_date") later_order_date:String?=null,
        @Query("later_order_time") later_order_time:String?=null,
        @Part shipment_image:MultipartBody.Part,
        @Query("identity_type") identity_type:String?=null,
        @Query("identity_number") identity_number:String?=null,
        @Query("coupon") coupon:String?=null,
        @Query("notes") notes:String?=null,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>*/

    @POST("ClientJoinOrder")
    fun joinTrip(
        @Query("order_id") order_id:String,
        @Query("start_address") start_address:String,
        @Query("start_lat") start_lat:String,
        @Query("start_long") start_long:String,
        @Query("end_address") end_address:String,
        @Query("end_lat") end_lat:String,
        @Query("end_long") end_long:String,
        @Query("car_type_id") car_type_id:String,
        @Query("expected_distance") expected_distance:String,
        @Query("expected_period") expected_period:String,
        @Query("expected_price") expected_price:String,
        @Query("payment_type") payment_type:String,
        @Query("num_persons") num_order_persons:String,
        @Query("cheaper_way") cheaper_way:String?=null,
        @Query("type") type:String,
        @Query("later_order_date") later_order_date:String?=null,
        @Query("later_order_time") later_order_time:String?=null,
        @Query("identity_type") identity_type:String?=null,
        @Query("identity_number") identity_number:String?=null,
        @Query("coupon") coupon:String?=null,
        @Query("notes") notes:String?=null,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>


//    @POST("UserCreateOrder")
//    fun createTrip(
//        @Query("price_id") price_id:String,
//        @Query("service_type") service_type:String,
//        @Query("service_in") service_in:String,
//        @Query("start_address") start_address:String,
//        @Query("start_lat") start_lat:String,
//        @Query("start_long") start_long:String,
//        @Query("end_address") end_address:String,
//        @Query("end_lat") end_lat:String,
//        @Query("end_long") end_long:String,
//        @Query("car_type_id") car_type_id:String,
//        @Query("expected_distance") expected_distance:String,
//        @Query("expected_period") expected_period:String,
//        @Query("expected_price") expected_price:String,
//        @Query("payment_type") payment_type:String,
//        @Query("num_order_persons") num_order_persons:String,
//        @Query("cheaper_way") cheaper_way:String?=null,
//        @Query("type") type:String,
//        @Query("later_order_date") later_order_date:String?=null,
//        @Query("later_order_time") later_order_time:String?=null,
//        @Query("identity_type") identity_type:String?=null,
//        @Query("identity_number") identity_number:String?=null,
//        @Query("coupon") coupon:String?=null,
//        @Query("notes") notes:String?=null,
//        @Header("Authorization") Authorization:String,
//        @Header("lang") lang:String?=Util.language
//    ):Observable<OrderDetailsModel>

    @POST("carTypesFilter")
    fun getCars(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<CarFilterModel>

    @POST("ClientNearOrders")
    fun nearTrips(
        @Query("lat") lat:String,
        @Query("long") lng:String,
        @Query("car_type_id") car_type_id:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<NearestTripModel>

    @POST("ClientRemoteDistanationOrders")
    fun srcDistTrips(
        @Query("yourlat") from_lat:String,
        @Query("yourlong") from_lng:String,
        @Query("remotelat") to_lat:String,
        @Query("remotelong") to_lng:String,
        @Query("car_type_id") car_type_id:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<NearestTripModel>

  /*  @Multipart
    @POST("editProfile")
    fun editProfile(
        @Query("name") first_name:String?=null,
        @Query("email") email: String?=null,
        @Query("phone") phone: String?=null,
        @Part avatar:MultipartBody.Part,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<RegisterModel>*/
    @POST("editProfile")
    fun editProfile(
        @Query("name") first_name:String?=null,
        @Query("email") email: String?=null,
        @Query("phone") phone: String?=null,
        @Query("birth_date") birth_date: String?=null,
        @Query("gender") gender: String?=null,
        @Header("Authorization") Authorization:String?=null,
        @Header("lang") lang:String?=Util.language
    ):Observable<RegisterModel>



    @FormUrlEncoded
    @POST("changePassword")
    fun changePassword(
        @Field("current_password") oldPass:String,
        @Field("password") newPass:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("ClientLaterOrders")
    fun laterOrders(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<PrevTripModel>

    @POST("ClientLaterOrder")
    fun laterOrderDetails(
        @Query("order_id") order_id:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<LaterTripModel>

    @POST("ClientCancelOrder")
    fun cancelOrder(
        @Query("order_id") order_id:String,
        @Query("reason_id") reason_id:Int?=null,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("UpdateClientLaterOrder")
    fun updateLaterRide(
        @Query("order_id") order_id:String,
        @Query("later_order_date") later_order_date:String,
        @Query("later_order_time") later_order_time:String,
        @Query("notes") notes:String?="",
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>


    @POST("transferMoney")
    fun transferMoney(
        @Query("country_id") country_id:String,
        @Query("phone") phone:String,
        @Query("transfer_amount") transfer_amount:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("YourBalance")
    fun transferBalance(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<BalanceModel>

    @POST("contactways")
    fun contactUs(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ContactUsModel>

    @POST("createContact")
    fun sendMsg(
        @Query("message") msg:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("deviceData")
    fun settings(
        @Query("device_id") device_id:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<DeviceModel>

    @POST("updateDeviceData")
    fun updateNotif(
        @Query("device_id") device_id:String,
        @Query("show_ads") show_ads:String?="false",
        @Query("orders_notify") orders_notify:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("inviteClientBalance")
    fun invitationData(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<InvitaionModel>

    @POST("useBalanceFirst")
    fun useBalance(
        @Query("use_balance_first") use_balance_first:Boolean,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<UseBlanceModel>

    @POST("addCoupon")
    fun addPromoCode(
        @Query("coupon") coupon:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<DefaultModel>

    @POST("addChargeCard")
    fun chargeCard(
        @Query("code") coupon:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("clientFinishedOrderDetails")
    fun orderDetials(
        @Query("order_id") order_id:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<OrderDetailsModel>

    @POST("replacePoints")
    fun replacePoints(
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("ClientViewAcceptedOrderCaptain")
    fun captinDetails(
        @Query("order_id") order_id:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<CaptdeinModel>

    @POST("ClientShowTotalOrderPrice")
    fun showBill(
        @Query("order_id") order_id:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<BillModel>

    @POST("ratingUser")
    fun rateUser(
        @Query("user_id") user_id:Int,
        @Query("rating") rating:Float,
        @Query("comment") comment:String?=null,
        @Query("block_captain") block_captain:Boolean?=false,
        @Header("Authorization") Authorization:String,
        @Query("is_client") isClient:Boolean?=true,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("ClientViewOrderBids")
    fun bids(
        @Query("order_id") order_id:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<BidsModel>

    @POST("ClientAgreeOrderBid")
    fun selectBid(
        @Query("bid_id") bid_id:Int,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<ResetModel>

    @POST("terms")
    fun terms(
      //  @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ):Observable<TermsModel>

    @POST("logout")
    fun logOut(
        @Header("Authorization") token: String,
        @Query("device_id") device_id:String?,
        @Header("lang") lang: String?=Util.language
    ):Call<ResetModel>

    @POST("updateUserlocale")
    fun updateUserlocale(
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<UserLocaleModel>

    @POST("cancelReasons")
    fun reasons(
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<CancelModel>

    @POST("offers")
    fun offers(
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<NotificationModel>

    @POST("checkUserSignInSocial")
    fun checkUserSignIn(
        @Query("social_id") social_id: String,
        @Query("name") name: String?=null,
        @Query("email") email: String?=null,
        @Query("device_id") device_id: String?,
        @Query("device_type") device_type: String?="android"
    ):Observable<RegisterModel>

    @POST("reNotifyCaptains")
    fun renotifyCaptain(
        @Query("order_id") order_id: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<OrderDetailsModel>

   @POST("clientCurrentOrder")
    fun getCurrentOrder(
       @Header("Authorization") token: String,
       @Header("lang") lang: String?=Util.language
   ):Observable<OrderDetailsModel>

   @POST("conversation")
    fun chat(
       @Query("conversation_id") conversation_id : String,
       @Header("Authorization") token: String,
       @Header("lang") lang: String?=Util.language
   ):Observable<ChatModel>


   @POST("getExpectedDistancePriceTime")
    fun getExpectedDistancePriceTime(
       @Query("from_lat") from_lat : String,
       @Query("from_long") from_lng : String,
       @Query("to_lat") to_lat : String,
       @Query("to_long") to_lng : String,
       @Header("Authorization") token: String,
       @Header("lang") lang: String?=Util.language
   ):Observable<com.aait.getak.models.expected_distance_model.ExpectedTimeModel>


    @POST("nearResturants")
    fun nearResturants(
        @Query("lat") from_lat:String,
        @Query("long") from_lng:String,
        @Query("page") page:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ): Flowable<StoreModel>

    @POST("UserCreateFoodOrder")
    fun createOrderFood(
        @Query("place_id") place_id:String,
        @Query("place_ref") place_ref:String,
        @Query("place_name") place_name:String,
        @Query("start_address") start_address:String,
        @Query("start_lat") start_lat:String,
        @Query("start_long") start_lng:String,
        @Query("end_address") end_address:String,
        @Query("end_lat") end_lat:String,
        @Query("end_long") end_lng:String,
        @Query("expected_distance") expected_distance:String,
        @Query("expected_period") expected_period:String,
        @Query("expected_price") expected_price:String,
        @Query("payment_type") payment_type:String,
        @Query("notes") desc:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ): Observable<OrderDetailsModel>

    @POST("saveCreditCard")
    fun saveCreditCard(
        @Query("user_id") user_id: Int,
        @Query("card_token") card_token:String,
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ): Observable<RegisterModel>

    @POST("payment_status")
    fun confirmPayment(
        @Query("PaymentId") PaymentId: String,
        @Query("TranId") TranId:String,
        @Query("TrackId") TrackId:String,
        @Query("amount") amount:String,
        @Query("UserField1") UserField1:String?="trip",
        @Query("cardBrand") cardBrand:String?="STCPAY",
        @Header("Authorization") Authorization:String,
        @Header("lang") lang:String?=Util.language
    ): Observable<ResetModel>


    @POST("changePhone")
    fun changePhone(
        @Query("phone") phone: String,
        @Query("country_iso") country_iso: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<RegisterModel>

    @POST("newPhoneActivation")
    fun activationPhone(
        @Query("code") code: String,
        @Query("phone") phone: String,
        @Query("country_iso") country_iso: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<RegisterModel>

    @POST("payment-ways")
    fun paymentWays(
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<PaymentWaysModel>

    @POST("hyper-index")
    fun hyperIndex(
        @Query("type") type: String,
        @Query("amount") amount: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<ResetModel>

    @POST("hyper-result")
    fun hyperResult(
        @Query("type") type: String,
        @Query("resourcePath") resourcePath: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<ResetModel>
    @POST("hyper-index")
    fun hyperIndex(
        @Query("order_id")orderId:String,
        @Query("type") type: String,
        @Query("amount") amount: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<ResetModel>

    @POST("hyper-result")
    fun hyperResult(
        @Query("order_id")orderId:String,
        @Query("type") type: String,
        @Query("resourcePath") resourcePath: String,
        @Header("Authorization") token: String,
        @Header("lang") lang: String?=Util.language
    ):Observable<ResetModel>

}