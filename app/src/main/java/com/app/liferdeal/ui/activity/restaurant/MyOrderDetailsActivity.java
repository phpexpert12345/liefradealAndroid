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

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.MYOrderTrackDetailModel;
import com.app.liferdeal.model.restaurant.MyOrderModel;
import com.app.liferdeal.model.restaurant.OrderDetailItem;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_view_ordernumber, txt_view_orderstatus, txt_view_sub_total_price, txt_view_total_price, txt_view_sunmenu,
            txt_pizza_section_cuisine, txt_view_sub_menu, txt_view_sub_menu_one, txt_sub_sub_menu_cuisine, txt_btn_track;

    private PrefsHelper prefsHelper;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private ImageView img_back;
    private String strordernumber="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_details_actvity);
        init();
    }

    private void init()
    {
        try {
            prefsHelper = new PrefsHelper(this);
            progressDialog = new ProgressDialog(this);
            img_back = findViewById(R.id.img_back);

            txt_view_ordernumber = findViewById(R.id.txt_view_ordernumber);
            txt_view_orderstatus = findViewById(R.id.txt_view_orderstatus);
            txt_view_sub_total_price = findViewById(R.id.txt_view_sub_total_price);
            txt_view_total_price = findViewById(R.id.txt_view_total_price);
            txt_view_sunmenu = findViewById(R.id.txt_view_sunmenu);
            txt_pizza_section_cuisine = findViewById(R.id.txt_pizza_section_cuisine);
            txt_view_sub_menu = findViewById(R.id.txt_view_sub_menu);
            txt_view_sub_menu_one = findViewById(R.id.txt_view_sub_menu_one);
            txt_sub_sub_menu_cuisine = findViewById(R.id.txt_sub_sub_menu_cuisine);
            txt_btn_track = findViewById(R.id.txt_btn_track);

            strordernumber = getIntent().getStringExtra("orderid");
            img_back.setOnClickListener(this);
            txt_btn_track.setOnClickListener(this);
            getOrderDetails();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_back:
                finish();
                break;

            case R.id.txt_btn_track:
                Intent i = new Intent(MyOrderDetailsActivity.this, OrderTrackActivity.class);
                i.putExtra("orderid", strordernumber);
                startActivity(i);
                break;

            default:
                break;
        }
    }
private String currency, menuprice, itemname;
    private long quantity;
    private void getOrderDetails(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);

        Observable<MYOrderTrackDetailModel> observable = apiInterface.getMyOrderDetailsDisplay(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,strordernumber);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MYOrderTrackDetailModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MYOrderTrackDetailModel searchResult) {
                        // showProgress();
                       /* setAdapterCategory(searchResult.getOrders().getOrderViewResult());
                        banner_progress.setVisibility(View.GONE);*/
                        String orderno = searchResult.getOrderDetailItem().get(0).getOrderIdentifyno().toString();
                        String orderstatusmsg = searchResult.getOrderDetailItem().get(0).getOrderStatusMsg().toString();
                        String subtotal = searchResult.getOrderDetailItem().get(0).getSubTotal().toString();
                        String orderpricetotal = searchResult.getOrderDetailItem().get(0).getOrderPrice().toString();
                        String restname = searchResult.getOrderDetailItem().get(0).getRestaurantName().toString();

                        currency = searchResult.getOrderDetailItem().get(0).getOrderFoodItem().get(0).getCurrency().toString();
                        menuprice = searchResult.getOrderDetailItem().get(0).getOrderFoodItem().get(0).getMenuprice().toString();
                        itemname = searchResult.getOrderDetailItem().get(0).getOrderFoodItem().get(0).getItemsName().toString();
                        quantity = searchResult.getOrderDetailItem().get(0).getOrderFoodItem().get(0).getQuantity();

                        /*for (int i = 0; i<searchResult.getOrderFoodItem().size(); i++)
                        {
                             currency = searchResult.getOrderFoodItem().get(i).getCurrency().toString();
                             menuprice = searchResult.getOrderFoodItem().get(i).getMenuprice().toString();
                             itemname = searchResult.getOrderFoodItem().get(i).getItemsName().toString();
                             quantity = searchResult.getOrderFoodItem().get(i).getQuantity();
                        }
*/
                        setTextData(orderno, orderstatusmsg, subtotal, orderpricetotal, restname, currency, menuprice, itemname, quantity);
                      //  String orderpricetotal = searchResult.getOrderPrice().toString();

                    }

                    @Override
                    public void onError(Throwable e) {
                        // hideProgress();
                        Log.d("TAG","log...."+e);
                    }

                    @Override
                    public void onComplete() {
                        //   activity.mySharePreferences.setBundle("login");

                    }
                });

    }

    public void showProgress(){
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void hideProgress(){
        progressDialog.dismiss();
    }

    private void setTextData(String orderno, String orderstatusmsg, String subtotal, String orderpricetotal, String restname, String currency, String menuprice, String itemname, long quantity)
    {
        txt_view_ordernumber.setText("Order Id : " + " " + orderno);
        txt_view_orderstatus.setText(orderstatusmsg);
        txt_view_sub_total_price.setText(subtotal);
        txt_view_total_price.setText(orderpricetotal);
        txt_view_sunmenu.setText(itemname + " " + "" + quantity);
    }
}
