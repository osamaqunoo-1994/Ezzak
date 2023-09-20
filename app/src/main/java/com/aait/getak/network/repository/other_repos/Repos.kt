package com.aait.getak.network.repository.other_repos

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
import com.aait.getak.network.remote_db.RetroWeb
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.Header

interface Repos {
    fun updateUserLocale(
        token: String
    ): Observable<UserLocaleModel>

    fun getCountries(): Observable<CountriesModel>
    fun rideDetails(
        orderId: Int,
        token: String
    ): Observable<OrderDetailsModel>

    fun profile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        birth_date: String? = null,
        gender: String? = null,
        token: String? = null
    ): Observable<RegisterModel>

    fun srcDistTrips(
        from_lat: String,
        from_lng: String,
        to_lat: String,
        to_lng: String,
        car_type_id: String,
        token: String
    ): Observable<NearestTripModel>

    fun getNearestOrder(
        lat: String, lng: String, car_type_id: String, token: String
    ): Observable<NearestTripModel>

    fun getCars(
        token: String
    ): Observable<CarFilterModel>

    fun getRountes(src: String, dist: String, key: String): Observable<MyRouteModel>

    fun getCities(country_id: Int): Observable<CitiesModel>

    fun getUser(
        phone: String,
        country_iso: String, email: String,
        password: String, name: String,
        friend_code: String? = null,
        social_id: String? = null,
        auth: String,
        deviceId: String
    ): Observable<RegisterModel>

    fun verification(
        code: String,
        header: String
    ): Observable<RegisterModel>

    fun uploadImage(
        lang: String,
        file: MultipartBody.Part
    ): Observable<UploadImageResponse>

    fun captainSignup(
        lang: String,
        name: String,
        phone: String,
        email: String,
        birthdate: String,
        identity_number: String?,
        city_id: String?,
        friend_code: String?,
        marketing_code: String?,
        identity_card: String?,
        driving_license: String?,
        car_form: String?,
        iban: String?,
        car_insurance: String?,
        personal_image: String?,
        car_image: String?,
        car_type: String?,
        car_model: String?,
        car_color: String?,
        manufacturing_year: String?,
        car_numbers: String?,
        car_letters: String?,
        sequenceNumber: String?,
        plateType: String?
    ): Observable<SigninResponse>

    fun sigIn(
        phone: String,
        key: String,
        social_id: String? = null,
        deviceId: String
    ): Observable<RegisterModel>

    fun forgetPass(
        phone: String
    ): Observable<RegisterModel>

    fun resendCode(
        header: String
    ): Observable<RegisterModel>

    fun resetPass(
        pass: String,
        header: String
    ): Observable<RegisterModel>

    fun getAds(): Observable<AdsModel>
    fun blanced(header: String): Observable<BlanceModel>
    fun points(header: String): Observable<BlanceModel>
    fun years(): Observable<YearsModel>
    fun getPreviousTrips(/*year: String,*/header: String): Observable<PrevTripModel>
    fun deleteNotif(id: Int, token: String): Observable<ResetModel>
    fun getExpectedTime(
        from_lat: String,
        from_lng: String,
        car_type_id: Int,
        Authorization: String
    ): Observable<ExpectedTimeModel>

    fun getPlaces(
        from_lat: String,
        from_lng: String,
        Authorization: String
    ): Observable<WholePlacesModel>

    fun searchPlaces(
        lat: String,
        lng: String,
        name: String,
        page: String,
        Authorization: String
    ): Flowable<StoreModel>

    fun nearStores(
        lat: String,
        lng: String,
        page: String,
        Authorization: String
    ): Flowable<StoreModel>

    fun savePlace(
        lat: String,
        lng: String,
        name: String? = null,
        address: String,
        place_id: String? = null,
        Authorization: String
    ): Observable<ResetModel>

    fun getCarTypes(
        from_lat: String,
        from_lng: String,
        to_lat: String? = null,
        to_lng: String? = null,
        token: String
    ): Observable<CategoriesModel>

    fun getCarType(
        service_type: String,
        from_lat: String,
        rom_lng: String,
        to_lat: String,
        to_lng: String,
        service_in: String,
        token: String,
        orderId: String? = null,
        car_type_id: String? = null
    ): Observable<CategoriesModel>

    fun createTrip(
        expected_price: String?,
        tripTime: String,
        price_id: String,
        start_address: String,
        start_lat: String,
        start_long: String,
        end_address: String? = null,
        end_lat: String? = null,
        end_long: String? = null,
        car_type_id: String,
        payment_type: String? = "cash",
        type: String? = "now",
        later_order_date: String? = null,
        later_order_time: String? = null,
        coupon: String? = null,
        notes: String? = null,
        Authorization: String,
        gender: String,
        captain_type: String?,
        disturb: String
    ): Observable<OrderDetailsModel>

    fun changePass(
        oldPass: String,
        newPass: String,
        token: String
    ): Observable<ResetModel>

    fun laterOrders(
        token: String
    ): Observable<PrevTripModel>

    fun cancelOrder(
        order_id: String,
        reason_id: Int? = null,
        token: String
    ): Observable<ResetModel>

    fun updateClientOrder(
        order_id: String,
        later_order_date: String,
        later_order_time: String,
        notes: String? = "",
        token: String
    ): Observable<ResetModel>

    fun transferCharge(
        country_id: String,
        phone: String,
        transfer_amount: String,
        token: String
    ): Observable<ResetModel>

    fun getBalance(
        token: String
    ): Observable<BalanceModel>

    fun contactUs(
        token: String
    ): Observable<ContactUsModel>

    fun contactMSg(
        msg: String,
        token: String
    ): Observable<ResetModel>

    fun settings(
        deviceId: String,
        token: String
    ): Observable<DeviceModel>

    fun updateNotif(
        deviceId: String,
        isNotify: String,
        token: String
    ): Observable<ResetModel>

    fun invitation(
        token: String
    ): Observable<InvitaionModel>

    fun useBalance(
        use_balance_first: Boolean,
        token: String
    ): Observable<UseBlanceModel>

    fun addPromoCode(
        coupon: String,
        token: String
    ): Observable<DefaultModel>

    fun chargeCode(
        card: String,
        token: String
    ): Observable<ResetModel>

    fun replace_charge(
        token: String
    ): Observable<ResetModel>

    fun captin_details(
        order_id: Int,
        token: String
    ): Observable<CaptdeinModel>

    fun showBill(
        order_id: Int,
        token: String
    ): Observable<BillModel>

    fun rateUser(
        user_id: Int,
        rating: Float,
        comment: String? = null,
        block_captain: Boolean? = false,
        token: String
    ): Observable<ResetModel>

    fun bids(
        order_id: Int,
        token: String
    ): Observable<BidsModel>

    fun selectBid(
        bid_id: Int,
        token: String
    ): Observable<ResetModel>

    fun terms(): Observable<TermsModel>
    fun offers(token: String): Observable<NotificationModel>
    fun cancel(
        token: String
    ): Observable<CancelModel>

    fun laterOdrderDetails(
        order_id: Int,
        token: String
    ): Observable<LaterTripModel>

    fun notificationsNum(
        token: String
    ): Observable<NotificationNum>

    fun editProfile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        birth_date: String? = null,
        gender: String? = null,
        token: String? = null
    ): Observable<RegisterModel>

    fun checkUserSign(
        social_id: String,
        name: String?,
        mail: String?,
        deviceId: String
    ): Observable<RegisterModel>

    fun renotifyCaptain(
        user_id: Int,
        token: String
    ): Observable<OrderDetailsModel>

    fun getCurrentOrder(token: String): Observable<OrderDetailsModel>
    fun chat(conversation_id: String, token: String): Observable<ChatModel>
    fun expectedPrice(
        from_lat: String,
        from_lng: String,
        to_lat: String,
        to_lng: String,
        token: String
    ): Observable<com.aait.getak.models.expected_distance_model.ExpectedTimeModel>

    fun createOrder(
        place_id: String,
        place_ref: String,
        place_name: String,
        start_address: String,
        start_lat: String,
        start_lng: String,
        end_address: String,
        end_lat: String,
        end_lng: String,
        expected_distance: String,
        expected_period: String,
        expected_price: String,
        payment_type: String,
        desc: String,
        token: String
    ): Observable<OrderDetailsModel>

    fun saveCreditCard(
        user_id: Int,
        card_token: String,
        token: String
    ): Observable<RegisterModel>

    fun confirmPayment(
        paymentId: String,
        tranId: String,
        trackId: String,
        amount: String,
        cardBrand: String,
        token: String
    ): Observable<ResetModel>

    fun changePhone(
        phone: String,
        country_iso: String,
        token: String
    ): Observable<RegisterModel>

    fun activationPhone(
        code: String,
        phone: String,
        country_iso: String,
        token: String
    ): Observable<RegisterModel>

    fun storeDetails(
        store_id: String,
        from_lat: String? = null,
        from_lng: String? = null
    ): Observable<StoreDetailsModel>

    fun paymentWays(): Observable<PaymentWaysModel>

    fun hyperIndex(
        type: String, amount: String
    ): Observable<ResetModel>

    fun hyperResult(type: String, resourcePath: String): Observable<ResetModel>

    fun hyperIndex(
        orderId: String, type: String, amount: String
    ): Observable<ResetModel>

    fun hyperResult(orderId: String, type: String, resourcePath: String): Observable<ResetModel>
    fun uploadImage(file: MultipartBody.Part): Observable<UploadImageResponse>
}
