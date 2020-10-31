package com.app.liferdeal.ui.activity.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.AddressModel;
import com.app.liferdeal.model.restaurant.ModelAddressList;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.adapters.AddressAdapter;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.GPSTracker;
import com.app.liferdeal.util.PrefsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener,AddressAdapter.AddressDelete {
    private RecyclerView list_view_details;
    private RelativeLayout rl_back;
    private CardView card_add_address;
    private List<AddressModel> list_add;
    private TextView tv_name, tv_mobile, tv_address;
    private CardView default_add;
    private ImageView img_select;
    Double currentLatitude, currentLongitude;
    public String current_add;
    private PrefsHelper prefsHelper;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        init();
    }

    private void init() {
        prefsHelper = new PrefsHelper(this);
        list_add = new ArrayList<>();
        list_view_details = findViewById(R.id.list_view_details);
        card_add_address = findViewById(R.id.card_add_address);
        rl_back = findViewById(R.id.rl_back);
        tv_name = findViewById(R.id.tv_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_address = findViewById(R.id.tv_address);
        default_add = findViewById(R.id.default_add);
        img_select = findViewById(R.id.img_select);

        rl_back.setOnClickListener(this);
        card_add_address.setOnClickListener(this);
        default_add.setOnClickListener(this);
        GPSTracker trackerObj = new GPSTracker(this);
        currentLatitude = trackerObj.getLatitude();
        currentLongitude = trackerObj.getLongitude();
        System.out.println("=== prefsHelper.getPref(Constants.CUSTOMER_ID) : " + prefsHelper.getPref(Constants.CUSTOMER_ID) + currentLongitude + currentLatitude);
        getAddress();
        getSavedAddress();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_add_address:
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.default_add:
                img_select.setImageResource(R.drawable.select);
                current_add = tv_address.getText().toString();
                // activity.setCurrent_fragment(new FragmentSelectDate(),FragmentSelectDate.FRAG_TITLE);
                break;

            case R.id.rl_back:
                finish();
                break;

            default:
                break;
        }
    }

    private void getSavedAddress() {
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<ModelAddressList> observable = apiInterface.getSavedAddress(String.valueOf(currentLatitude), String.valueOf(currentLongitude), prefsHelper.getPref(Constants.CUSTOMER_ID));

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelAddressList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ModelAddressList searchResult) {
                        showProgress();


                        setAddAdapter(searchResult.getAddressModel().getDeliveryaddress());
                        hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        Log.d("TAG", "log...." + e);
                    }

                    @Override
                    public void onComplete() {
                        //   activity.mySharePreferences.setBundle("login");

                    }
                });
    }

    private void getAddress() {
        try {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                if (addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    System.out.println("===== addresss " + address);
                      /*  String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();*/
                    tv_address.setText(address);
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.dismiss();

    }

    public void setAddAdapter(List<AddressModel.Deliveryaddress>list){
        list_view_details.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter adapterAddress = new AddressAdapter(this,list);
        list_view_details.setAdapter(adapterAddress);
        adapterAddress.callbackAddress(this);
    }


    @Override
    public void deleteAddress(AddressModel.Deliveryaddress address, String title) {
        if (title.equals("delete")) {
           // deleteAddressCustomer(address.getId());
        } /*else {
            activity.setCurrent_fragment(new FragmentUpdateAddress(), FragmentUpdateAddress.FRAG_TITLE);
            activity.modelAddress = address;
        }*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("===== onnew intent");
    }

    private void deleteAddressCustomer(int id){
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<ModelAddressList> observable = apiInterface.getSavedAddress(String.valueOf(currentLatitude), String.valueOf(currentLongitude), prefsHelper.getPref(Constants.CUSTOMER_ID));

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelAddressList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ModelAddressList searchResult) {
                        showProgress();


                        setAddAdapter(searchResult.getAddressModel().getDeliveryaddress());
                        hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        Log.d("TAG", "log...." + e);
                    }

                    @Override
                    public void onComplete() {
                        //   activity.mySharePreferences.setBundle("login");

                    }
                });
    }
}
