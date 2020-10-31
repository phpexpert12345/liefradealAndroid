package com.app.liferdeal.ui.activity.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.FoodItemSizeModel;
import com.app.liferdeal.model.restaurant.RestMenuCat;
import com.app.liferdeal.model.restaurant.RestaurantDetailsModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.adapters.RestaurantDetailsAdapter;
import com.app.liferdeal.ui.adapters.RestaurantFoodItemSizeAdapter;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddClickDetails extends AppCompatActivity implements View.OnClickListener, RestaurantFoodItemSizeAdapter.RestaurantFoodSizeItemInterface {
    private int clickItemtId;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private PrefsHelper prefsHelper;
    private TextView btn_add_extra,btn_add_to_cart,txt_view_name, txt_currect_symbol,txt_view_cusine_name,txt_total;
    private int globItemId, globItemSizeId;
    private String globExtraAvail, CLICKITEMNAME, selectFoodSizeName, ClickPizzdesc, CLICKITEMPRICE, mainClickRestId, mainClickRestName, sizePizzaPrice;
    private RecyclerView food_item_size_list_rcv;
    private ImageView img_back;
    private String currencySymbol="";
    private double totalPriceAdd=0.0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_size);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        food_item_size_list_rcv = findViewById(R.id.food_item_size_list_rcv);
        btn_add_extra = findViewById(R.id.btn_add_extra);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        txt_view_name = findViewById(R.id.txt_view_name);
        txt_currect_symbol = findViewById(R.id.txt_currect_symbol);
        txt_view_cusine_name = findViewById(R.id.txt_view_cusine_name);
        txt_total = findViewById(R.id.txt_total);
        img_back = findViewById(R.id.img_back);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddClickDetails.this);
        food_item_size_list_rcv.setLayoutManager(mLayoutManager);
        food_item_size_list_rcv.setItemAnimator(new DefaultItemAnimator());

        clickItemtId = getIntent().getIntExtra("CLICKITEMID",0);
        CLICKITEMNAME = getIntent().getStringExtra("CLICKITEMNAME");
        CLICKITEMPRICE = getIntent().getStringExtra("CLICKITEMPRICE");
        ClickPizzdesc = getIntent().getStringExtra("CLICKPIZZDESC");
        mainClickRestId = getIntent().getStringExtra("mainClickRestId");
        mainClickRestName = getIntent().getStringExtra("mainClickRestName");
        System.out.println("=== clickItemtId : " + clickItemtId + "CLICKITEMNAME" + CLICKITEMNAME + "CLICKITEMPRICE" + CLICKITEMPRICE + "mainClickRestId" + mainClickRestId + "mainClickRestName" + mainClickRestName);
        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
        currencySymbol =  hh.getSymbol();
        txt_view_name.setText(CLICKITEMNAME);
        txt_view_cusine_name.setText(ClickPizzdesc);
        txt_currect_symbol.setText("" +prefsHelper.getPref(Constants.APP_CURRENCY));
        txt_total.setText(currencySymbol + " " + "" + CLICKITEMPRICE);
        btn_add_to_cart.setOnClickListener(this);
        btn_add_extra.setOnClickListener(this);
        img_back.setOnClickListener(this);
        getItemSizeData(clickItemtId);
    }

    private void getItemSizeData(int clickItemtId)
    {
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<FoodItemSizeModel> observable = apiInterface.getFoodItemSize(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,clickItemtId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FoodItemSizeModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodItemSizeModel searchResult) {
                        showProgress();
                        setAdapterCategory(searchResult.getRestaurantMenItemsSize());
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void hideProgress(){
        progressDialog.dismiss();
    }

    private void setAdapterCategory(List<FoodItemSizeModel.RestaurantMenItemsSize> list){
        RestaurantFoodItemSizeAdapter adapterCategory = new RestaurantFoodItemSizeAdapter(AddClickDetails.this,list, AddClickDetails.this);
        food_item_size_list_rcv.setAdapter(adapterCategory);


        hideProgress();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_add_extra:
                Intent i = new Intent(AddClickDetails.this, AddExtraActivity.class);
                i.putExtra("ITEMID", globItemId);
                i.putExtra("FOODITEMSIZEID", globItemSizeId);
                i.putExtra("selectFoodItemName", selectFoodSizeName);
                i.putExtra("sizePizzaPrice", sizePizzaPrice);
                i.putExtra("mainClickRestId", mainClickRestId);
                i.putExtra("mainClickRestName", mainClickRestName);
                i.putExtra("SUBCATCLICKITEMID", clickItemtId);
                i.putExtra("SUBCATCLICKITEMNAME", CLICKITEMNAME);
                i.putExtra("SUBCATCLICKITEMPRICE", CLICKITEMPRICE);
                i.putExtra("SUBCATCLICKITEMDesc", ClickPizzdesc);
                i.putExtra("TotalPriceWithPizzaItemAndSize", ""+totalPriceAdd);
                startActivity(i);
                break;
            case R.id.btn_add_to_cart:
                break;

            case R.id.img_back:
                finish();
                break;

            default:
                break;
        }
    }
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    @Override
    public void getrvcheckeddata(int itemId, int itemSizeId, String extraAvail, String selectFoodItemName, String sizePizzaPricee) {
        System.out.println("==== checked in clickdetails : " + itemId + " " + "sizeid : " + itemSizeId + " " + "extraavail : " + extraAvail + " " + "fooditemname" + selectFoodItemName + " sizePizzaPrice " + sizePizzaPricee);

        globItemId = itemId;
        globItemSizeId = itemSizeId;
        globExtraAvail = extraAvail;
        selectFoodSizeName = selectFoodItemName;
        sizePizzaPrice = sizePizzaPricee;

         totalPriceAdd = Double.parseDouble(CLICKITEMPRICE)+Double.parseDouble(sizePizzaPrice);
        System.out.println("==== totalprice Add : " + totalPriceAdd);


        txt_total.setText(currencySymbol + " " + "" + ""+String.format("%.2f",totalPriceAdd));
        if (extraAvail.equalsIgnoreCase("yes"))
        {
            btn_add_extra.setVisibility(View.VISIBLE);
            btn_add_to_cart.setVisibility(View.GONE);
        }
        else
        {
            btn_add_to_cart.setVisibility(View.VISIBLE);
            btn_add_extra.setVisibility(View.GONE);
        }
    }
}
