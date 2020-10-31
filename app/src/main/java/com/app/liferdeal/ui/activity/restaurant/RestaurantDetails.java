package com.app.liferdeal.ui.activity.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.Restaurant;
import com.app.liferdeal.model.restaurant.RestDetailClickFoodModel;
import com.app.liferdeal.model.restaurant.RestMenuCat;
import com.app.liferdeal.model.restaurant.RestaurantDetailsModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.Database.Database;
import com.app.liferdeal.ui.activity.cart.CartActivity;
import com.app.liferdeal.ui.adapters.RestaurantDetailsAdapter;
import com.app.liferdeal.ui.adapters.Restaurant_Details_quick;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantDetails extends AppCompatActivity implements View.OnClickListener, Restaurant_Details_quick.RestaurantDetailsQuickInterface, RestaurantDetailsAdapter.RestaurantDetailsAdapterInterface
{
    private PrefsHelper prefsHelper;
    private ImageView img_back,shop_img_places,rset_logo, shop_img_places_cat, img_restmenu_info, img_discount_btn;
    private RecyclerView rcv_rest_details_list, quickrecycler;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private String clickRestId, clickRestName, RESTCOVER, RESTLOGO, RESTADDRESS, RESTISOPEN, SIZEITEMID,tempExtraItemidWithSizeIdd, globTempExtraItemidWithSizeIdd, getRatingValue;
    public static TextView tvMenuItemName, tvCartItemCount, txt_title, tv_cart_item_countt;
    private RelativeLayout rl_cart, rl_cartt;
    private LinearLayout img_view_gallery, lnr_rest_menyu_details, lnr_bookatable, lnr_view_rating;
    private Database database;
    public static int cart_Item_number;
    private ProgressBar banner_progress;
    private TextView shop_image_place_text,tv_item_discount_cost,txt_rest_address, txt_rest_name, tv_restaurant_rating_value;
    private int subPizzaItemId;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);
        init();
    }

    private void init()
    {
        try {
            prefsHelper = new PrefsHelper(this);
            progressDialog = new ProgressDialog(this);
            img_back = findViewById(R.id.img_back);
            database = new Database(RestaurantDetails.this);
            rcv_rest_details_list = findViewById(R.id.rcv_rest_details_list);
            tvCartItemCount = findViewById(R.id.tv_cart_item_count);
            tv_cart_item_countt = findViewById(R.id.tv_cart_item_countt);
            shop_image_place_text = findViewById(R.id.shop_image_place_text);
            tv_item_discount_cost = findViewById(R.id.tv_item_discount_cost);
            shop_img_places = findViewById(R.id.shop_img_places);
            rset_logo = findViewById(R.id.rset_logo);
            txt_rest_address = findViewById(R.id.txt_rest_address);
            banner_progress =  findViewById(R.id.banner_progress);
            rl_cart = findViewById(R.id.cart_count_layout);
            rl_cartt = findViewById(R.id.rl_cart);
            txt_rest_name = findViewById(R.id.txt_rest_name);
            quickrecycler = findViewById(R.id.quickrecycler);
            img_view_gallery = findViewById(R.id.img_view_gallery);
            lnr_rest_menyu_details = findViewById(R.id.lnr_rest_menyu_details);
            lnr_bookatable = findViewById(R.id.lnr_bookatable);
            shop_img_places_cat = findViewById(R.id.shop_img_places_cat);
            lnr_view_rating = findViewById(R.id.lnr_view_rating);
            img_restmenu_info = findViewById(R.id.img_restmenu_info);
            img_discount_btn = findViewById(R.id.img_discount_btn);
            ratingBar = findViewById(R.id.ratingBar);
            tv_restaurant_rating_value = findViewById(R.id.tv_restaurant_rating_value);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RestaurantDetails.this);
            rcv_rest_details_list.setLayoutManager(mLayoutManager);
            rcv_rest_details_list.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            quickrecycler.setLayoutManager(horizontalLayoutManager2);
            img_back.setOnClickListener(this);
            rl_cart.setOnClickListener(this);
            rl_cartt.setOnClickListener(this);
            img_view_gallery.setOnClickListener(this);
            lnr_rest_menyu_details.setOnClickListener(this);
            lnr_bookatable.setOnClickListener(this);
            lnr_view_rating.setOnClickListener(this);
            img_restmenu_info.setOnClickListener(this);
            img_discount_btn.setOnClickListener(this);

            clickRestId = getIntent().getStringExtra("RESTID");
            clickRestName = getIntent().getStringExtra("RESTName");
            RESTCOVER = getIntent().getStringExtra("RESTCOVER");
            RESTLOGO = getIntent().getStringExtra("RESTLOGO");
            getRatingValue = getIntent().getStringExtra("RATINGBARDATA");
            RESTADDRESS = getIntent().getStringExtra("RESTADDRESS");
            RESTISOPEN = getIntent().getStringExtra("RESTISOPEN");
            SIZEITEMID = getIntent().getStringExtra("SIZEITEMID");
            tempExtraItemidWithSizeIdd = getIntent().getStringExtra("tempExtraItemidWithSizeIdd");
            System.out.println("==== tempExtraItemidWithSizeIdd : " + tempExtraItemidWithSizeIdd);
            if (tempExtraItemidWithSizeIdd!=null) {
                globTempExtraItemidWithSizeIdd = tempExtraItemidWithSizeIdd.replace("[", "");
                globTempExtraItemidWithSizeIdd = globTempExtraItemidWithSizeIdd.replace("]", "");
                System.out.println("==== globTempExtraItemidWithSizeIdd : " + globTempExtraItemidWithSizeIdd);
            }

            txt_rest_name.setText(clickRestName);
            System.out.println("=== clickRestId : " + clickRestId + " " + "clickRestName" + clickRestName + " SIZEITEMID " + SIZEITEMID);
            shop_image_place_text.setText(clickRestName);
            tv_item_discount_cost.setText(RESTISOPEN);
            txt_rest_address.setText(RESTADDRESS);
            System.out.println("====== rating value : " + getRatingValue);
            if (!getRatingValue.equalsIgnoreCase(""))
            {
                ratingBar.setRating(Float.parseFloat(getRatingValue));
            }

            tv_restaurant_rating_value.setText("(" + getRatingValue + ")");
            Glide.with(this).load(Uri.parse(RESTCOVER)).into(shop_img_places);
            Glide.with(this).load(Uri.parse(RESTLOGO)).into(rset_logo);

            getRestSearchDetailsData();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (AddExtraActivity.cart_Item_number!=0)
        {
            // rl_cart.setVisibility(View.VISIBLE);
            RestaurantDetails.tvCartItemCount.setText("" + AddExtraActivity.cart_Item_number);

            rl_cartt.setVisibility(View.VISIBLE);
            RestaurantDetails.tv_cart_item_countt.setText("" + AddExtraActivity.cart_Item_number);
        }

    }

    private void setAdapterCategory(List<RestMenuCat> list){
        RestaurantDetailsAdapter adapterCategory = new RestaurantDetailsAdapter(RestaurantDetails.this,list, list.get(0).getSubItemsRecord(), RestaurantDetails.this, clickRestId,clickRestName, globcategoryImage);
        rcv_rest_details_list.setAdapter(adapterCategory);
        adapterCategory.notifyDataSetChanged();
        hideProgress();

    /*    rcv_rest_details_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                getClickFoddMenuData(globRestId, globSelectedCatId, globcategoryImage);
            }
        });*/
    }


    private void setAdapterCategoryForQuick(List<RestaurantDetailsModel.RestaurantMencategory> list){
        Restaurant_Details_quick adapterCategory = new Restaurant_Details_quick(RestaurantDetails.this,list, RestaurantDetails.this);
        quickrecycler.setAdapter(adapterCategory);
        // adapterCategory.notifyDataSetChanged();
        hideProgress();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_back:
                finish();
                break;

            case R.id.img_discount_btn:
                Intent discount = new Intent(RestaurantDetails.this, DiscountOrderActivity.class);
                discount.putExtra("clickRestId", clickRestId);

                startActivity(discount);
                break;

            case R.id.img_restmenu_info:
                Intent info = new Intent(RestaurantDetails.this, InfoRestMenuActivity.class);
                info.putExtra("clickRestId", clickRestId);
                startActivity(info);
                break;

            case R.id.lnr_view_rating:
                Intent writerating = new Intent(RestaurantDetails.this, RestMenuReviewActivity.class);
                writerating.putExtra("clickRestId", clickRestId);
                writerating.putExtra("RESTName", clickRestName);
                writerating.putExtra("RESTCOVER", RESTCOVER);
                writerating.putExtra("RESTLOGO", RESTLOGO);
                writerating.putExtra("RESTISOPEN", RESTISOPEN);
                writerating.putExtra("RATINGVAL", getRatingValue);

                startActivity(writerating);
                break;

            case R.id.img_view_gallery:
                Intent i = new Intent(RestaurantDetails.this, RestaurantPhotoGallery.class);
                i.putExtra("clickRestId", clickRestId);
                startActivity(i);
                break;
            case R.id.lnr_rest_menyu_details:
                Intent ii = new Intent(RestaurantDetails.this, RestaurantMenuPhotoGallery.class);
                ii.putExtra("clickRestId", clickRestId);
                startActivity(ii);
                break;

            case R.id.lnr_bookatable:
                Intent booktable = new Intent(RestaurantDetails.this, RestaurantBookTable.class);
                booktable.putExtra("clickRestId", clickRestId);
                startActivity(booktable);
                break;

            case R.id.cart_count_layout:
                SQLiteDatabase db = database.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM item_table", null);
                //    Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
                if (cursor.moveToNext()) {
                    Intent intent = new Intent(RestaurantDetails.this, CartActivity.class);
                    intent.putExtra("RESTID", clickRestId);
                    intent.putExtra("RESTName", clickRestName);
                    intent.putExtra("RESTLOGO", RESTLOGO);
                    intent.putExtra("subPizzaItemId", ""+subPizzaItemId);
                    intent.putExtra("SIZEITEMID", ""+SIZEITEMID);
                    intent.putExtra("globTempExtraItemidWithSizeIdd", ""+globTempExtraItemidWithSizeIdd);
                    startActivity(intent);
                } else {
                    /// showCustomDialog1decline("Cart is empty,please add item in cart.");
                }
                break;
            case R.id.rl_cart:
                SQLiteDatabase dbb = database.getReadableDatabase();
                Cursor cursorr = dbb.rawQuery("SELECT * FROM item_table", null);
                //    Cursor cursor1 = db.rawQuery("SELECT * FROM deal_item_table", null);
                if (cursorr.moveToNext()) {
                    Intent intent = new Intent(RestaurantDetails.this, CartActivity.class);
                    intent.putExtra("RESTID", clickRestId);
                    intent.putExtra("RESTName", clickRestName);
                    intent.putExtra("RESTLOGO", RESTLOGO);
                    intent.putExtra("subPizzaItemId", ""+subPizzaItemId);
                    intent.putExtra("SIZEITEMID", ""+SIZEITEMID);
                    intent.putExtra("globTempExtraItemidWithSizeIdd", ""+globTempExtraItemidWithSizeIdd);
                    startActivity(intent);
                } else {
                    /// showCustomDialog1decline("Cart is empty,please add item in cart.");
                }
                break;


            default:
                break;
        }
    }

    private void getRestSearchDetailsData(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<RestaurantDetailsModel> observable = apiInterface.getSearchRestDetailsData(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,clickRestId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestaurantDetailsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RestaurantDetailsModel searchResult) {
                        showProgress();
                        //  setAdapterCategory(searchResult.getRestaurantMencategory());
                        if (searchResult.getRestaurantMencategory().get(0).getError()==1)
                        {

                            Toast.makeText(RestaurantDetails.this, "There is no Details", Toast.LENGTH_SHORT).show();
                            hideProgress();
                        }
                        else
                        {
                            setAdapterCategoryForQuick(searchResult.getRestaurantMencategory());                        }

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

    private String globcategoryImage="", globRestId="";
    private int globSelectedCatId;
    @Override
    public void getRestaaurantQuickClickData(String restId, int selectedCatId, String categoryImage) {
        System.out.println("===== slected cat id : " + selectedCatId + "restId : " + restId + "categoryImage " + categoryImage);
        globRestId = restId;
        globSelectedCatId = selectedCatId;
        globcategoryImage = categoryImage;
        getClickFoddMenuData(restId, selectedCatId, categoryImage);
        if (!globcategoryImage.equalsIgnoreCase("")) {
            Glide.with(this).load(Uri.parse(globcategoryImage)).into(shop_img_places_cat);
        }
    }

    private void getClickFoddMenuData(String restId, int catid, String categoryImage){

        banner_progress.setVisibility(View.VISIBLE);
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<RestDetailClickFoodModel> observable = apiInterface.getClockFoodMenu(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,restId, catid);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestDetailClickFoodModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RestDetailClickFoodModel searchResult) {
                        showProgress();
                        setAdapterCategory(searchResult.getMenuCat());
                        banner_progress.setVisibility(View.GONE);
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

    @Override
    public void getClickData(int itemId, String itemName) {
        System.out.println("===== item name : " + itemId);
        subPizzaItemId = itemId;
        System.out.println("===== subPizzaItemId name : " + subPizzaItemId);
        prefsHelper.savePref(Constants.Rest_DETAIL_CLICK_ITEM_ID,itemId);
        prefsHelper.savePref(Constants.Rest_DETAIL_CLICK_ITEM_NAME, itemName);
    }
}
