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
import com.aait.getak.utils.GlobalPreferences
import com.aait.getak.utils.Util
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MultipartBody

class RemoteRepos(val prefs:GlobalPreferences) : Repos {
    override fun updateUserLocale(token: String): Observable<UserLocaleModel> {
        return RetroWeb.serviceApi.updateUserlocale(token)
    }

    override fun terms(/*token: String*/): Observable<TermsModel> {
        return RetroWeb.serviceApi.terms(/*token*/)
    }

    override fun offers(token: String): Observable<NotificationModel> {
        return RetroWeb.serviceApi.offers(token)
    }

    override fun cancel(token: String): Observable<CancelModel> {
        return RetroWeb.serviceApi.reasons(token)
    }

    override fun laterOdrderDetails(order_id: Int, token: String): Observable<LaterTripModel> {
        return RetroWeb.serviceApi.laterOrderDetails(order_id.toString(),token)
    }

    override fun notificationsNum(token: String): Observable<NotificationNum> {
        return RetroWeb.serviceApi.notification_numbers(token)
    }

    override fun editProfile(name: String?, mail:String?, phone: String?,birh_date:String?,gender:String?,token: String?): Observable<RegisterModel> {

        return   RetroWeb.serviceApi.editProfile(
            name?:prefs.user?.firstName,
            mail?:prefs.user?.email,
        phone?:prefs.user?.phone,
            birh_date?:prefs.user?.birth_date,
            gender?:prefs.user?.gender,
            token?:prefs.token
        )
    }

    override fun checkUserSign(social_id: String,name:String?,mail:String?,deviceId: String): Observable<RegisterModel> {
        return RetroWeb.serviceApi.checkUserSignIn(social_id,name,mail, device_id = deviceId)
    }

    override fun renotifyCaptain(order_id: Int, token: String): Observable<OrderDetailsModel> {
        return RetroWeb.serviceApi.renotifyCaptain(order_id =order_id.toString(),token = token)
    }

    override fun getCurrentOrder(token: String): Observable<OrderDetailsModel> {
        return RetroWeb.serviceApi.getCurrentOrder(token)
    }

    override fun chat(conversation_id: String, token: String): Observable<ChatModel> {
        return RetroWeb.serviceApi.chat(conversation_id,token)
    }

    override fun expectedPrice(
        from_lat: String,
        from_lng: String,
        to_lat: String,
        to_lng: String,
        token: String
    ): Observable<com.aait.getak.models.expected_distance_model.ExpectedTimeModel> {
        return RetroWeb.serviceApi.getExpectedDistancePriceTime(from_lat, from_lng, to_lat, to_lng, token)
    }

    override fun createOrder(
        place_id: String,
        place_ref: String,
        place_name: String,
        start_address: String,
        start_lat: String,
        start_lng: String,
        end_address:String,
        end_lat: String,
        end_lng: String,
        expected_distance: String,
        expected_period: String,
        expected_price: String,
        payment_type: String,
        desc: String,
        token: String
    ): Observable<OrderDetailsModel> {
        return RetroWeb.serviceApi.createOrderFood(place_id,place_ref,place_name,
            start_address,start_lat,start_lng,end_address,end_lat,end_lng,
           expected_distance,expected_period,expected_price,payment_type,desc,token
            )

    }

    override fun saveCreditCard(
        user_id: Int,
        card_token: String,
        token: String
    ): Observable<RegisterModel> {
        return RetroWeb.serviceApi.saveCreditCard(user_id,card_token,token)
    }

    override fun confirmPayment(
        paymentId: String,
        tranId: String,
        trackId: String,
        amount: String,
        cardBrand: String,
        token: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.confirmPayment(PaymentId = paymentId,TranId = tranId,TrackId = trackId,amount = amount,cardBrand = cardBrand,Authorization = token)
    }


    override fun selectBid(bid_id: Int, token: String): Observable<ResetModel> {
        return RetroWeb.serviceApi.selectBid(bid_id,token)
    }

    override fun bids(order_id: Int, token: String): Observable<BidsModel> {
        return RetroWeb.serviceApi.bids(order_id,token)
    }

    override fun rateUser(
        user_id: Int,
        rating: Float,
        comment: String?,
        block_captain: Boolean?,
        token: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.rateUser(user_id,rating,comment,block_captain,token)
    }

    override fun showBill(order_id: Int, token: String): Observable<BillModel> {
        return RetroWeb.serviceApi.showBill(order_id,token)
    }

    override fun captin_details(order_id: Int, token: String): Observable<CaptdeinModel> {
        return RetroWeb.serviceApi.captinDetails(order_id,token)
    }

    override fun replace_charge(token: String): Observable<ResetModel> {
        return RetroWeb.serviceApi.replacePoints(token)
    }

    override fun rideDetails(orderId: Int, token: String): Observable<OrderDetailsModel> {
        return RetroWeb.serviceApi.orderDetials(orderId,token)
    }

    override fun chargeCode(card: String, token: String): Observable<ResetModel> {
        return RetroWeb.serviceApi.chargeCard(card,token)
    }

    override fun addPromoCode(coupon: String, token: String): Observable<DefaultModel> {
        return RetroWeb.serviceApi.addPromoCode(coupon,token)
    }

    override fun useBalance(isChecked:Boolean,token: String): Observable<UseBlanceModel> {
        return RetroWeb.serviceApi.useBalance(isChecked,token)
    }

    override fun invitation(token: String): Observable<InvitaionModel> {
        return RetroWeb.serviceApi.invitationData(token)
    }

    override fun updateNotif(deviceId: String,isNotify: String, token: String): Observable<ResetModel> {
        return RetroWeb.serviceApi.updateNotif(device_id = deviceId,orders_notify = isNotify,Authorization = token)
    }

    override fun settings(deviceId: String,token: String): Observable<DeviceModel> {
        return RetroWeb.serviceApi.settings(device_id = deviceId,Authorization = token)
    }

    override fun contactMSg(msg: String, token: String): Observable<ResetModel> {
        return RetroWeb.serviceApi.sendMsg(msg,token)
    }

    override fun contactUs(token: String): Observable<ContactUsModel> {
        return RetroWeb.serviceApi.contactUs(token)
    }

    override fun getBalance(token: String): Observable<BalanceModel> {
        return RetroWeb.serviceApi.transferBalance(token)
    }

    override fun transferCharge(
        country_id: String,
        phone: String,
        transfer_amount: String,
        token: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.transferMoney(country_id,phone,transfer_amount,token)
    }

    override fun updateClientOrder(
        order_id: String,
        later_order_date: String,
        later_order_time: String,
        notes: String?,
        token: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.updateLaterRide(order_id,later_order_date,later_order_time,notes,token)
    }

    override fun cancelOrder(
        order_id: String,
        reason_id: Int?,
        token: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.cancelOrder(order_id,reason_id,token)
    }

    override fun laterOrders(token: String): Observable<PrevTripModel> {
        return RetroWeb.serviceApi.laterOrders(token)
    }

    override fun changePass(
        oldPass: String,
        newPass: String,
        token: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.changePassword(oldPass,newPass,token)
    }



    override fun profile(
        name: String?, mail:String?, phone: String?,birh_date:String?,gender:String?,token: String?
    ): Observable<RegisterModel> {
        return RetroWeb.serviceApi.editProfile(
            name?:prefs.user?.firstName,
            mail?:prefs.user?.email,
            phone?:prefs.user?.phone,
            birh_date?:prefs.user?.birth_date,
            gender?:prefs.user?.gender,
            token?:prefs.token)
    }
    override fun srcDistTrips(
        from_lat: String,
        from_lng: String,
        to_lat: String,
        to_lng: String,
        car_type_id: String,
        token: String
    ): Observable<NearestTripModel> {
        return RetroWeb.serviceApi.srcDistTrips(from_lat,from_lng,to_lat,to_lng,car_type_id,token)
    }

    override fun getNearestOrder(
        lat: String,
        lng: String,
        car_type_id: String,
        token: String
    ): Observable<NearestTripModel> {
        return RetroWeb.serviceApi.nearTrips(lat,lng,car_type_id,token)
    }

    override fun getCars(token: String): Observable<CarFilterModel> {
        return RetroWeb.serviceApi.getCars(token)
    }

    /*override fun createTrip(
        price_id: String,
        service_type: String,
        service_in: String,
        start_address: String,
        start_lat: String,
        start_long: String,
        end_address: String,
        end_lat: String,
        end_long: String,
        car_type_id: String,
        expected_distance: String,
        expected_period: String,
        expected_price: String,
        payment_type: String,
        num_order_persons: String,
        cheaper_way: String?,
        type: String,
        later_order_date: String?,
        later_order_time: String?,
        identity_type: String?,
        identity_number: String?,
        coupon: String?,
        notes: String?,
        Authorization: String
    ): Observable<OrderDetailsModel> {
        return RetroWeb.serviceApi.createTrip(price_id, service_type, service_in, start_address, start_lat, start_long, end_address, end_lat, end_long, car_type_id, expected_distance, expected_period, expected_price, payment_type, num_order_persons, cheaper_way, type, later_order_date, later_order_time, identity_type, identity_number, coupon, notes, Authorization)
    }
*/

    /*override fun joinTrip(
        order_id: String,
        service_type: String,
        service_in: String,
        start_address: String,
        start_lat: String,
        start_long: String,
        end_address: String,
        end_lat: String,
        end_long: String,
        car_type_id: String,
        expected_distance: String,
        expected_period: String,
        expected_price: String,
        payment_type: String,
        num_order_persons: String,
        cheaper_way: String?,
        type: String,
        later_order_date: String?,
        later_order_time: String?,
        shipment_image: MultipartBody.Part,
        identity_type: String?,
        identity_number: String?,
        coupon: String?,
        notes: String?,
        Authorization: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.joinTrip(order_id, start_address, start_lat, start_long, end_address, end_lat, end_long, car_type_id, expected_distance, expected_period, expected_price, payment_type, num_order_persons, cheaper_way, type, later_order_date, later_order_time,shipment_image,identity_type, identity_number, coupon, notes, Authorization)
    }
*/
    /*override fun joinTrip(
        order_id: String,
        service_type: String,
        service_in: String,
        start_address: String,
        start_lat: String,
        start_long: String,
        end_address: String,
        end_lat: String,
        end_long: String,
        car_type_id: String,
        expected_distance: String,
        expected_period: String,
        expected_price: String,
        payment_type: String,
        num_order_persons: String,
        cheaper_way: String?,
        type: String,
        later_order_date: String?,
        later_order_time: String?,
        identity_type: String?,
        identity_number: String?,
        coupon: String?,
        notes: String?,
        Authorization: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.joinTrip(order_id , start_address, start_lat, start_long, end_address, end_lat, end_long, car_type_id, expected_distance, expected_period, expected_price, payment_type, num_order_persons, cheaper_way, type, later_order_date, later_order_time, identity_type, identity_number, coupon, notes, Authorization)
    }*/
    override fun getCarTypes(
        from_lat: String,
        from_lng: String,
        to_lat: String?,
        to_lng: String?,
        token: String
    ) : Observable<CategoriesModel>{
        return RetroWeb.serviceApi.carTypes(
            from_lat,
            from_lng,
            to_lat,
            to_lng,
            token
        )
    }


    override fun getCarType(
        service_type: String,
        from_lat: String,
        from_lng: String,
        to_lat: String,
        to_lng: String,
        service_in: String,
        token: String,
        orderId: String?,
        car_type_id: String?
    ): Observable<CategoriesModel> {
        return RetroWeb.serviceApi.carType(
            service_type,
            from_lat,
            from_lng,
            to_lat,
            to_lng,
            service_in,
            token,
            orderId,
            car_type_id
            )
    }

    override fun createTrip(
        expected_price:String?,
        tripTime:String,
        priceId:String,
        start_address: String,
        start_lat: String,
        start_long: String,
        end_address: String?,
        end_lat: String?,
        end_long: String?,
        car_type_id: String,
        payment_type: String?,
        type: String?,
        later_order_date: String?,
        later_order_time: String?,
        coupon: String?,
        notes: String?,
        Authorization: String,
        gender: String,
        captain_type: String?,
        disturb: String

    ): Observable<OrderDetailsModel> {
        return RetroWeb.serviceApi.createTrip(expected_price,tripTime,priceId,start_address, start_lat, start_long, end_address, end_lat, end_long, car_type_id,
            type, later_order_date, later_order_time, coupon, notes, payment_type, gender, captain_type, disturb, Authorization)
    }

    override fun getRountes(src: String, dist: String,key:String): Observable<MyRouteModel> {
        return RetroWeb.serviceMapApi.getRouts(src,dist,key)
    }

    override fun savePlace(
        lat: String,
        lng: String,
        name: String?,
        address: String,
        place_id:String?,
        header: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.savePlace(lat,lng,name,address,place_id,header)
    }

    override fun searchPlaces(lat: String, lng: String,name:String, page:String,header: String): Flowable<StoreModel> {
        return RetroWeb.serviceApi.searchStores(lat/*null*/,lng/*null*/,name,page,header)
    }

    override fun nearStores(
        lat: String,
        lng: String,
        page: String,
        token: String
    ): Flowable<StoreModel> {
        return RetroWeb.serviceApi.nearResturants(lat,lng,page,token)
    }

    override fun getPlaces(
        lat: String,
        lng: String,
        token: String
    ): Observable<WholePlacesModel> {
        return RetroWeb.serviceApi.nearstores(lat,lng,token)
    }

    override fun getExpectedTime(
        from_lat: String,
        from_lng: String,
        car_type_id:Int,
        header: String
    ): Observable<ExpectedTimeModel> {
        return RetroWeb.serviceApi.getExpectedTime(from_lat,from_lng,car_type_id,header)
    }

    override fun deleteNotif(id: Int, token: String): Observable<ResetModel> {
        return RetroWeb.serviceApi.delete_notif(id,token)
    }



    override fun getPreviousTrips( header: String): Observable<PrevTripModel> {
        return RetroWeb.serviceApi.getPreviousTrips(header)
    }

    override fun years(): Observable<YearsModel> {
        return RetroWeb.serviceApi.years()
    }

    override fun points(header: String): Observable<BlanceModel> {
        return RetroWeb.serviceApi.points(header)
    }

    override fun blanced(header: String): Observable<BlanceModel> {
        return RetroWeb.serviceApi.balance(header)
    }


    override fun resetPass(pass: String, header: String): Observable<RegisterModel> {
        return RetroWeb.serviceApi.resetPassword(pass,header)
    }

    override fun resendCode(header: String): Observable<RegisterModel> {
        return RetroWeb.serviceApi.resendActivation(header)
    }

    override fun forgetPass(phone: String): Observable<RegisterModel> {
        return RetroWeb.serviceApi.forgetPass(phone)
    }

    override fun sigIn(phone: String, country_iso: String,social_id: String?,deviceId: String): Observable<RegisterModel> {
        return RetroWeb.serviceApi.signIn(phone,country_iso,social_id=social_id, device_id = deviceId)
    }

    override fun verification(code: String, header: String): Observable<RegisterModel> {
        return RetroWeb.serviceApi.activation(code,header)
    }

    override fun uploadImage(
        lang: String,
        file: MultipartBody.Part
    ): Observable<UploadImageResponse> {
        return RetroWeb.serviceApi.uploadImage(Util.language,file)


    }


    override fun uploadImage(file: MultipartBody.Part): Observable<UploadImageResponse> {
        return RetroWeb.serviceApi.uploadImage(Util.language,file)
    }

    override fun captainSignup(
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
    ): Observable<SigninResponse> {
        return RetroWeb.serviceApi.captainSignup(Util.language,
            name,
            phone,
            email,
            birthdate,
            identity_number,
        city_id,
        friend_code,
        marketing_code,
        identity_card,
        driving_license,
        car_form,
        iban,
        car_insurance,
        personal_image,
        car_image,
        car_type,
        car_model,
        car_color,
        manufacturing_year,
        car_numbers,
        car_letters,
        sequenceNumber,
        plateType)
    }


    override fun getCities(country_id: Int)
            : Observable<CitiesModel> {
        return RetroWeb.serviceApi.getCities(country_id)
    }



    override fun getUser(phone:String,country_iso: String,
                         email:String,
                         password:String,name:String,friend_code:String?,social_id: String?,auth: String,deviceId:String
    ): Observable<RegisterModel> {
       return RetroWeb.serviceApi.register(phone,country_iso,
           email,password,name,friend_code,social_id = social_id,Authorization = auth, device_id = deviceId)
    }


    override fun getAds()
            : Observable<AdsModel> {
        return RetroWeb.serviceApi.getAds()
    }


    override fun getCountries()
            : Observable<CountriesModel> {
        return RetroWeb.serviceApi.getCountries(Util.language)
    }

    override fun changePhone(
        phone: String,
        country_iso: String,
        token: String
    ): Observable<RegisterModel> {
        return RetroWeb.serviceApi.changePhone(phone, country_iso, token)
    }

    override fun activationPhone(
        code: String,
        phone: String,
        country_iso: String,
        token: String
    ): Observable<RegisterModel> {
        return RetroWeb.serviceApi.activationPhone(code, phone, country_iso, token)
    }

    override fun storeDetails(
        store_id: String,
        lat: String?,
        lng: String?
    ): Observable<StoreDetailsModel> {
        return RetroWeb.serviceApi.storeDetails(store_id,lat,lng,prefs.token!!)
    }

    override fun paymentWays(): Observable<PaymentWaysModel> {
        return RetroWeb.serviceApi.paymentWays(prefs.token!!)
    }

    override fun hyperIndex(
        type: String,amount: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.
        hyperIndex(type,amount,prefs.token!!)
    }

    override fun hyperResult(
        type: String,resourcePath: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.hyperResult(type,resourcePath,prefs.token!!)
    }
    override fun hyperIndex(
        orderId:String,type: String,amount: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.hyperIndex(orderId,type,amount,prefs.token!!)
    }

    override fun hyperResult(
        orderId:String,type: String,resourcePath: String
    ): Observable<ResetModel> {
        return RetroWeb.serviceApi.hyperResult(orderId,type,resourcePath,prefs.token!!)
    }
}

