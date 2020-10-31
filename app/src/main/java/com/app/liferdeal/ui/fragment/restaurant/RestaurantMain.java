package com.app.liferdeal.ui.fragment.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.loginsignup.SignupModel;
import com.app.liferdeal.model.restaurant.RestaurantMainModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.activity.MainActivity;
import com.app.liferdeal.ui.activity.login.SignInActivity;
import com.app.liferdeal.ui.activity.login.SignUpActivity;
import com.app.liferdeal.ui.activity.profile.ProfileActivity;
import com.app.liferdeal.ui.activity.restaurant.CusineFilter;
import com.app.liferdeal.ui.adapters.RestaurantMainAdapter;
import com.app.liferdeal.ui.fragment.LocationMapFragment;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantMain extends Fragment implements View.OnClickListener {
    private static final String TAG = "RestaurantMainFrag";

    private RecyclerView rcv_rest_list;
    private ProgressDialog progressDialog;
    private PrefsHelper prefsHelper;
    private ApiInterface apiInterface;
    private ProgressBar banner_progress;
    private ImageView home_icon, filter_cusine, img_location, img_profile;
    private String fullAddress="", cityName="", cityState="", cityPostalcode="", cityCountry="", locationSearchAddress="";
    private ArrayList<String> selected_cusines;
    private  Observable<RestaurantMainModel> observable;
    private EditText edt_search;
    private List<RestaurantMainModel.SearchResult> mlist;
    private RestaurantMainAdapter adapterCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_main_layout, container, false);
        System.out.println("==== rest main is call");
        intializingView(view);
        return view;
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
        System.out.println("=== setretain is called");
    }

    private void intializingView(View v)
    {
        prefsHelper = new PrefsHelper(getActivity());
        selected_cusines = new ArrayList<>();
        mlist = new ArrayList<>();
        fullAddress = prefsHelper.getPref(Constants.SAVE_FULL_ADDRESS);
        cityName = prefsHelper.getPref(Constants.SAVE_CITY_NAME);
        cityState = prefsHelper.getPref(Constants.SAVE_STATE);
        cityPostalcode = prefsHelper.getPref(Constants.SAVE_POSTAL_CODE);
        cityCountry = prefsHelper.getPref(Constants.SAVE_COUNTRY);
        System.out.println("==== addresses rest main: " + fullAddress + cityName);
        rcv_rest_list =  v.findViewById(R.id.rcv_rest_list);
        banner_progress =  v.findViewById(R.id.banner_progress);
        home_icon =  v.findViewById(R.id.home);
        filter_cusine =  v.findViewById(R.id.filter_cusine);
        img_location =  v.findViewById(R.id.img_location);
        img_profile =  v.findViewById(R.id.img_profile);
        edt_search =  v.findViewById(R.id.edt_search);
        String applogo = prefsHelper.getPref(Constants.APP_TOP_LOGO_ICON);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rcv_rest_list.setLayoutManager(mLayoutManager);
        rcv_rest_list.setItemAnimator(new DefaultItemAnimator());
        banner_progress.setVisibility(View.VISIBLE);
      //  Glide.with(getActivity()).load(Uri.parse(applogo)).into(home_icon);

        img_location.setOnClickListener(this);
        filter_cusine.setOnClickListener(this);
        home_icon.setOnClickListener(this);
        img_profile.setOnClickListener(this);

        if (getArguments()!=null) {
            Bundle bundle = getArguments();
            selected_cusines = bundle.getStringArrayList("SELECTEDCUSINES");
            System.out.println("==== selected cusine in main restmain" + selected_cusines);
            locationSearchAddress = bundle.getString("locationSearchAddress");
            System.out.println("==== locationSearchAddress : " + locationSearchAddress);
        }
        System.out.println("==== selected cusine in main restmain 1" + selected_cusines);

        getRestSearchInfo();

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterByDistance(editable.toString());
            }
        });
    }

    public static String getTAG() {
        return TAG;
    }

    private void setAdapterCategory(List<RestaurantMainModel.SearchResult> list){
        mlist = list;
         adapterCategory = new RestaurantMainAdapter(getActivity(),list);
        rcv_rest_list.setAdapter(adapterCategory);
       // hideProgress();
    }

    private void getRestSearchInfo(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);

           observable = apiInterface.getSearchRestData(fullAddress, cityName, cityPostalcode, cityState, Constants.API_KEY, Constants.LNG_CODE,selected_cusines);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestaurantMainModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RestaurantMainModel searchResult) {
                       // showProgress();
                       setAdapterCategory(searchResult.getSearchResult());
                        banner_progress.setVisibility(View.GONE);


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
        progressDialog = new ProgressDialog(getActivity());
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
            case R.id.filter_cusine:
                Intent i = new Intent(getActivity(), CusineFilter.class);
                startActivity(i);
                break;

            case R.id.home:
                Intent ii = new Intent(getActivity(), MainActivity.class);
                ii.putExtra("FROMWHERE", "fromhome");
                startActivity(ii);
                break;

            case R.id.img_location:
                Intent il = new Intent(getActivity(), LocationMapFragment.class);
                il.putExtra("FROMWHERE", "fromlocation");
                startActivity(il);
                break;

            case R.id.img_profile:
                System.out.println("==== check is login : " + prefsHelper.isLoggedIn());
                if(prefsHelper.isLoggedIn())
                {
                    Intent ip = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(ip);
                }
                else
                {
                    Intent is = new Intent(getActivity(), SignInActivity.class);
                    startActivity(is);
                }

                break;

            default:
                break;
        }
    }
    void filterByDistance(String text){
        try {
            List<RestaurantMainModel.SearchResult> temp = new ArrayList();
            for(RestaurantMainModel.SearchResult d:mlist ){
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if(d.getRestaurantName().toLowerCase().contains(text)){
                    temp.add(d);
                }
            }
            adapterCategory.updateList(temp,"");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
