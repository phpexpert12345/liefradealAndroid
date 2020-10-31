package com.app.liferdeal.ui.activity.cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.GuestUserPaymentModel;
import com.app.liferdeal.model.restaurant.RaviCartModle;
import com.app.liferdeal.model.restaurant.RestaurantDetailsModel;
import com.app.liferdeal.model.restaurant.ShippingCartModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.Database.Database;
import com.app.liferdeal.ui.activity.restaurant.AddressActivity;
import com.app.liferdeal.ui.activity.restaurant.RestaurantDetails;
import com.app.liferdeal.ui.activity.splash.SplashActivity;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private PrefsHelper prefsHelper;
    Database database;
    ArrayList<RaviCartModle> raviCartModles;
    public Double totalPrice = 0.0, disPrice = 0.0, subTot = 0.0, minOrder = 0.0, grandTotal = 0.0;
    private LinearLayout rl_pre_order_delivery_time, rl_pre_order_collection_time;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    private TextView tv_sipping, tv_sub_total, tv_food_item_total;
    private RecyclerView list_view_items;
    private ImageView img_select_picup,img_select_delivery;
    public String orderType="", RestId="",  postalCode="";
    private RelativeLayout rl_pickup,rl_delivery;
    private RelativeLayout card_checkout;
    private TextView tv_total,tv_count, checkout_total_price, txt_inclusive_tax_food;
    private ImageView img_back;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private String currencySymbol="", strMainRestName="", strMainRestLogo="", pizzaItemid="";
    private String payment_transaction_paypal="", quantity, deliveryChargeValue="", SeviceFeesValue="", ServiceFees="", ServiceFeesType="", PackageFeesType="", PackageFees="", PackageFeesValue="", SalesTaxAmount="", VatTax="";
    private String  strsizeid, extraItemID, CustomerId, CustomerAddressId, payment_type, order_price, subTotalAmount, delivery_date, delivery_time, instructions, deliveryCharge,
            CouponCode,CouponCodePrice , couponCodeType,  order_type, SpecialInstruction, extraTipAddAmount, RestaurantNameEstimate, discountOfferDescription, discountOfferPrice, RestaurantoffrType,
             PaymentProcessingFees, deliveryChargeValueType, WebsiteCodePrice, WebsiteCodeType, WebsiteCodeNo, preorderTime,
             GiftCardPay, WalletPay, loyptamount, table_number_assign, customer_country, group_member_id, loyltPnts, branch_id, FoodCosts,
            getTotalItemDiscount, getFoodTaxTotal7, getFoodTaxTotal19, TotalSavedDiscount , discountOfferFreeItems, number_of_people, customer_allow_register,address,
            street, floor_no, zipcode, phone, email, name_customer, city, mealID, mealquqntity, mealPrice, mealItemExtra, mealItemOption;

    public static CartActivity mInstance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        init();
    }
    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        progressDialog = new ProgressDialog(this);
        mInstance = this;
        database = new Database(CartActivity.this);
        raviCartModles = new ArrayList<>();
        list_view_items = findViewById(R.id.list_view_items);
        img_back = findViewById(R.id.img_back);
        rl_pickup = findViewById(R.id.rl_pickup);
        rl_delivery = findViewById(R.id.rl_delivery);
        card_checkout =findViewById(R.id.card_checkout);
        checkout_total_price =findViewById(R.id.checkout_total_price);
       // img_select_delivery = findViewById(R.id.img_select_delivery);
       // img_select_picup = findViewById(R.id.img_select_picup);
        tv_sub_total =findViewById(R.id.tv_sub_total);
        txt_inclusive_tax_food = findViewById(R.id.txt_inclusive_tax_food);
        tv_total = findViewById(R.id.tv_total);
        tv_food_item_total = findViewById(R.id.tv_food_item_total);
        // tv_count = findViewById(R.id.tv_count);
         RestId = getIntent().getStringExtra("RESTID");
         strMainRestName = getIntent().getStringExtra("RESTName");
         strMainRestLogo = getIntent().getStringExtra("RESTLOGO");
         pizzaItemid = getIntent().getStringExtra("subPizzaItemId");
        strsizeid = getIntent().getStringExtra("SIZEITEMID");
        extraItemID = getIntent().getStringExtra("globTempExtraItemidWithSizeIdd");
        System.out.println("==== rest id in cart activity : " + RestId);
       postalCode =  prefsHelper.getPref(Constants.SAVE_POSTAL_CODE);
       if (prefsHelper.isLoggedIn())
       {
           CustomerId = prefsHelper.getPref(Constants.CUSTOMER_ID);
           CustomerAddressId = prefsHelper.getPref(Constants.CUSTOMER_ADDRESS_ID);
       }

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        delivery_date = formattedDate;
        System.out.println("=== delivery_date date : " + delivery_date);

        img_back.setOnClickListener(this);
        rl_pickup.setOnClickListener(this);
        rl_delivery.setOnClickListener(this);
        card_checkout.setOnClickListener(this);
        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
        currencySymbol =  hh.getSymbol();
        this.orderType = "delivery";
        getShippingChargeData();

    }
    @Override
    protected void onStop() {
        super.onStop();
//        database.deal_delete();
        updateCart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateCart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateCart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCart();
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateCart();

    }

    private String extraItemNameG="";
    private String extraItemIDG="";
    private String selectedPizzaName="";
    private String selectedPizzaItemPrice="";
    private void updateCart() {
        try {
            //made by me
            totalPrice = 0.0;
            raviCartModles.clear();
            SQLiteDatabase db = database.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM item_table", null);
            //  Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
            if (cursor.moveToFirst()) {
                if (cursor.moveToFirst()) {
                    do {
                        String item_id = cursor.getString(0);
                        String item_name = cursor.getString(1);
                        String size_item_id = cursor.getString(2);
                        String size_item_name = cursor.getString(3);
                        String extra_item_id = cursor.getString(4);
                        String extra_item_name = cursor.getString(5);
                        String price = cursor.getString(6);
                        totalPrice = totalPrice + Double.parseDouble(price);
                        totalPrice = (double) Math.round(totalPrice * 100) / 100;
                        String item_quantity = cursor.getString(7);
                        extraItemNameG = extra_item_name;
                        extraItemIDG = extra_item_id;
                        quantity = item_quantity;
                        System.out.println("==== extraname : " + extraItemNameG);
                        System.out.println("==== extraname : " + extraItemIDG);
                        tv_total.setText(currencySymbol + "" + ""+String.format("%.2f",totalPrice));
                        checkout_total_price.setText(currencySymbol + "" + ""+String.format("%.2f",totalPrice));
                        order_price = totalPrice.toString();
                        subTotalAmount = totalPrice.toString();
                        tv_sub_total.setText(currencySymbol + "" + ""+String.format("%.2f",totalPrice));
                        tv_food_item_total.setText(currencySymbol + "" + ""+String.format("%.2f",totalPrice));
                        FoodCosts = tv_food_item_total.getText().toString();
                        txt_inclusive_tax_food.setText(currencySymbol + "" + ""+String.format("%.2f",totalPrice));
                        raviCartModles.add(new RaviCartModle(item_id, item_name, size_item_id, size_item_name, extra_item_id, extra_item_name, price, item_quantity));
                    } while (cursor.moveToNext());
                    //  tvTotalFoodCost.setText("+".concat(pound.concat("" + String.format("%.2f", totalPrice))));
                    //    tvTotalFoodCost.setText("+".concat(pound.concat("" +String.valueOf(totalPrice))));
                    // getDiscount();
                    CartAdapterravi cartAdapterravi = new CartAdapterravi(CartActivity.this, raviCartModles);
                    linearLayoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
                    list_view_items.setLayoutManager(linearLayoutManager);
                    list_view_items.setAdapter(cartAdapterravi);
                }
                else
                {
                    list_view_items.setVisibility(View.GONE);
                }


            } else {
                db.close();
          /*      Ravifinalitem.cart_Item_number = 0;
                Intent iii = new Intent(CartActivity.this, EmptyCartActivity.class);
                iii.putExtra("restaurantAddress", restaurantAddress);
                iii.putExtra("restaurantName", restaurantName);
                iii.putExtra("id", id);
                iii.putExtra("restaurantCategoryId", restaurantCategoryId);
                startActivity(iii);
                finish();*/
            }


        } catch (Exception e) {

            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rl_pickup:
                changeSelect("pickup");
                this.orderType = "pickup";
                //  addQuatity.insert("1","sufiyan");
                break;
            case R.id.rl_delivery:
                changeSelect("delivery");
                this.orderType = "delivery";
                // addQuatity.updateItem("1","name.....");
                break;
            case R.id.card_checkout:
                if (!this.orderType.equals("")) {
                    //  activity.setCurrent_fragment(new FragmentAddressList(), FragmentAddressList.FRAG_TITLE);
                    System.out.println("=== extraItemNameG :" + extraItemNameG);
                    System.out.println("=== extraItemIDG :" + extraItemIDG);
                    Intent i = new Intent(CartActivity.this, CartCheckout.class);
                    i.putExtra("RestId", RestId);
                    i.putExtra("TotalPrice", totalPrice.toString());
                    i.putExtra("SubTotalPrice", totalPrice.toString());
                    i.putExtra("RESTName", strMainRestName);
                    i.putExtra("RESTLOGO", strMainRestLogo);
                    i.putExtra("subPizzaItemId", pizzaItemid);
                    i.putExtra("SIZEITEMID", strsizeid);
                    i.putExtra("globTempExtraItemidWithSizeIdd", extraItemID);
                    i.putExtra("delivery_date", delivery_date);
                    i.putExtra("food_cost", FoodCosts);
                    i.putExtra("quantity", quantity);
                    i.putExtra("deliveryChargeValue", deliveryChargeValue);
                    i.putExtra("SeviceFeesValue", SeviceFeesValue);
                    i.putExtra("ServiceFees", ServiceFees);
                    i.putExtra("ServiceFeesType", ServiceFeesType);
                    i.putExtra("PackageFeesType", PackageFeesType);
                    i.putExtra("PackageFees", PackageFees);
                    i.putExtra("PackageFeesValue", PackageFeesValue);
                    i.putExtra("SalesTaxAmount", SalesTaxAmount);
                    i.putExtra("VatTax", VatTax);
                    i.putExtra("deliveryType", this.orderType);
                    i.putExtra("pizzaQuantity", quantity);
                    i.putExtra("Pizzaname", selectedPizzaName);
                    i.putExtra("selectedPizzaItemPrice", selectedPizzaItemPrice);
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "Please chose pickup type", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.img_back:
                Intent i = new Intent(CartActivity.this, RestaurantDetails.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public void changeSelect(String type){
        if (type.equals("pickup")){
            rl_pickup.setBackgroundResource(R.drawable.circle_background);
            rl_delivery.setBackgroundResource(R.drawable.edit_back_with_gray);
          //  img_select_picup.setImageResource(R.drawable.img_select);
          //  img_select_delivery.setImageResource(R.drawable.unselect);
        }
        if (type.equals("delivery")){
            rl_delivery.setBackgroundResource(R.drawable.circle_background);
            rl_pickup.setBackgroundResource(R.drawable.edit_back_with_gray);
           // img_select_picup.setImageResource(R.drawable.unselect);
           // img_select_delivery.setImageResource(R.drawable.img_select);
        }

    }
    public class CartAdapterravi extends RecyclerView.Adapter<CartAdapterravi.ViewHolder> {
        Context context;
        ArrayList<RaviCartModle> rr;

        public CartAdapterravi(Context c, ArrayList<RaviCartModle> r) {
            this.context = c;
            this.rr = r;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vv = layoutInflater.inflate(R.layout.recyclerview_cart_item, parent, false);
            return new ViewHolder(vv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            if (rr.get(position).getSize_item_name().equals("0") && rr.get(position).getExtra_item_name().equals("0"))
            {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name());
                selectedPizzaName = rr.get(position).getItem_name();
                holder.tv_menu_item_extra.setVisibility(View.GONE);
            }
            else if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0"))
            {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name() + " (" + rr.get(position).getSize_item_name() + ")");
                selectedPizzaName = rr.get(position).getItem_name();
                holder.tv_menu_item_extra.setVisibility(View.GONE);
            }
            else if (rr.get(position).getSize_item_name().equals("0") && rr.get(position).getExtra_item_name() != "0")
            {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name());
                selectedPizzaName = rr.get(position).getItem_name();

                holder.tv_menu_item_extra.setVisibility(View.VISIBLE);
                String a = rr.get(position).getExtra_item_name().replace("[", "");
                a = a.replace("]", "");
                a = a.replace(",", "+");
                extraItemNameG = a;
                extraItemIDG = rr.get(position).getExtra_item_id();
                System.out.println("==== extraname 1: " + extraItemNameG);
                System.out.println("==== extraname 1: " + extraItemIDG);
                holder.tv_menu_item_extra.setText(a);
            }
            else if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name() != "0")
            {
                holder.tvMenuItemName.setText(rr.get(position).getItem_name() + " (" + rr.get(position).getSize_item_name() + ")");
                selectedPizzaName = rr.get(position).getItem_name();
                holder.tv_menu_item_extra.setVisibility(View.VISIBLE);
                String a = rr.get(position).getExtra_item_name().replace("[", "");
                a = a.replace("]", "");
                a = a.replace(",", "+");
                extraItemNameG = a;
                extraItemIDG = rr.get(position).getExtra_item_id();
                System.out.println("==== extraname 1: " + extraItemNameG);
                System.out.println("==== extraname 1: " + extraItemIDG);
                holder.tv_menu_item_extra.setText(a);
            }

            double pp = Double.parseDouble(rr.get(position).getPrice());

            holder.tvItemPrice.setText(currencySymbol + String.valueOf(pp));
            selectedPizzaItemPrice = holder.tvItemPrice.getText().toString();
            holder.tvQuantity.setText(rr.get(position).getItem_quantity());

            holder.ivPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    String subMenuItemId = rr.get(position).getItem_id();
                    String size_id = rr.get(position).getSize_item_id();
                    SQLiteDatabase db = database.getReadableDatabase();

                    if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'AND size_item_id='" + size_id + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt + 1;
                            price = price * qunt;
                        }
                        database.update_item_size(subMenuItemId, size_id, qunt, price);
                        Log.e("query", "" + subMenuItemId + " " + rr.get(position).getSize_item_id() + " " + qunt + " " + price);
                    } else {
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt + 1;
                            price = price * qunt;
                        }
                        database.update_item(subMenuItemId, qunt, price);
                        Log.e("query", "" + subMenuItemId + " " + rr.get(position).getSize_item_id() + " " + qunt + " " + price);
                    }
                    updateCart();
                }

            });

            holder.ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qunt = 0;
                    double price = 0.0;
                    String subMenuItemId = rr.get(position).getItem_id();
                    String size_id = rr.get(position).getSize_item_id();
                    if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                        SQLiteDatabase db = database.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'AND size_item_id='" + size_id + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt - 1;
                            price = price * qunt;
                            if (qunt == 0) {
                                database.delete_Item_size(rr.get(position).getItem_id(), rr.get(position).getSize_item_id());
                                RestaurantDetails.cart_Item_number = RestaurantDetails.cart_Item_number - 1;
                            } else {
                                database.update_item_size(subMenuItemId, size_id, qunt, price);
                            }
                        }
                    } else {
                        SQLiteDatabase db = database.getReadableDatabase();
                        Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + subMenuItemId + "'", null);
                        if (cursor.moveToNext()) {
                            qunt = Integer.parseInt(cursor.getString(7));
                            price = Double.parseDouble(rr.get(position).getPrice());
                            price = price / qunt;
                            price = (double) Math.round(price * 100) / 100;
                            qunt = qunt - 1;
                            price = price * qunt;
                            if (qunt == 0) {
                                database.delete_Item(rr.get(position).getItem_id());
                                RestaurantDetails.cart_Item_number = RestaurantDetails.cart_Item_number - 1;
                            } else {
                                database.update_item(subMenuItemId, qunt, price);
                            }
                        }
                    }
                    updateCart();
                }
            });
            holder.cartDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rr.get(position).getSize_item_name() != "0" && rr.get(position).getExtra_item_name().equals("0")) {
                        database.delete_Item_size(rr.get(position).getItem_id(), rr.get(position).getSize_item_id());
                        RestaurantDetails.cart_Item_number = RestaurantDetails.cart_Item_number - 1;
                    } else {
                        database.delete_Item_size(rr.get(position).getItem_id(), rr.get(position).getSize_item_id());

                        database.delete_Item(rr.get(position).getItem_id());
                        RestaurantDetails.cart_Item_number = RestaurantDetails.cart_Item_number - 1;

                    }
                    updateCart();
                }
            });
        }

        @Override
        public int getItemCount() {
            return rr.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvMenuItemName, tvItemPrice, tvQuantity, tv_menu_item_extra, ivPlus, ivMinus;
            private ImageView cartDelete;

            public ViewHolder(View itemView) {
                super(itemView);
                tvMenuItemName = itemView.findViewById(R.id.tv_menu_item_name);
                tvItemPrice = itemView.findViewById(R.id.tv_item_price);
                tvQuantity = itemView.findViewById(R.id.tv_quantity);
                tv_menu_item_extra = itemView.findViewById(R.id.tv_menu_item_extra);
                ivPlus = itemView.findViewById(R.id.iv_plus);
                ivMinus = itemView.findViewById(R.id.iv_minus);
                cartDelete = itemView.findViewById(R.id.cart_delete);
            }
        }
    }


    private void getShippingChargeData(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<ShippingCartModel> observable = apiInterface.getServiceCharge(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE, totalPrice.toString(), RestId, postalCode,"");

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShippingCartModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ShippingCartModel searchResult) {
                       deliveryChargeValue = searchResult.getDeliveryChargeValue();
                        SeviceFeesValue = searchResult.getSeviceFeesValue();
                        ServiceFees = searchResult.getServiceFees();
                        ServiceFeesType = searchResult.getServiceFeesType();
                        PackageFeesType = searchResult.getPackageFeesType();
                        PackageFees = searchResult.getPackageFees();
                        PackageFeesValue = searchResult.getPackageFeesValue();
                        SalesTaxAmount = searchResult.getSalesTaxAmount();
                        VatTax = searchResult.getVatTax();


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
