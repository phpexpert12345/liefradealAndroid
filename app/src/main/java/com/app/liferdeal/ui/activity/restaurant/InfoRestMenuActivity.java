package com.app.liferdeal.ui.activity.restaurant;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.DeliveryListInfo;
import com.app.liferdeal.model.restaurant.InfoMenuAcModel;
import com.app.liferdeal.model.restaurant.RestaurantDetailsModel;
import com.app.liferdeal.model.restaurant.RestaurantHoliday;
import com.app.liferdeal.model.restaurant.RestaurantOpeningTime;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.adapters.Info_Delivery_Cost_Adapter;
import com.app.liferdeal.ui.adapters.Restaurant_Details_quick;
import com.app.liferdeal.ui.adapters.SpecialHolidayAdapter;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InfoRestMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_back, rlt_about_img_view, rlt_delivery_time_img_view, rlt_delivery_cost_img_view, rlt_contact_us_img_view, rlt_impresum_img_view, rlt_special_holiday_img_view;
    private RecyclerView info_list_view;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private PrefsHelper prefsHelper;
    private String restId = "";
    private RelativeLayout rlt_about, rlt_delivery_time, rlt_delivery_cost,rlt_contact_us, rlt_impressum, rlt_special_holiday;
    private LinearLayout lnr_text_for_about, lnr_text_for_delivery_time, lnr_text_for_delivery_cost, lnr_text_for_contact_us, lnr_text_for_impressum, lnr_text_for_special_holiday;
    private TextView lnr_txt_view_for_about, lnr_txt_view_for_delivery_time_monday, lnr_txt_view_for_delivery_time_tuesday,lnr_txt_view_for_delivery_time_wednesday,
            lnr_txt_view_for_delivery_time_thursday, lnr_txt_view_for_delivery_time_friday, lnr_txt_view_for_delivery_time_satday,lnr_txt_view_for_delivery_time_sunday;

    private RecyclerView rcv_delivery_cost, rcv_special_holiday;
    private TextView txt_restaurant_name, txt_rest_name_address, txt_legal_repesent_name, txt_legal_contact_no, txt_legal_email_id,
            txt_legal_fax, txt_legal_conmpany_registero, txt_legal_cocompany_register_nom, txt_legal_vat_number, txt_legal_social_sharing_msg,
            txt_view_impressum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        init();
    }

    private void init()
    {
        try {
            prefsHelper = new PrefsHelper(this);
            progressDialog = new ProgressDialog(this);
            img_back = findViewById(R.id.img_back);
            rlt_about = findViewById(R.id.rlt_about);
            lnr_text_for_about = findViewById(R.id.lnr_text_for_about);
            lnr_txt_view_for_about = findViewById(R.id.lnr_txt_view_for_about);
            rlt_about_img_view = findViewById(R.id.rlt_about_img_view);
        /////// FOR DELIVERY TIME/////////////////////
            lnr_txt_view_for_delivery_time_monday = findViewById(R.id.lnr_txt_view_for_delivery_time_monday);
            lnr_txt_view_for_delivery_time_tuesday = findViewById(R.id.lnr_txt_view_for_delivery_time_tuesday);
            lnr_txt_view_for_delivery_time_wednesday = findViewById(R.id.lnr_txt_view_for_delivery_time_wednesday);
            lnr_txt_view_for_delivery_time_thursday = findViewById(R.id.lnr_txt_view_for_delivery_time_thursday);
            lnr_txt_view_for_delivery_time_friday = findViewById(R.id.lnr_txt_view_for_delivery_time_friday);
            lnr_txt_view_for_delivery_time_satday = findViewById(R.id.lnr_txt_view_for_delivery_time_satday);
            lnr_txt_view_for_delivery_time_sunday = findViewById(R.id.lnr_txt_view_for_delivery_time_sunday);
            lnr_text_for_delivery_time = findViewById(R.id.lnr_text_for_delivery_time);
            rlt_delivery_time_img_view = findViewById(R.id.rlt_delivery_time_img_view);
            rlt_delivery_time = findViewById(R.id.rlt_delivery_time);

            /////// FOR DELIVERY COST////////////////
            rlt_delivery_cost = findViewById(R.id.rlt_delivery_cost);
            lnr_text_for_delivery_cost = findViewById(R.id.lnr_text_for_delivery_cost);
            rcv_delivery_cost = findViewById(R.id.rcv_delivery_cost);
            rlt_delivery_cost_img_view = findViewById(R.id.rlt_delivery_cost_img_view);
          /*  LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rcv_delivery_cost.setLayoutManager(horizontalLayoutManager2);*/
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InfoRestMenuActivity.this);
            rcv_delivery_cost.setLayoutManager(mLayoutManager);
            rcv_delivery_cost.setItemAnimator(new DefaultItemAnimator());
            //////////////// FOR CONTACT US /////////////////////////

            rlt_contact_us_img_view = findViewById(R.id.rlt_contact_us_img_view);
            rlt_contact_us = findViewById(R.id.rlt_contact_us);
            lnr_text_for_contact_us = findViewById(R.id.lnr_text_for_contact_us);
            txt_restaurant_name = findViewById(R.id.txt_restaurant_name);
            txt_rest_name_address = findViewById(R.id.txt_rest_name_address);
            txt_legal_repesent_name = findViewById(R.id.txt_legal_repesent_name);
            txt_legal_contact_no = findViewById(R.id.txt_legal_contact_no);
            txt_legal_email_id = findViewById(R.id.txt_legal_email_id);
            txt_legal_fax = findViewById(R.id.txt_legal_fax);
            txt_legal_conmpany_registero = findViewById(R.id.txt_legal_conmpany_registero);
            txt_legal_cocompany_register_nom = findViewById(R.id.txt_legal_cocompany_register_nom);
            txt_legal_vat_number = findViewById(R.id.txt_legal_vat_number);
            txt_legal_social_sharing_msg = findViewById(R.id.txt_legal_social_sharing_msg);

            //////////// FOR IMRESSUM ////////////////////////
            rlt_impressum = findViewById(R.id.rlt_impressum);
            rlt_impresum_img_view = findViewById(R.id.rlt_impresum_img_view);
            lnr_text_for_impressum = findViewById(R.id.lnr_text_for_impressum);
            txt_view_impressum = findViewById(R.id.txt_view_impressum);

            /////////////////////// FOR SPECIAL HOLIDAY //////////////////////////////

            rlt_special_holiday = findViewById(R.id.rlt_special_holiday);
            rlt_special_holiday_img_view = findViewById(R.id.rlt_special_holiday_img_view);
            lnr_text_for_special_holiday = findViewById(R.id.lnr_text_for_special_holiday);
            rcv_special_holiday = findViewById(R.id.rcv_special_holiday);
            RecyclerView.LayoutManager mLayoutManagerr = new LinearLayoutManager(InfoRestMenuActivity.this);
            rcv_special_holiday.setLayoutManager(mLayoutManagerr);
            rcv_special_holiday.setItemAnimator(new DefaultItemAnimator());


           restId = getIntent().getStringExtra("clickRestId");
            img_back.setOnClickListener(this);
            rlt_about.setOnClickListener(this);
            rlt_delivery_time.setOnClickListener(this);
            rlt_delivery_cost.setOnClickListener(this);
            rlt_contact_us.setOnClickListener(this);
            rlt_impressum.setOnClickListener(this);
            rlt_special_holiday.setOnClickListener(this);

            getRestSearchDetailsData();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    int rltabout=0;
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_back:
                finish();
                break;

            case R.id.rlt_about:
                if(lnr_text_for_about.getVisibility()== View.VISIBLE)
                    {
                        System.out.println("==== visible ");
                        lnr_text_for_about.setVisibility(View.GONE);
                        rlt_about_img_view.setBackgroundResource(R.drawable.plusbtn);
                    }
                else
                {
                    System.out.println("==== not visible ");
                    lnr_text_for_about.setVisibility(View.VISIBLE);
                    lnr_text_for_about.setBackgroundColor(getResources().getColor(R.color.gray));
                    rlt_about_img_view.setBackgroundResource(R.drawable.minusbtn);
                }
                break;

            case R.id.rlt_delivery_time:
                if(lnr_text_for_delivery_time.getVisibility()== View.VISIBLE)
                {
                    System.out.println("==== visible ");
                    lnr_text_for_delivery_time.setVisibility(View.GONE);
                    rlt_delivery_time_img_view.setBackgroundResource(R.drawable.plusbtn);
                }
                else
                {
                    System.out.println("==== not visible ");
                    lnr_text_for_delivery_time.setVisibility(View.VISIBLE);
                    lnr_text_for_delivery_time.setBackgroundColor(getResources().getColor(R.color.gray));
                    rlt_delivery_time_img_view.setBackgroundResource(R.drawable.minusbtn);
                }
                break;

            case R.id.rlt_delivery_cost:
                if(lnr_text_for_delivery_cost.getVisibility()== View.VISIBLE)
                {
                    System.out.println("==== visible ");
                    lnr_text_for_delivery_cost.setVisibility(View.GONE);
                    rlt_delivery_cost_img_view.setBackgroundResource(R.drawable.plusbtn);
                }
                else
                {
                    System.out.println("==== not visible ");
                    lnr_text_for_delivery_cost.setVisibility(View.VISIBLE);
                    lnr_text_for_delivery_cost.setBackgroundColor(getResources().getColor(R.color.gray));
                    rlt_delivery_cost_img_view.setBackgroundResource(R.drawable.minusbtn);
                }

                break;

            case R.id.rlt_contact_us:
                if(lnr_text_for_contact_us.getVisibility()== View.VISIBLE)
                {
                    System.out.println("==== visible ");
                    lnr_text_for_contact_us.setVisibility(View.GONE);
                    rlt_contact_us_img_view.setBackgroundResource(R.drawable.plusbtn);
                }
                else
                {
                    System.out.println("==== not visible ");
                    lnr_text_for_contact_us.setVisibility(View.VISIBLE);
                    lnr_text_for_contact_us.setBackgroundColor(getResources().getColor(R.color.gray));
                    rlt_contact_us_img_view.setBackgroundResource(R.drawable.minusbtn);
                }

                break;

            case R.id.rlt_impressum:
                if(lnr_text_for_impressum.getVisibility()== View.VISIBLE)
                {
                    System.out.println("==== visible ");
                    lnr_text_for_impressum.setVisibility(View.GONE);
                    rlt_impresum_img_view.setBackgroundResource(R.drawable.plusbtn);
                }
                else
                {
                    System.out.println("==== not visible ");
                    lnr_text_for_impressum.setVisibility(View.VISIBLE);
                    lnr_text_for_impressum.setBackgroundColor(getResources().getColor(R.color.gray));
                    rlt_impresum_img_view.setBackgroundResource(R.drawable.minusbtn);
                }

                break;

            case R.id.rlt_special_holiday:
                if(lnr_text_for_special_holiday.getVisibility()== View.VISIBLE)
                {
                    System.out.println("==== visible ");
                    lnr_text_for_special_holiday.setVisibility(View.GONE);
                    rlt_special_holiday_img_view.setBackgroundResource(R.drawable.plusbtn);
                }
                else
                {
                    System.out.println("==== not visible ");
                    lnr_text_for_special_holiday.setVisibility(View.VISIBLE);
                    lnr_text_for_special_holiday.setBackgroundColor(getResources().getColor(R.color.gray));
                    rlt_special_holiday_img_view.setBackgroundResource(R.drawable.minusbtn);
                }

                break;

            default:
                break;
        }
    }

    private void getRestSearchDetailsData(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<InfoMenuAcModel> observable = apiInterface.getRestMenuInfoButtondata(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,restId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InfoMenuAcModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(InfoMenuAcModel searchResult) {
                        //showProgress();

                        setDataInExpandList(searchResult);

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
    String restAbout="";
   private List<RestaurantOpeningTime> deliveryListInfoList;
   private List<DeliveryListInfo> deliveryListCostInfoList;
   private List<RestaurantHoliday> restaurantHolidaysList;
    private void setDataInExpandList(InfoMenuAcModel searchResult)
    {
        try {
             restAbout = searchResult.getRestaurantInfo().get(0).getRestaurantAbout();
            lnr_txt_view_for_about.setText(restAbout);
            deliveryListInfoList = searchResult.getRestaurantInfo().get(0).getRestaurantOpeningTime();
            System.out.println("==== deliveryListInfoList : " + deliveryListInfoList.size());
            if (deliveryListInfoList.size()>0)
            {
                rlt_delivery_time.setVisibility(View.VISIBLE);
                for(int i=0; i<deliveryListInfoList.size(); i++) {
                    lnr_txt_view_for_delivery_time_monday.setText(deliveryListInfoList.get(i).getMondayText());
                    lnr_txt_view_for_delivery_time_tuesday.setText(deliveryListInfoList.get(i).getTuesdayText());
                    lnr_txt_view_for_delivery_time_wednesday.setText(deliveryListInfoList.get(i).getWednesdayText());
                    lnr_txt_view_for_delivery_time_thursday.setText(deliveryListInfoList.get(i).getThursdayText());
                    lnr_txt_view_for_delivery_time_friday.setText(deliveryListInfoList.get(i).getFridayText());
                    lnr_txt_view_for_delivery_time_satday.setText(deliveryListInfoList.get(i).getSaturdayText());
                    lnr_txt_view_for_delivery_time_sunday.setText(deliveryListInfoList.get(i).getSundayText());
                }
            }
            else
            {
                rlt_delivery_time.setVisibility(View.GONE);
            }

            ///////// FOR DELIVERY COST /////////////////////

            deliveryListCostInfoList = searchResult.getRestaurantInfo().get(0).getDeliveryListInfo();
            System.out.println("==== deliveryListCostInfoList : " + deliveryListCostInfoList.size());

            if (deliveryListCostInfoList.size()>0)
            {
                rlt_delivery_cost.setVisibility(View.VISIBLE);
                Info_Delivery_Cost_Adapter adapterCategory = new Info_Delivery_Cost_Adapter(InfoRestMenuActivity.this, deliveryListCostInfoList);
                rcv_delivery_cost.setAdapter(adapterCategory);
            }
            else
            {
                rlt_delivery_cost.setVisibility(View.GONE);
            }


            /////////////// FOR CONTACT US////////////////////////////

            txt_restaurant_name.setText(searchResult.getRestaurantInfo().get(0).getRestaurantName());
            txt_rest_name_address.setText(searchResult.getRestaurantInfo().get(0).getRestaurantAddress());
            txt_legal_repesent_name.setText(searchResult.getRestaurantInfo().get(0).getLegalRepresentative());
            txt_legal_contact_no.setText(searchResult.getRestaurantInfo().get(0).getRestaurantContactMobile());
            txt_legal_email_id.setText(searchResult.getRestaurantInfo().get(0).getEmail());
            txt_legal_fax.setText(searchResult.getRestaurantInfo().get(0).getFax());
            txt_legal_conmpany_registero.setText(searchResult.getRestaurantInfo().get(0).getCompanyRegister());
            txt_legal_cocompany_register_nom.setText(searchResult.getRestaurantInfo().get(0).getCompanyRegisterNo());
            txt_legal_vat_number.setText(searchResult.getRestaurantInfo().get(0).getVATNumber());
            txt_legal_social_sharing_msg.setText(searchResult.getRestaurantInfo().get(0).getSocialSharingMessage());


            //////////////////// FOR IMPRESSUM ////////////////////////////////////

            txt_view_impressum.setText(searchResult.getRestaurantInfo().get(0).getRestaurantImpression());

            ///////// FOR Special Holiday /////////////////////

            restaurantHolidaysList = searchResult.getRestaurantInfo().get(0).getRestaurantHoliday();
            System.out.println("==== restaurantHolidaysList : " + restaurantHolidaysList.size());

            if (restaurantHolidaysList.size()>0)
            {
                rlt_special_holiday.setVisibility(View.VISIBLE);
                SpecialHolidayAdapter adapterCategory = new SpecialHolidayAdapter(InfoRestMenuActivity.this, restaurantHolidaysList);
                rcv_special_holiday.setAdapter(adapterCategory);
            }
            else
            {
                rlt_special_holiday.setVisibility(View.GONE);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
