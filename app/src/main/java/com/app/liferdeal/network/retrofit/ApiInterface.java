package com.app.liferdeal.network.retrofit;

import com.app.liferdeal.model.FoodGallery;
import com.app.liferdeal.model.GetProfileModel;
import com.app.liferdeal.model.LanguageModel;
import com.app.liferdeal.model.PhpInitialInfoModel;
import com.app.liferdeal.model.loginsignup.SignInModel;
import com.app.liferdeal.model.loginsignup.SignupModel;
import com.app.liferdeal.model.restaurant.AddTicketDataModel;
import com.app.liferdeal.model.restaurant.AddTicketModel;
import com.app.liferdeal.model.restaurant.ChangepasswordModel;
import com.app.liferdeal.model.restaurant.ContactusModel;
import com.app.liferdeal.model.restaurant.CusineFilterModel;
import com.app.liferdeal.model.restaurant.DiscountModel;
import com.app.liferdeal.model.restaurant.FoodExtraModel;
import com.app.liferdeal.model.restaurant.FoodItemSizeModel;
import com.app.liferdeal.model.restaurant.GuestUserPaymentModel;
import com.app.liferdeal.model.restaurant.InfoMenuAcModel;
import com.app.liferdeal.model.restaurant.LoyalityPointsModel;
import com.app.liferdeal.model.restaurant.MYOrderTrackDetailModel;
import com.app.liferdeal.model.restaurant.ModelAddressList;
import com.app.liferdeal.model.restaurant.MyOrderModel;
import com.app.liferdeal.model.restaurant.OrderDetailItem;
import com.app.liferdeal.model.restaurant.RestDetailClickFoodModel;
import com.app.liferdeal.model.restaurant.RestMenuReviewModel;
import com.app.liferdeal.model.restaurant.RestaurantBookTableModel;
import com.app.liferdeal.model.restaurant.RestaurantDetailsModel;
import com.app.liferdeal.model.restaurant.RestaurantGalleryModel;
import com.app.liferdeal.model.restaurant.RestaurantMainModel;
import com.app.liferdeal.model.restaurant.SaveAddressModel;
import com.app.liferdeal.model.restaurant.ShippingCartModel;
import com.app.liferdeal.model.restaurant.TicketListDataModel;
import com.app.liferdeal.model.restaurant.TimeListModel;
import com.app.liferdeal.model.splash.SplashModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("phpexpert_customer_account_register.php")
    Observable<SignupModel> registerUser(@Field("first_name")String fstName,
                                         @Field("last_name")String lstName,
                                         @Field("user_email")String emailId,
                                         @Field("user_phone")String mobilno,
                                         @Field("user_pass")String password,
                                         @Field("customer_country")String costomerCountry,
                                         @Field("referral_code")String refferCode,
                                         @Field("api_key")String apiKey,
                                         @Field("lang_code")String langCode,
                                         @Field("device_id")String device_id,
                                         @Field("device_platform")String platform);

    @FormUrlEncoded
    @POST("phpexpert_customer_login.php")
    Observable<SignInModel> loginUser(@Field("user_email")String userEmail,
                                      @Field("user_pass")String password,
                                      @Field("api_key")String apiKey,
                                      @Field("lang_code")String langCode,
                                      @Field("device_id")String device_id,
                                      @Field("device_platform")String platform);

    @FormUrlEncoded
    @POST("phpexpert_search.php")
    Observable<RestaurantMainModel> getSearchRestData(@Field("full_address") String fulladdress,
                                                      @Field("current_city")String currentcity,
                                                      @Field("current_postcode ")String current_postcode ,
                                                      @Field("current_localty")String current_localty,
                                                      @Field("api_key")String apiKey,
                                                      @Field("lang_code")String langCode,
                                                      @Field("cuisine") ArrayList<String> cuisine);

    @GET("phpexpert_website_information.php")
    Observable<PhpInitialInfoModel> getSplashData();
    @FormUrlEncoded
    @POST("phpexpert_restaurant_app_splash_gallery.php")
    Observable<SplashModel> getSplashImageData(@Field("api_key") String api_key,
                                               @Field("lang_code")String lang_code,
                                               @Field("splash_type")String splash_type);

    @FormUrlEncoded
    @POST("phpexpert_restaurantMenuCategory.php")
    Observable<RestaurantDetailsModel> getSearchRestDetailsData(@Field("api_key") String api_key,
                                                                @Field("lang_code")String lang_code,
                                                                @Field("resid")String resid);

    @FormUrlEncoded
    @POST("phpexpert_food_gallery_android.php")
    Observable<RestaurantGalleryModel> getRestaurantFoodGallery(@Field("api_key") String api_key,
                                                                @Field("lang_code")String lang_code,
                                                                @Field("resid")String resid);

    @FormUrlEncoded
    @POST("phpexpert_menu_gallery_android.php")
    Observable<RestaurantGalleryModel> getRestaurantMenuGallery(@Field("api_key") String api_key,
                                                                @Field("lang_code")String lang_code,
                                                                @Field("resid")String resid);


    @FormUrlEncoded
   // @POST("phpexpert_food_items_android.php")
    @POST("phpexpert_all_category_list.php")
    Observable<RestDetailClickFoodModel> getClockFoodMenu(@Field("api_key") String api_key,
                                                          @Field("lang_code")String lang_code,
                                                          @Field("resid")String resid,
                                                          @Field("Category_ID")int Category_ID);

    @FormUrlEncoded
    @POST("phpexpert_restaurantMenuItemSize.php")
    Observable<FoodItemSizeModel> getFoodItemSize(@Field("api_key") String api_key,
                                                  @Field("lang_code")String lang_code,
                                                  @Field("ItemID")int resid);

    @FormUrlEncoded
    @POST("phpexpert_food_items_extra_android.php")
    Observable<FoodExtraModel> getFoodItemExtra(@Field("api_key") String api_key,
                                                @Field("lang_code")String lang_code,
                                                @Field("ItemID")int resid,
                                                @Field("FoodItemSizeID")int FoodItemSizeID );
    @FormUrlEncoded
    @POST("phpexpert_customer_add_new_address.php")
    Observable<SaveAddressModel> saveNewAddress(@Field("api_key") String api_key,
                                                @Field("lang_code")String lang_code,
                                                @Field("user_phone")String user_phone,
                                                @Field("customerAddressLabel")String customerAddressLabel,
                                                @Field("customerFloor_House_Number")String customerFloor_House_Number,
                                                @Field("customerStreet")String customerStreet,
                                                @Field("customerCity")String customerCity,
                                                @Field("customerZipcode")String customerZipcode,
                                                @Field("address_direction")String address_direction,
                                                @Field("CustomerId")String CustomerId,
                                                @Field("customerFlat_Name")String customerFlat_Name
    );


    @FormUrlEncoded
    @POST("phpexpert_customer_address_list.php")
    Observable<ModelAddressList> getSavedAddress(@Field("customer_long") String customer_long,
                                                 @Field("customer_lat")String customer_lat,
                                                 @Field("CustomerId")String CustomerId );
    @FormUrlEncoded
    @POST("phpexpert_customer_profile_inform.php")
    Observable<GetProfileModel> getProfileData(@Field("CustomerId") String CustomerId
    );


    @FormUrlEncoded
    @POST("phpexpert_customer_app_langauge_list.php")
    Observable<LanguageModel> getLanguageList(@Field("api_key") String api_key);

    @FormUrlEncoded
    @POST("phpexpert_cuisine.php")
    Observable<CusineFilterModel> getCusineFilterList(@Field("api_key") String api_key,
                                                      @Field("lang_code")String lang_code);

    @FormUrlEncoded
    @POST("phpexpert_restaurant_loyalty_point_list.php")
    Observable<LoyalityPointsModel> getLoyalityPoints(@Field("api_key") String api_key,
                                                      @Field("lang_code")String lang_code);

    @FormUrlEncoded
    @POST("phpexpert_customer_profile_inform.php")
    Observable<LoyalityPointsModel> updateProfileData(@Field("api_key") String api_key,
                                                      @Field("lang_code")String lang_code);

    @FormUrlEncoded
    @POST("ServiceChargetGet.php")
    Observable<ShippingCartModel> getServiceCharge(@Field("api_key") String api_key,
                                                   @Field("lang_code")String lang_code,
                                                   @Field("subTotal")String subTotal,
                                                   @Field("resid")String resid,
                                                   @Field("restaurant_locality")String restaurant_locality,
                                                   @Field("Order_Type")String Order_Type
    );

    @FormUrlEncoded
    @POST("phpexpert_time_fetch.php")
    Observable<TimeListModel> getTimeListData(@Field("api_key") String api_key,
                                              @Field("lang_code")String lang_code,
                                              @Field("resid")String resid);

    @FormUrlEncoded
    @POST("phpexpert_customer_ticket_list.php")
    Observable<TicketListDataModel> getTicketList(@Field("api_key") String api_key,
                                                  @Field("lang_code") String lang_code,
                                                  @Field("CustomerId") String CustomerId);


    @FormUrlEncoded
    @POST("phpexpert_customer_passwordChange.php")
    Observable<ChangepasswordModel> changePassword(@Field("api_key") String api_key,
                                                   @Field("lang_code") String lang_code,
                                                   @Field("CustomerId") String CustomerId,
                                                   @Field("OldCustomerPassword") String OldCustomerPassword,
                                                   @Field("NewCustomerPassword") String NewCustomerPassword
    );

    @FormUrlEncoded
    @POST("phpexpert_customer_ticket_submit.php")
    Observable<AddTicketDataModel> submitTicket(@Field("api_key") String api_key,
                                                @Field("lang_code") String lang_code,
                                                @Field("CustomerId") String customer_Id,
                                                @Field("order_identifyno") String oderno,
                                                @Field("Order_Message") String order_msg,
                                                @Field("Order_Issue_Subject")
                                                        String issueType,
                                                @Field("Order_Email_Address")
                                                        String email_address);

    @FormUrlEncoded
    @POST("phpexpert_customer_contact_submit.php")
    Observable<ContactusModel> sendContactUsData(@Field("api_key") String api_key,
                                                 @Field("lang_code") String lang_code,
                                                 @Field("contact_name") String contact_name,
                                                 @Field("contact_email") String contact_email,
                                                 @Field("contact_mobile") String contact_mobile,
                                                 @Field("contact_message") String contact_message
    );

    @FormUrlEncoded
    @POST("phpexpert_customer_table_booking.php")
    Observable<RestaurantBookTableModel> setBookingTableData(@Field("api_key") String api_key,
                                                             @Field("lang_code") String lang_code,
                                                             @Field("CustomerId") String CustomerId,
                                                             @Field("resid") String resid,
                                                             @Field("table_number_assign") String table_number_assign,
                                                             @Field("booking_email") String booking_email,
                                                             @Field("booking_mobile") String booking_mobile,
                                                             @Field("booking_date") String booking_date,
                                                             @Field("booking_time") String booking_time,
                                                             @Field("Number_of_person") String Number_of_person,
                                                             @Field("booking_instruction") String booking_instruction
    );



    @FormUrlEncoded
    @POST("phpexpert_OrderDisplay.php")
    Observable<MyOrderModel> getMyOrderDetails(@Field("api_key") String api_key,
                                               @Field("lang_code")String lang_code,
                                               @Field("CustomerId")String CustomerId);

    @FormUrlEncoded
    @POST("phpexpert_restaurant_Offers.php")
    Observable<DiscountModel> getDiscountOfferData(@Field("api_key") String api_key,
                                                   @Field("lang_code")String lang_code,
                                                   @Field("resid")String CustomerId);

    @FormUrlEncoded
    @POST("phpexpert_restaurantReviewAndroid.php")
    Observable<RestMenuReviewModel> getRestMenuReviewData(@Field("api_key") String api_key,
                                                          @Field("lang_code")String lang_code,
                                                          @Field("resid")String resid);

    @FormUrlEncoded
    @POST("phpexpert_Order_DetailsDisplay.php")
    Observable<MYOrderTrackDetailModel> getMyOrderDetailsDisplay(@Field("api_key") String api_key,
                                                                 @Field("lang_code")String lang_code,
                                                                 @Field("order_identifyno")String order_identifyno);

    @FormUrlEncoded
    @POST("phpexpert_Order_Track_Dettail.php")
    Observable<MYOrderTrackDetailModel> getMyOrderTrackData(@Field("api_key") String api_key,
                                                            @Field("lang_code")String lang_code,
                                                            @Field("order_identifyno")String order_identifyno);
    @FormUrlEncoded
    @POST("phpexpert_restaurant_all_information.php")
    Observable<InfoMenuAcModel> getRestMenuInfoButtondata(@Field("api_key") String api_key,
                                                          @Field("lang_code")String lang_code,
                                                          @Field("resid")String resid);


    @FormUrlEncoded
    @POST("phpexpert_write_review.php")
    Observable<MYOrderTrackDetailModel> submitReviewData(@Field("api_key") String api_key,
                                                            @Field("lang_code")String lang_code,
                                                            @Field("CustomerId")String order_identifyno,
                                                            @Field("RestaurantReviewRating")String RestaurantReviewRating,
                                                            @Field("Quality_ratingN")String Quality_ratingN
                                                         ) ;



    @FormUrlEncoded
    @POST("phpexpert_payment_android_submit_guest.php")
    Observable<GuestUserPaymentModel> getPaymentGuestData(@Field("api_key") String api_key,
                                                          @Field("lang_code")String lang_code,
                                                          @Field("payment_transaction_paypal")String payment_transaction_paypal,
                                                          @Field("itemId")String itemId,
                                                          @Field("Quantity")String Quantity,
                                                          @Field("Price")String Price,
                                                          @Field("strsizeid")String strsizeid,
                                                          @Field("extraItemID")String extraItemID,
                                                          @Field("CustomerId")String CustomerId,
                                                          @Field("CustomerAddressId")String CustomerAddressId,
                                                          @Field("payment_type")String payment_type,
                                                          @Field("order_price")String order_price,
                                                          @Field("subTotalAmount")String subTotalAmount,
                                                          @Field("delivery_date")String delivery_date,
                                                          @Field("delivery_time")String delivery_time,
                                                          @Field("instructions")String instructions,
                                                          @Field("deliveryCharge")String deliveryCharge,
                                                          @Field("CouponCode")String CouponCode,
                                                          @Field("CouponCodePrice")String CouponCodePrice,
                                                          @Field("couponCodeType")String couponCodeType,
                                                          @Field("SalesTaxAmount")String SalesTaxAmount,
                                                          @Field("order_type")String order_type,
                                                          @Field("SpecialInstruction")String SpecialInstruction,
                                                          @Field("extraTipAddAmount")String extraTipAddAmount,
                                                          @Field("RestaurantNameEstimate")String RestaurantNameEstimate,
                                                          @Field("discountOfferDescription")String discountOfferDescription,
                                                          @Field("discountOfferPrice")String discountOfferPrice,
                                                          @Field("RestaurantoffrType")String RestaurantoffrType,
                                                          @Field("ServiceFees")String ServiceFees,
                                                          @Field("PaymentProcessingFees")String PaymentProcessingFees,
                                                          @Field("deliveryChargeValueType")String deliveryChargeValueType,
                                                          @Field("ServiceFeesType")String ServiceFeesType,
                                                          @Field("PackageFeesType")String PackageFeesType,
                                                          @Field("PackageFees")String PackageFees,
                                                          @Field("WebsiteCodePrice")String WebsiteCodePrice,
                                                          @Field("WebsiteCodeType")String WebsiteCodeType,
                                                          @Field("WebsiteCodeNo")String WebsiteCodeNo,
                                                          @Field("preorderTime")String preorderTime,
                                                          @Field("VatTax")String VatTax,
                                                          @Field("GiftCardPay")String GiftCardPay,
                                                          @Field("WalletPay")String WalletPay,
                                                          @Field("loyptamount")String loyptamount,
                                                          @Field("table_number_assign")String table_number_assign,
                                                          @Field("customer_country")String customer_country,
                                                          @Field("group_member_id")String group_member_id,
                                                          @Field("loyltPnts")String loyltPnts,
                                                          @Field("branch_id")String branch_id,
                                                          @Field("FoodCosts")String FoodCosts,
                                                          @Field("getTotalItemDiscount")String getTotalItemDiscount,
                                                          @Field("getFoodTaxTotal7")String getFoodTaxTotal7,
                                                          @Field("getFoodTaxTotal19")String getFoodTaxTotal19,
                                                          @Field("TotalSavedDiscount")String TotalSavedDiscount,
                                                          @Field("discountOfferFreeItems")String discountOfferFreeItems,
                                                          @Field("number_of_people")String number_of_people,
                                                          @Field("customer_allow_register")String customer_allow_register,
                                                          @Field("address")String address,
                                                          @Field("street")String street,
                                                          @Field("floor_no")String floor_no,
                                                          @Field("zipcode")String zipcode,
                                                          @Field("phone")String phone,
                                                          @Field("email")String email,
                                                          @Field("name_customer")String name_customer,
                                                          @Field("city")String city,
                                                          @Field("resid")String resid,
                                                          @Field("mealID")String mealID,
                                                          @Field("mealquqntity")String mealquqntity,
                                                          @Field("mealPrice")String mealPrice,
                                                          @Field("mealItemExtra")String mealItemExtra,
                                                          @Field("mealItemOption")String mealItemOption );

}
