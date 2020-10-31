package com.app.liferdeal.ui.activity.cart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.GuestUserPaymentModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.activity.restaurant.TimeListActivity;
import com.app.liferdeal.ui.activity.splash.SplashActivity;
import com.app.liferdeal.util.CommonMethods;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import java.util.Currency;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartCheckout extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_signIn, deleviry_add,edit_post_code, edt_city_name, edit_company_name,edit_add_note,edit_full_name,
            edit_email_add,edit_mobile_no, btn_order_placed, txt_selected_time;
    private LinearLayout ll_delivery_time;
    private ApiInterface apiInterface;
    private PrefsHelper prefsHelper;
    private ProgressDialog progressDialog;
    private RadioButton rd_cash, rd_paypal, rd_card;
    private ImageView img_back;
    private String strPostalCode="", strCityname="", strFullAddress="", restId="", TotalPrice="", currencySymbol="", pizzaQuantity="", Pizzaname="", selectedPizzaItemPrice="";
    private String  strMainRestName="", strMainRestLogo="", pizzaItemid="", subTotalPrice="";
    private String payment_transaction_paypal="", quantity, deliveryChargeValue="", SeviceFeesValue="", ServiceFees="", ServiceFeesType="", PackageFeesType="", PackageFees="", PackageFeesValue="", SalesTaxAmount="", VatTax="";
    private String  strsizeid, extraItemID, CustomerId, CustomerAddressId, payment_type, order_price, subTotalAmount, delivery_date, delivery_time, instructions, deliveryCharge,
            CouponCode,CouponCodePrice , couponCodeType, order_type, SpecialInstruction, extraTipAddAmount, RestaurantNameEstimate, discountOfferDescription, discountOfferPrice, RestaurantoffrType,
             PaymentProcessingFees, deliveryChargeValueType, WebsiteCodePrice, WebsiteCodeType, WebsiteCodeNo, preorderTime,
             GiftCardPay, WalletPay, loyptamount, table_number_assign, customer_country, group_member_id, loyltPnts, branch_id, FoodCosts,
            getTotalItemDiscount, getFoodTaxTotal7, getFoodTaxTotal19, TotalSavedDiscount , discountOfferFreeItems, number_of_people, customer_allow_register,address,
            street, floor_no, zipcode, phone, email, name_customer, city, mealID, mealquqntity, mealPrice, mealItemExtra, mealItemOption;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        progressDialog = new ProgressDialog(this);
        tv_signIn = findViewById(R.id.tv_signIn);
        deleviry_add = findViewById(R.id.deleviry_add);
        edit_post_code = findViewById(R.id.edit_post_code);
        edt_city_name = findViewById(R.id.edt_city_name);
        edit_company_name = findViewById(R.id.edit_company_name);
        edit_add_note = findViewById(R.id.edit_add_note);
        edit_full_name = findViewById(R.id.edit_full_name);
        edit_email_add = findViewById(R.id.edit_email_add);
        edit_mobile_no = findViewById(R.id.edit_mobile_no);
        ll_delivery_time = findViewById(R.id.ll_delivery_time);
        btn_order_placed = findViewById(R.id.btn_order_placed);
        txt_selected_time = findViewById(R.id.txt_selected_time);
        img_back = findViewById(R.id.img_back);
        rd_cash = findViewById(R.id.rd_cash);
        rd_paypal = findViewById(R.id.rd_paypal);
        rd_card = findViewById(R.id.rd_card);


       strPostalCode =  prefsHelper.getPref(Constants.SAVE_POSTAL_CODE);
       strCityname=  prefsHelper.getPref(Constants.SAVE_CITY_NAME);
       strFullAddress = prefsHelper.getPref(Constants.SAVE_FULL_ADDRESS);

       edit_post_code.setText(strPostalCode);
       edt_city_name.setText(strCityname);
       deleviry_add.setText(strFullAddress);

       restId = getIntent().getStringExtra("RestId");
        TotalPrice = getIntent().getStringExtra("TotalPrice");
        order_price = TotalPrice;

        subTotalPrice = getIntent().getStringExtra("SubTotalPrice");
        subTotalAmount = subTotalPrice;
        strMainRestName = getIntent().getStringExtra("RESTName");
        strMainRestLogo = getIntent().getStringExtra("RESTLOGO");
        pizzaItemid = getIntent().getStringExtra("subPizzaItemId");
        strsizeid = getIntent().getStringExtra("SIZEITEMID");
        extraItemID = getIntent().getStringExtra("globTempExtraItemidWithSizeIdd");
        delivery_date = getIntent().getStringExtra("delivery_date");
        FoodCosts = getIntent().getStringExtra("food_cost");
        quantity = getIntent().getStringExtra("quantity");
        deliveryChargeValue =  getIntent().getStringExtra("deliveryChargeValue");
        SeviceFeesValue = getIntent().getStringExtra("SeviceFeesValue");
        ServiceFees = getIntent().getStringExtra("ServiceFees");
        ServiceFeesType =  getIntent().getStringExtra("ServiceFeesType");
        PackageFeesType = getIntent().getStringExtra("PackageFeesType");
        PackageFees =  getIntent().getStringExtra("PackageFees");
        PackageFeesValue = getIntent().getStringExtra("PackageFeesValue");
        SalesTaxAmount =  getIntent().getStringExtra("SalesTaxAmount");
        VatTax =  getIntent().getStringExtra("VatTax");
        order_type =  getIntent().getStringExtra("deliveryType");
        pizzaQuantity =  getIntent().getStringExtra("pizzaQuantity");
        Pizzaname =  getIntent().getStringExtra("Pizzaname");
        selectedPizzaItemPrice =  getIntent().getStringExtra("selectedPizzaItemPrice");
        customer_allow_register = "yes";
        payment_type = "cash";



        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
        currencySymbol =  hh.getSymbol();

        btn_order_placed.setText("Order on pay : " + currencySymbol + " " + ""+String.format("%.2f", Double.parseDouble(TotalPrice)));

        if (prefsHelper.isLoggedIn())
        {
            CustomerId = prefsHelper.getPref(Constants.CUSTOMER_ID);
            CustomerAddressId = prefsHelper.getPref(Constants.CUSTOMER_ADDRESS_ID);
        }
        btn_order_placed.setOnClickListener(this);
        tv_signIn.setOnClickListener(this);
        ll_delivery_time.setOnClickListener(this);
        img_back.setOnClickListener(this);
        rd_cash.setOnClickListener(this);
        rd_paypal.setOnClickListener(this);
        rd_card.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_signIn:
                break;

            case R.id.btn_order_placed:
                zipcode = CommonMethods.getStringDataInbase64(edit_post_code.getText().toString());
                city = CommonMethods.getStringDataInbase64(edt_city_name.getText().toString());
                address = CommonMethods.getStringDataInbase64(deleviry_add.getText().toString());
                System.out.println("==== zipcode in base64" + zipcode);
                name_customer = CommonMethods.getStringDataInbase64(edit_full_name.getText().toString().trim());
                email = edit_mobile_no.getText().toString().trim();
                phone = CommonMethods.getStringDataInbase64(edit_mobile_no.getText().toString().trim());
               delivery_time = txt_selected_time.getText().toString();
                if (edit_full_name.getText().toString().equalsIgnoreCase(""))
                {
                    edit_full_name.setError("Please Enter Full name");
                    edit_full_name.requestFocus();
                }
               else if (edit_email_add.getText().toString().equalsIgnoreCase(""))
                {
                    edit_email_add.setError("Please Enter Email id");
                    edit_email_add.requestFocus();
                }
                else if (!isValidEmailId(edit_email_add.getText().toString().trim())) {
                    edit_email_add.setError("Invalid Email Id");
                    edit_email_add.requestFocus();
                }
               else if (edit_mobile_no.getText().toString().equalsIgnoreCase(""))
                {
                    edit_mobile_no.setError("Please Enter Mobile Number");
                    edit_mobile_no.requestFocus();
                }

               else if (txt_selected_time.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(CartCheckout.this, "Please enter Delivery Time", Toast.LENGTH_SHORT).show();
                }

               else if(payment_type.equalsIgnoreCase(""))
                {
                    Toast.makeText(CartCheckout.this, "Please select payment Type", Toast.LENGTH_SHORT).show();

                }
               else
                {
                    getPaymentRequestData();
                }





                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.ll_delivery_time:
                Intent i =  new Intent(CartCheckout.this, TimeListActivity.class);
                i.putExtra("RestId", restId);
                startActivityForResult(i,10);
               // startActivity(i);
                break;

            case R.id.rd_cash:

                    payment_type = "cash";
                rd_cash.setChecked(true);
                rd_paypal.setChecked(false);
                rd_card.setChecked(false);
                break;

            case R.id.rd_paypal:
                payment_type = "paypal";
                rd_cash.setChecked(false);
                rd_paypal.setChecked(true);
                rd_card.setChecked(false);
                break;

            case R.id.rd_card:
                payment_type = "card";
                rd_cash.setChecked(false);
                rd_paypal.setChecked(false);
                rd_card.setChecked(true);
                break;

            default:
                break;
        }
    }

    private boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,8})$").matcher(email).matches();
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 9 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                // txtPhone.setError("Not Valid Number");
            } else {
                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10)
        {
            if (resultCode== Activity.RESULT_OK)
            {
                String gettime = data.getStringExtra("gettime");
                System.out.println("==== gettime : " + gettime);
                txt_selected_time.setText(gettime);

            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("==== intent : " + intent);
    }

    private void getPaymentRequestData(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<GuestUserPaymentModel> observable = apiInterface.getPaymentGuestData(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,payment_transaction_paypal, pizzaItemid, quantity, TotalPrice, strsizeid, extraItemID, CustomerId, CustomerAddressId, payment_type, order_price, subTotalAmount, delivery_date, delivery_time, instructions, deliveryCharge,
                CouponCode,CouponCodePrice , couponCodeType, SalesTaxAmount, order_type, SpecialInstruction, extraTipAddAmount, RestaurantNameEstimate, discountOfferDescription, discountOfferPrice, RestaurantoffrType,
                ServiceFees, PaymentProcessingFees, deliveryChargeValueType, ServiceFeesType, PackageFeesType, PackageFees, WebsiteCodePrice, WebsiteCodeType, WebsiteCodeNo, preorderTime,
                VatTax, GiftCardPay, WalletPay, loyptamount, table_number_assign, customer_country, group_member_id, loyltPnts, branch_id, FoodCosts,
                getTotalItemDiscount, getFoodTaxTotal7, getFoodTaxTotal19, TotalSavedDiscount , discountOfferFreeItems, number_of_people, customer_allow_register,address,
                street, floor_no, zipcode, phone, email, name_customer, city, restId, mealID, mealquqntity, mealPrice, mealItemExtra, mealItemOption);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GuestUserPaymentModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GuestUserPaymentModel searchResult) {
                         showProgress();
                        if (searchResult.getSuccess()==1)
                        {
                            System.out.println("==== success");
                           String restname =  searchResult.getRestaurantName().toString();
                           String restTime =  searchResult.getRequestTime().toString();
                           String deliveryDate =  searchResult.getDeliverydate().toString();
                           String customeName =  searchResult.getNameCustomer();
                           String orderNumber =  searchResult.getOrderNo();
                           String orderType =  searchResult.getOrderType();
                           String oldprice =  searchResult.getOrdPrice();
                            if (CartActivity.mInstance!=null)
                            {
                                CartActivity.mInstance.finish();
                            }
                              Intent ii = new Intent(CartCheckout.this, ThankyouPageActivity.class);
                              ii.putExtra("restname", restname);
                              ii.putExtra("restTime", restTime);
                              ii.putExtra("deliveryDate", deliveryDate);
                              ii.putExtra("customeName", customeName);
                              ii.putExtra("orderNumber", orderNumber);
                              ii.putExtra("orderType", orderType);
                              ii.putExtra("oldprice", oldprice);
                              ii.putExtra("strMainRestLogo", strMainRestLogo);
                            ii.putExtra("pizzaQuantity", quantity);
                            ii.putExtra("Pizzaname", Pizzaname);
                            ii.putExtra("selectedPizzaItemPrice", selectedPizzaItemPrice);
                              startActivity(ii);
                              finish();
                              hideProgress();
                        }

                        //  setAdapterCategory(searchResult.getRestaurantMencategory());
                       /* if (searchResult.getRestaurantMencategory().get(0).getError()==1)
                        {

                            Toast.makeText(RestaurantDetails.this, "There is no Details", Toast.LENGTH_SHORT).show();
                            hideProgress();
                        }
                        else
                        {
                            setAdapterCategoryForQuick(searchResult.getRestaurantMencategory());                        }

                    }*/
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        Log.d("TAG","log...."+e);
                    }

                    @Override
                    public void onComplete() {
                        //   activity.mySharePreferences.setBundle("login");

                    }
                });

    }

    public void showProgress(){
        try {

            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public void hideProgress(){
        try {
            progressDialog.dismiss();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
