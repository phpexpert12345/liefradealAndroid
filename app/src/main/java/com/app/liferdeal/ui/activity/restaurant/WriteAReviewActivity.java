package com.app.liferdeal.ui.activity.restaurant;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.liferdeal.R;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;


public class WriteAReviewActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btn_submit;
    private ImageView iv_back;
    private PrefsHelper prefsHelper;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private String restid ="", customerId="";
    private RatingBar ratingBar;
    private EditText edt_txt_write_review;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeareview);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        progressDialog = new ProgressDialog(this);
        iv_back = findViewById(R.id.iv_back);
        ratingBar = findViewById(R.id.ratingBar);
        edt_txt_write_review = findViewById(R.id.edt_txt_write_review);
        btn_submit = findViewById(R.id.btn_submit);

        customerId = prefsHelper.getPref(Constants.CUSTOMER_ID);
        restid = getIntent().getStringExtra("clickRestId");
        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_submit:

                break;

            default:
                break;
        }
    }
}
