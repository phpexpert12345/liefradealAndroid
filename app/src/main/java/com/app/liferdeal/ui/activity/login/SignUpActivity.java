package com.app.liferdeal.ui.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.liferdeal.R;
import com.app.liferdeal.model.loginsignup.SignupModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.util.CommonMethods;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_fst_name, edt_lst_name, edt_pass, edt_mobile_num, edt_con_pass, edt_email_id;
    private TextView btn_signup, txt_login_view;
    private ApiInterface apiInterface;
    private PrefsHelper prefsHelper;
    private String deviceId="", devicePlateform="";
    private String country, langCode;
    private ProgressDialog progressDialog;
    private ImageView img_logo, img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        init();
    }

    private void init()
    {
        try {
            prefsHelper = new PrefsHelper(this);
            String logo =  prefsHelper.getPref(Constants.APP_LOGO);

            edt_fst_name = findViewById(R.id.edt_fst_name);
            img_logo = findViewById(R.id.img_logo);
            edt_lst_name = findViewById(R.id.edt_lst_name);
            edt_email_id = findViewById(R.id.edt_email_id);
            edt_mobile_num = findViewById(R.id.edt_mobile);
            edt_pass = findViewById(R.id.edt_usr_pass);
            edt_con_pass = findViewById(R.id.edt_con_pass);
            btn_signup = findViewById(R.id.btn_signup);
            txt_login_view = findViewById(R.id. txt_clogin_acc);
            img_back = findViewById(R.id. img_back);

            img_back.setOnClickListener(this);
            btn_signup.setOnClickListener(this);
            txt_login_view.setOnClickListener(this);
            country = "india";
            langCode = "en";
            deviceId = prefsHelper.getPref(Constants.deviceId);
            devicePlateform = prefsHelper.getPref(Constants.devicePlateform);
            if (!logo.equalsIgnoreCase(""))
            {
                Glide.with(SignUpActivity.this).load(Uri.parse(logo)).into(img_logo);

            }
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

            case R.id.btn_signup:

                if (edt_fst_name.equals(""))
                {
                    edt_fst_name.setError("Please enter First name");
                    edt_fst_name.requestFocus();
                }

                if (edt_lst_name.equals(""))
                {
                    edt_lst_name.setError("Please enter Last name");
                    edt_lst_name.requestFocus();
                }

                if (edt_email_id.equals(""))
                {
                    edt_email_id.setError("Please enter email id");
                    edt_email_id.requestFocus();
                }

                else if (!isValidEmailId(edt_email_id.getText().toString().trim())) {
                    edt_email_id.setError("Invalid Email Id");
                    edt_email_id.requestFocus();
                }
                else if (edt_mobile_num.getText().toString().length() == 0) {
                    edt_mobile_num.setError("Phone Number is Empty");
                    edt_mobile_num.requestFocus();
                }else if (!isValidMobile(edt_mobile_num.getText().toString().trim())) {
                    edt_mobile_num.setError("Invalid Phone Number");
                    edt_mobile_num.requestFocus();
                }

                else if (edt_pass.getText().toString().length() == 0) {
                    edt_pass.setError("Password is Empty");
                    edt_pass.requestFocus();
                } else if (edt_con_pass.getText().toString().length() == 0) {
                    edt_con_pass.setError("Confirm Password");
                    edt_con_pass.requestFocus();
                }  else if (!edt_con_pass.getText().toString().trim().equals(edt_pass.getText().toString().trim())) {
                    edt_con_pass.setError("Password not match");
                    edt_con_pass.requestFocus();
                }

                else
                {
                    userRegistration(edt_fst_name.getText().toString().trim(), edt_lst_name.getText().toString().trim(),edt_email_id.getText().toString().trim(),edt_mobile_num.getText().toString().trim(), edt_pass.getText().toString().trim());
                }
                break;

            case R.id.txt_clogin_acc:
                Intent intent  =  new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
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

    private void userRegistration(String fstName, String lstname, String emailid, String mobileno, String pass){
        String ffName = CommonMethods.getStringDataInbase64(fstName);
        String llstname = CommonMethods.getStringDataInbase64(lstname);

        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<SignupModel> observable = apiInterface.registerUser(ffName, llstname, emailid, mobileno, pass, country, "", Constants.API_KEY, langCode ,deviceId,devicePlateform);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignupModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SignupModel signup) {
                        showProgress();
                        if (signup.getSuccess()==1)
                        {
                            String cusomerId = signup.getCustomerId();
                            prefsHelper.savePref(Constants.CUSTOMER_ID, cusomerId);
                            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, signup.getError_msg(), Toast.LENGTH_SHORT).show();
                        }

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
}
