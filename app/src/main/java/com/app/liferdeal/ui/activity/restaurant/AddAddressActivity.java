package com.app.liferdeal.ui.activity.restaurant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.SaveAddressModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.util.CommonMethods;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_back;
    private EditText edit_add_tittle,edit_flat_no,edit_flat_name,edit_address,edit_zipcode,edit_city,edit_street_name,edit_mobile;
    private String title,address,zipcode,city,landmark,mobile, flatNo, flatName, streetName;
    private CardView card_add;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private PrefsHelper prefsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_activity);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        rl_back = findViewById(R.id.rl_back);
        edit_add_tittle = findViewById(R.id.edit_add_tittle);
        edit_flat_no = findViewById(R.id.edit_flat_no);
        edit_flat_name = findViewById(R.id.edit_flat_name);
        edit_address = findViewById(R.id.edit_address);
        edit_zipcode = findViewById(R.id.edit_zipcode);
        edit_city = findViewById(R.id.edit_city);
        edit_street_name = findViewById(R.id.edit_street_name);
        edit_mobile = findViewById(R.id.edit_mobile);
        card_add = findViewById(R.id.card_add);

        rl_back.setOnClickListener(this);
        card_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rl_back:
                finish();
                break;
            case R.id.card_add:
                updateProfileValidation();
                break;
            default:
                break;
        }
    }
    public void updateProfileValidation() {

        title = edit_add_tittle.getText().toString().trim();
        flatNo = CommonMethods.getStringDataInbase64(edit_flat_no.getText().toString().trim());
        flatName = CommonMethods.getStringDataInbase64(edit_flat_name.getText().toString().trim());
        address = CommonMethods.getStringDataInbase64(edit_address.getText().toString().trim());
        city = CommonMethods.getStringDataInbase64(edit_city.getText().toString().trim());
        streetName = CommonMethods.getStringDataInbase64(edit_street_name.getText().toString().trim());
        zipcode = edit_zipcode.getText().toString().trim();
        mobile = edit_mobile.getText().toString().trim();


        if (title.isEmpty()) {
            edit_add_tittle.setError("Enter Address Title");
            edit_add_tittle.requestFocus();
        }
        else if (edit_flat_no.getText().toString().trim().isEmpty()) {
            edit_flat_no.setError("Enter FlatNo");
            edit_flat_no.requestFocus();
        }

        else if (edit_flat_name.getText().toString().trim().isEmpty()) {
            edit_flat_name.setError("Enter Flat Name");
            edit_flat_name.requestFocus();
        }

        else if (edit_address.getText().toString().trim().isEmpty()) {
            edit_address.setError("Enter Address");
            edit_address.requestFocus();
        } else if (edit_city.getText().toString().trim().isEmpty()) {
            edit_city.setError("Enter City");
            edit_city.requestFocus();
        } else if (edit_street_name.getText().toString().trim().isEmpty()) {
            edit_street_name.setError("Enter Street Name");
            edit_street_name.requestFocus();
        } else if (zipcode.isEmpty()) {
            edit_zipcode.setError("Enter post code");
            edit_zipcode.requestFocus();

        } else if (mobile.isEmpty()) {
            edit_mobile.setError("Enter mobile");
            edit_mobile.requestFocus();
        } else {
            saveAddress();
        }
    }

    private void saveAddress()
    {
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<SaveAddressModel> observable = apiInterface.saveNewAddress(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,mobile,address,flatNo, streetName, city,zipcode, "",prefsHelper.getPref(Constants.CUSTOMER_ID),flatName);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SaveAddressModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SaveAddressModel searchResult) {
                        showProgress();
                        prefsHelper.savePref(Constants.CUSTOMER_ADDRESS_ID, searchResult.getCustomerAddressId());
                        if (searchResult.getSuccess()==1)
                        {
                            hideProgress();
                            showCustomDialog1decline(searchResult.getSuccessMsg().toString());

                        }
                        //setAdapterCategory(searchResult.getMenuItemExtraGroup());
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
    public void showCustomDialog1decline (String s)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(AddAddressActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(""+s);
        alertDialog.setIcon(R.drawable.ic_place_order_check);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                Intent i = new Intent(AddAddressActivity.this, AddressActivity.class);
                startActivity(i);
                finish();
            }
        });
        alertDialog.show();
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


}
