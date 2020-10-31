package com.app.liferdeal.ui.activity.tickets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.ModelAddressList;
import com.app.liferdeal.model.restaurant.Orders;
import com.app.liferdeal.model.restaurant.TicketListDataModel;
import com.app.liferdeal.model.restaurant.TimeListModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.adapters.MyOrderAdapter;
import com.app.liferdeal.ui.adapters.TicketListAdapter;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TicketList extends Activity implements View.OnClickListener {
    private ImageView img_add_ticket;
    private PrefsHelper prefsHelper;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private RecyclerView recyler_view_ticket;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_list);


        findViewById();
    }

    private void findViewById() {

        prefsHelper = new PrefsHelper(this);
        img_add_ticket = findViewById(R.id.img_add_ticket);
        recyler_view_ticket = findViewById(R.id.recyler_view_ticket);
        iv_back = findViewById(R.id.iv_back);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyler_view_ticket.setLayoutManager(mLayoutManager);
        recyler_view_ticket.setItemAnimator(new DefaultItemAnimator());

        System.out.println("==== customer id : " + prefsHelper.getPref(Constants.CUSTOMER_ID));
        img_add_ticket.setOnClickListener(this::onClick);
        iv_back.setOnClickListener(this::onClick);
        getTicketData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_add_ticket:
                startActivity(new Intent(this, AddTicket.class));
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                break;

        }


    }


    private void getTicketData() {
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<TicketListDataModel> observable = apiInterface.getTicketList(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE, prefsHelper.getPref(Constants.CUSTOMER_ID));

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TicketListDataModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TicketListDataModel searchResult) {
                        showProgress();


                        setAdapterCategory(searchResult.getComplaintsHistory());
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

    private void setAdapterCategory(List<TicketListDataModel.ComplaintsHistory> list){
        TicketListAdapter adapterCategory = new TicketListAdapter(this,list);
        recyler_view_ticket.setAdapter(adapterCategory);
        // hideProgress();
    }
}
