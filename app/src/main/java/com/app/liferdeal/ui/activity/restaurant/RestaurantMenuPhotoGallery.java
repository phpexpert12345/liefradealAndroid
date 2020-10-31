package com.app.liferdeal.ui.activity.restaurant;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.GalleryPhoto;
import com.app.liferdeal.model.restaurant.RestaurantGalleryModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.adapters.PagerAdapter;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantMenuPhotoGallery extends AppCompatActivity implements View.OnClickListener {
    private PrefsHelper prefsHelper;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private RecyclerView rest_food_gallery;
    private ProgressBar banner_progress;
    private String clickRestId="";
    TabLayout tabLayout;
    private ImageView img_back;
    private List<GalleryPhoto> listParty;
    private List<GalleryPhoto> listPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_photo_gallery);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        rest_food_gallery = findViewById(R.id.rest_food_gallery);
        banner_progress = findViewById(R.id.banner_progress);
        img_back = findViewById(R.id.img_back);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(RestaurantMenuPhotoGallery.this,2);
        rest_food_gallery.setLayoutManager(mLayoutManager);
        rest_food_gallery.setItemAnimator(new DefaultItemAnimator());
        rest_food_gallery.setHasFixedSize(true);
        clickRestId = getIntent().getStringExtra("clickRestId");
        System.out.println("==== clickRestId in rest gallery activity : " + clickRestId);
        tabLayout = findViewById(R.id.tab_layout);
        img_back.setOnClickListener(this);
        getPhotoGalleryData();

    }

    private void getPhotoGalleryData(){

        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<RestaurantGalleryModel> observable = apiInterface.getRestaurantMenuGallery(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,clickRestId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestaurantGalleryModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RestaurantGalleryModel searchResult) {
                      //  showProgress();

                        setTabPageData(searchResult.getFoodGalleryList());
                       // banner_progress.setVisibility(View.GONE);
                      //  hideProgress();

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

  /*  private void setAdapterPhotoGallery(List<RestaurantGalleryModel.FoodGalleryList> list){
        ProfileGalleryAdapter adapterCategory = new ProfileGalleryAdapter(RestaurantPhotoGallery.this,list);
        rest_food_gallery.setAdapter(adapterCategory);
        // adapterCategory.notifyDataSetChanged();
        hideProgress();
    }*/

    private void setTabPageData(List<RestaurantGalleryModel.FoodGalleryList> list)
    {
        for (int i =0; i<list.size(); i++)
        {
            tabLayout.addTab(tabLayout.newTab().setText(list.get(i).getTabName()));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            if (list.get(i).getPhotoTabId()==5)
            {
                listParty = list.get(i).getGalleryPhoto();
            }
            if (list.get(i).getPhotoTabId()==4)
            {
                listPhoto = list.get(i).getGalleryPhoto();
            }
        }

      /*  tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
       // final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(),tabLayout.getTabCount());
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), list, listParty, listPhoto, RestaurantMenuPhotoGallery.this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

      /*  tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_back:
                finish();
                break;

            default:
                break;
        }
    }
}
