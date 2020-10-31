package com.app.liferdeal.ui.fragment.restaurant;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.GalleryPhoto;
import com.app.liferdeal.model.restaurant.RestaurantGalleryModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.ui.activity.restaurant.RestaurantPhotoGallery;
import com.app.liferdeal.ui.adapters.ProfileGalleryAdapter;
import com.app.liferdeal.util.PrefsHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TabFragment1  extends Fragment {

    private PrefsHelper prefsHelper;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private RecyclerView rest_food_gallery;
    private ProgressBar banner_progress;
    private String clickRestId="";
    private List<RestaurantGalleryModel.FoodGalleryList> mlist;
    private List<GalleryPhoto> mListParty;
    private List<GalleryPhoto> mListPhoto;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_frag_one, container, false);;
        init(view);
        return view;
    }

    private void init(View v)
    {
        prefsHelper = new PrefsHelper(getActivity());
        rest_food_gallery = v.findViewById(R.id.rest_food_gallery);
        banner_progress = v.findViewById(R.id.banner_progress);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        rest_food_gallery.setLayoutManager(mLayoutManager);
        rest_food_gallery.setItemAnimator(new DefaultItemAnimator());
        rest_food_gallery.setHasFixedSize(true);
       // clickRestId = getIntent().getStringExtra("clickRestId");
        System.out.println("==== clickRestId in rest gallery activity : " + clickRestId);
        Gson gson = new Gson();

        String gson1 =  getArguments().getString("MListdata");
        Type type = new TypeToken<List<RestaurantGalleryModel.FoodGalleryList>>() {
        }.getType();

       mlist =  gson.fromJson(gson1, type);

       //////////////////////////////////////////////

        String gsonparty =  getArguments().getString("mListPartyData");
        Type typeparty = new TypeToken<List<GalleryPhoto>>() {
        }.getType();

        mListParty =  gson.fromJson(gsonparty, typeparty);

        setAdapterPhotoGallery();
    }

    private void setAdapterPhotoGallery(){
        ProfileGalleryAdapter adapterCategory = new ProfileGalleryAdapter(getActivity(),mListParty);
        rest_food_gallery.setAdapter(adapterCategory);
        // adapterCategory.notifyDataSetChanged();
       // hideProgress();
    }
}
