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
import androidx.fragment.app.FragmentTransaction;

import com.app.liferdeal.R;
import com.app.liferdeal.model.loginsignup.SignInModel;
import com.app.liferdeal.model.loginsignup.SignupModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.activity.MainActivity;
import com.app.liferdeal.ui.activity.splash.SplashActivity;
import com.app.liferdeal.ui.fragment.LocationMapFragment;
import com.app.liferdeal.ui.fragment.restaurant.RestaurantMain;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edt_usrName, edt_usrPass;
    private TextView btn_login, txt_forgot_pass, txt_create_new;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private PrefsHelper prefsHelper;
    private String langCode, deviceId, devicePlateform;
    private ImageView img_logo, img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment_layout);
        init();
    }

    private void init()
    {
        try {
            prefsHelper = new PrefsHelper(this);
            String logo =  prefsHelper.getPref(Constants.APP_LOGO);
            img_logo = findViewById(R.id.img_logo);
            edt_usrName = findViewById(R.id.edt_usr_name);
            edt_usrPass = findViewById(R.id.edt_usr_pass);
            txt_forgot_pass = findViewById(R.id.txt_forgot_pass);
            txt_create_new = findViewById(R.id.txt_create_acc);
            btn_login = findViewById(R.id.btn_login);
            img_back = findViewById(R.id.img_back);

            btn_login.setOnClickListener(this);
            img_back.setOnClickListener(this);
            txt_create_new.setOnClickListener(this);
            langCode = "en";
            deviceId = prefsHelper.getPref(Constants.deviceId);
            devicePlateform = prefsHelper.getPref(Constants.devicePlateform);
            if (!logo.equalsIgnoreCase(""))
            {
                Glide.with(SignInActivity.this).load(Uri.parse(logo)).into(img_logo);

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

            case R.id.btn_login:
                if (edt_usrName.equals(""))
                {
                    edt_usrName.setError("Please enter email id");
                    edt_usrName.requestFocus();
                }

                else if (!isValidEmailId(edt_usrName.getText().toString().trim())) {
                    edt_usrName.setError("Invalid Email Id");
                    edt_usrName.requestFocus();
                }
                else if (edt_usrPass.getText().toString().length() == 0) {
                    edt_usrPass.setError("Password is Empty");
                    edt_usrPass.requestFocus();
                }
                else
                {
                    userLogin(edt_usrName.getText().toString().trim(), edt_usrPass.getText().toString().trim());
                }
                break;

            case R.id.txt_create_acc:
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.txt_forgot_pass:
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

    private void userLogin(String emailid,String pass){
        showProgress();
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<SignInModel> observable = apiInterface.loginUser(emailid, pass, Constants.API_KEY, langCode ,deviceId,devicePlateform);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignInModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SignInModel signin) {

                        if (signin.getSuccess()==0)
                        {
                            String cusomerId = signin.getCustomerId();
                            String username = signin.getUserName();
                            String mobilenum = signin.getUserPhone();
                            String usernemail = signin.getUserEmail();
                           String refercode =  signin.getReferralCode();
                           String refercodeMessage =  signin.getReferralCodeMessage().toString();
                           String referSharingMessage =  signin.getReferralSharingMessage();
                           String referralEarnPoints =  signin.getReferralEarnPoints();
                           String referralJoinFriends =  signin.getReferralJoinFriends();
                            System.out.println("===== strReferCode : " + refercode + "refercodeMessage : " + refercodeMessage + "referSharingMessage :" + referSharingMessage + "referralEarnPoints : " + referralEarnPoints + "referralJoinFriends : " + referralJoinFriends);

                            prefsHelper.savePref(Constants.USER_NAME, username);
                            prefsHelper.savePref(Constants.USER_MOBILE, mobilenum);
                            prefsHelper.savePref(Constants.USER_EMAIL, usernemail);
                            prefsHelper.savePref(Constants.CUSTOMER_ID, cusomerId);
                            prefsHelper.savePref(Constants.REFERCODELOGIN, refercode);
                            prefsHelper.savePref(Constants.refercodeMessage, refercodeMessage);
                            prefsHelper.savePref(Constants.referSharingMessage, referSharingMessage);
                            prefsHelper.savePref(Constants.referralEarnPoints, referralEarnPoints);
                            prefsHelper.savePref(Constants.referralJoinFriends, referralJoinFriends);

                            LoginSuccess();
                            hideProgress();
                          //  initiateHomeFragment();
                            /*Intent i = new Intent(SignInActivity.this, SignInActivity.class);
                            startActivity(i);*/
                           // finish();
                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this, signin.getSuccessMsg(), Toast.LENGTH_SHORT).show();
                            hideProgress();
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

    public void LoginSuccess() {
        // if (!prefsHelper.isLoggedIn()) {
        prefsHelper.LogintoApp();
        if (SplashActivity.mInstance!=null)
        {
            SplashActivity.mInstance.finish();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
        //}
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

    private void initiateHomeFragment() {
        //  HomeFragment homeFragment = HomeFragment.newInstance(containerID);
        // LocationMapFragment locationMapFragment = LocationMapFragment.newInstance(containerID);
        RestaurantMain locationMapFragment = new RestaurantMain();

        FragmentTransaction transaction = SignInActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, locationMapFragment);
        transaction.addToBackStack(locationMapFragment.getTag());
        transaction.commit();
    }
}
