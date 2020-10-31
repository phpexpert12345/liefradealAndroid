package com.app.liferdeal.ui.activity.restaurant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.ContactusModel;
import com.app.liferdeal.model.restaurant.RestaurantBookTableModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.util.CommonMethods;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import java.util.Calendar;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantBookTable extends AppCompatActivity implements View.OnClickListener {

    private TextView edit_name,edit_email_addres, edit_mobile, edit_no_person, edit_specia_instruct;
    private Button btn_submit;
    private ImageView iv_back;
    private PrefsHelper prefsHelper;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private TextView edit_book_date, edit_book_time;
    private String restid ="", customerId="", strName="", stremail="", strPhone="", strBookDate="", strBookTime="", strNumberofPerson="", strSpecialInstruct="";

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_a_table);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        progressDialog = new ProgressDialog(this);
        iv_back = findViewById(R.id.iv_back);
        btn_submit = findViewById(R.id.btn_submit);
        edit_name = findViewById(R.id.edit_name);
        edit_email_addres = findViewById(R.id.edit_email_addres);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_book_date = findViewById(R.id.edit_book_date);
        edit_book_time = findViewById(R.id.edit_book_time);
        edit_no_person = findViewById(R.id.edit_no_person);
        edit_specia_instruct = findViewById(R.id.edit_specia_instruct);

       customerId = prefsHelper.getPref(Constants.CUSTOMER_ID);
        restid = getIntent().getStringExtra("clickRestId");
        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        edit_book_date.setOnClickListener(this);
        edit_book_time.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;

            case R.id.edit_book_date:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RestaurantBookTable.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                edit_book_date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

            case R.id.edit_book_time:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RestaurantBookTable.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edit_book_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

                break;

            case R.id.btn_submit:
                strName = edit_name.getText().toString().trim();
                stremail = edit_email_addres.getText().toString().trim();
                strPhone = edit_mobile.getText().toString().trim();
                strBookDate = edit_book_date.getText().toString().trim();
                strBookTime = edit_book_time.getText().toString().trim();
                strNumberofPerson = edit_no_person.getText().toString().trim();
                strSpecialInstruct = edit_specia_instruct.getText().toString().trim();
                if (edit_name.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_name.setError("Please Enter name");
                    edit_name.requestFocus();
                }
                else if (edit_email_addres.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_email_addres.setError("Please Enter Email id");
                    edit_email_addres.requestFocus();
                }
                else if (!isValidEmailId(edit_email_addres.getText().toString().trim())) {
                    edit_email_addres.setError("Invalid Email Id");
                    edit_email_addres.requestFocus();
                }
                else if (edit_mobile.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_mobile.setError("Please Enter Mobile");
                    edit_mobile.requestFocus();
                }
                else if (edit_book_date.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_book_date.setError("Please Enter Booking date");
                    edit_book_date.requestFocus();
                }

                else if (edit_book_time.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_book_time.setError("Please Enter Booking Time");
                    edit_book_time.requestFocus();
                }

                else if (edit_no_person.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_no_person.setError("Please Enter no. of person");
                    edit_no_person.requestFocus();
                }
                else if (edit_specia_instruct.getText().toString().trim().equalsIgnoreCase(""))
                {
                    edit_specia_instruct.setError("Please Enter Special instruction");
                    edit_specia_instruct.requestFocus();
                }
                else
                {
                    getBookingTableData();
                }
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

    private void getBookingTableData(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);

        Observable<RestaurantBookTableModel> observable = apiInterface.setBookingTableData(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE, customerId, restid, strName, stremail, strPhone, strBookDate, strBookTime,strNumberofPerson, strSpecialInstruct);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestaurantBookTableModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RestaurantBookTableModel searchResult) {
                        showProgress();
                        showCustomDialog1decline(searchResult.getSuccessMsg());
                        hideProgress();
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

    private void showCustomDialog1decline(String s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("" + s);

        alertDialog.setIcon(R.drawable.tick);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();

    }
}
