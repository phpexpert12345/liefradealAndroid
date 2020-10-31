package com.app.liferdeal.ui.activity.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.liferdeal.R;
import com.app.liferdeal.ui.activity.MainActivity;
import com.app.liferdeal.ui.activity.restaurant.AddressActivity;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back,userimage;
    private LinearLayout aboutus,help_suuport, termscondition,rl_loyality_setting, rl_my_profile, lnr_address, logout;
    private PrefsHelper prefsHelper;
    private TextView tv_user_name,tv_user_email;
    public static ProfileActivity mInstance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        init();
    }

    private void init()
    {
        try {
            prefsHelper = new PrefsHelper(this);
            mInstance = this;
            iv_back = findViewById(R.id.iv_back);
            aboutus = findViewById(R.id.aboutus);
            userimage = findViewById(R.id.userimage);
            tv_user_name = findViewById(R.id.tv_user_name);
            tv_user_email = findViewById(R.id.tv_user_email);
            termscondition = findViewById(R.id.termscondition);
            help_suuport = findViewById(R.id.help_suuport);
            rl_loyality_setting = findViewById(R.id.rl_loyality_setting);
            rl_my_profile = findViewById(R.id.rl_my_profile);
            lnr_address = findViewById(R.id.lnr_address);
            logout = findViewById(R.id.logout);

            aboutus.setOnClickListener(this);
            termscondition.setOnClickListener(this);
            help_suuport.setOnClickListener(this);
            rl_loyality_setting.setOnClickListener(this);
            iv_back.setOnClickListener(this);
            rl_my_profile.setOnClickListener(this);
            lnr_address.setOnClickListener(this);
            logout.setOnClickListener(this);

            System.out.println("=== name : " + prefsHelper.getPref(Constants.USER_NAME));
            System.out.println("=== name email : " + prefsHelper.getPref(Constants.USER_EMAIL));
            tv_user_name.setText(prefsHelper.getPref(Constants.USER_NAME));
            tv_user_email.setText(prefsHelper.getPref(Constants.USER_EMAIL));
            String userProfileImage = prefsHelper.getPref(Constants.USER_PROFILE_IMAGE);
            System.out.println("=== name userProfileImage : " + userProfileImage);

            if (userProfileImage!=null)
            {
                Glide.with(this).load(userProfileImage).apply(new RequestOptions().override(100,100).placeholder(R.drawable.defpizzaimg)).into(userimage);
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
            case R.id.iv_back:
                finish();
                break;
            case R.id.aboutus:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.termscondition:
                Intent t = new Intent(this, PrivacyAndPolicy.class);
                startActivity(t);
                break;
            case R.id.help_suuport:
                Intent h = new Intent(this, HelpSupportActivity.class);
                startActivity(h);
                break;

            case R.id.rl_loyality_setting:
                Intent il = new Intent(this, LoyaltyPoints.class);
                startActivity(il);
                break;

            case R.id.rl_my_profile:
                Intent ipro = new Intent(this, EditUpdateProfileActivity.class);
                startActivity(ipro);
                break;

            case R.id.lnr_address:
                    Intent addadress = new Intent(this, AddressActivity.class);
                    startActivity(addadress);
                    break;

            case R.id.logout:
                logoutDialog();
                break;

            default:
                break;
        }
    }

    public void updateProfilePic(String profileimage)
    {
        System.out.println("=== profileiamge : " + profileimage);
        Glide.with(this).load(profileimage).apply(new RequestOptions().override(100,100).placeholder(R.drawable.defpizzaimg)).into(userimage);


    }
    private void logoutDialog() {
        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("Liferdeal")
                .setMessage("Are you sure you want to logout now ?")
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                try {
                                    File dir = getCacheDir();
                                    //deleteDir(dir);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                prefsHelper.clearAllPref();
                                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                dialog.dismiss();

                            }
                        }).show();
    }


}
