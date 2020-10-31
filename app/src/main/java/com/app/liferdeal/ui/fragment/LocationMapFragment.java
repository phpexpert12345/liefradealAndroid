package com.app.liferdeal.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.liferdeal.R;
import com.app.liferdeal.databinding.LocationMapLayoutBinding;
import com.app.liferdeal.model.GeocodingResponse;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.activity.MainActivity;
import com.app.liferdeal.ui.activity.searching.SearchAddressGooglePlacesActivity;
import com.app.liferdeal.ui.fragment.restaurant.RestaurantMain;
import com.app.liferdeal.util.CommonMethods;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.GPSTracker;
import com.app.liferdeal.util.PrefsHelper;
import com.app.liferdeal.viewmodel.ViewModelFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocationMapFragment extends AppCompatActivity implements OnMapReadyCallback, LocationListener, View.OnClickListener {
    private static final String TAG = "LocationMapFragment";
  //  private LocationMapLayoutBinding mapFragmentBinding;
    private int containerID;
    private GoogleMap googleMap;
    //private MapFragment mapFragment;
    private SupportMapFragment mapFragment;
    Double currentLatitude, currentLongitude;
private PrefsHelper prefsHelper;
    private static final String ARG_PARAM = "";
    private TextView ed_search;
    private String currentLat, currentLong;
    private ApiInterface apiInterface;
    private Marker secondMarker;
    private ProgressDialog progressDialog;
    private LinearLayout lnr_confirm_location;
    private ImageView img_back;

    public LocationMapFragment() {
    }

   public static LocationMapFragment newInstance() {
        LocationMapFragment fragment = new LocationMapFragment();
        return fragment;
    }

   /* public static LocationMapFragment newInstance(String param) {
        LocationMapFragment fragment = new LocationMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_map_layout);
        intializingView();
    }

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // locationMapFragmentBinding = LocationMapFragmentBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.location_map_layout, container, false);
        intializingView(view);
        return view;
    }*/

    private void intializingView() {
        prefsHelper = new PrefsHelper(this);
        //mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ed_search = findViewById(R.id.search);
        lnr_confirm_location = findViewById(R.id.lnr_confirm_location);
        img_back = findViewById(R.id.img_back);
        GPSTracker trackerObj = new GPSTracker(this);
        currentLatitude = trackerObj.getLatitude();
        currentLongitude = trackerObj.getLongitude();

        ed_search.setOnClickListener(this);
        lnr_confirm_location.setOnClickListener(this);
        img_back.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        googleMap = googleMaps;
        Geocoder geocoder;
        List<Address> addresses = null;
        googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // RestaurantModel restaurantModel = new RestaurantModel();
        try {
           /* lat = Double.parseDouble(HomeFragment.restaurantModel.getRestaurant_LatitudePoint());
            longs = Double.parseDouble(HomeFragment.restaurantModel.getRestaurant_LongitudePoint());*/

          /*  lat = Double.parseDouble(latitude);
            longs = Double.parseDouble(longitude);*/

        } catch (NullPointerException e) {
            e.printStackTrace();

        }

        System.out.println("===== lat is :" + currentLatitude + " " + "long :" + currentLongitude);
        LatLng latLong = new LatLng(currentLatitude, currentLongitude);
        // LatLng currentLatLong = new LatLng(currentLatitude, currentLongitude);
        // LatLng currentLatLong = new LatLng(28.5463658 ,-82.2084836);
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
          //  String address = addresses.get(0).getAddressLine(0);
         //   System.out.println("===== addresss " + address);
        //    String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.setTrafficEnabled(false);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15.0f));
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            //googleMaps.addMarker(new MarkerOptions().position(latLong).title((""+getIntent().getStringExtra("resturtantaddress"))));
            secondMarker = googleMaps.addMarker(new MarkerOptions().position(latLong));//.title((address)));
            // startNavigation(secondMarker, currentLatLong, latLong );

            //   googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 17));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                Intent addressIntent = new Intent(this, SearchAddressGooglePlacesActivity.class);
                addressIntent.putExtra("chooser", getResources().getString(R.string.pickup_location));
                startActivityForResult(addressIntent, 18);
                overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                break;

            case R.id.lnr_confirm_location:
              //  initiateRestFragment();
                Intent i = new Intent(LocationMapFragment.this, MainActivity.class);
                i.putExtra("SEARCHADDRESS", mPICKUP_ADDRESS);
                startActivity(i);
                finish();
                break;

            case R.id.img_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("==== onlocation change is called : ");
    }

    /**
     * method used to get address from lat long
     */
    @SuppressLint("StaticFiel" +
            "dLeak")
    private class BackgroundGeocodingTaskNew extends AsyncTask<String, Void, String> {
        GeocodingResponse response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pickupLocationAddress.setText(mPICKUP_ADDRESS);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = "https://maps.google.com/maps/api/geocode/json?latlng=" + currentLat + ","
                    + currentLong + "&key=" + prefsHelper.getPref(Constants.google_map_key);
            // + currentLong + "&key=" + Constants.MAPS_DIRECTION;
//                    + currentLongitude + "&sensor=false";

            String stringResponse = CommonMethods.callhttpRequest(url);

            if (stringResponse != null) {
                Gson gson = new Gson();
                response = gson.fromJson(stringResponse, GeocodingResponse.class);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (response != null) {
                if (response.getStatus().equals("OK") && response.getResults() != null) {
                    System.out.println("==== formated address : " + response.getResults().get(0).getFormatted_address());
                    ed_search.setText(response.getResults().get(0).getFormatted_address());
                }
            }

        }
    }

    private String formattedAddress = "", locationName = "", mPICKUP_ADDRESS = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 18) {
            if (resultCode == Activity.RESULT_OK) {
                String latitudeString = data.getStringExtra("LATITUDE_SEARCH");
                String logitudeString = data.getStringExtra("LONGITUDE_SEARCH");
                mPICKUP_ADDRESS = data.getStringExtra("SearchAddress");

                formattedAddress = data.getStringExtra("SearchAddress");
                locationName = data.getStringExtra("ADDRESS_NAME");

                if (mPICKUP_ADDRESS != null) {

                    if (locationName != null && !locationName.isEmpty()) {
                        ed_search.setText(locationName + " " + mPICKUP_ADDRESS);
                    } else {
                        ed_search.setText(mPICKUP_ADDRESS);
                    }
                }
                LatLng latLng = new LatLng(Double.parseDouble(latitudeString), Double.parseDouble(logitudeString));

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                secondMarker = googleMap.addMarker(new MarkerOptions().position(latLng));//.title((address)));

                getAddressFromCurrentLatLong(latitudeString, logitudeString);

                System.out.println("onActivityResult latitudeString...." + latitudeString + "...logitudeString..." + logitudeString);

            }
        }
    }

    private void getAddressFromCurrentLatLong(String latitudeString, String logitudeString) {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(latitudeString), Double.parseDouble(logitudeString), 1);
                String address = addresses.get(0).getAddressLine(0);
                System.out.println("===== addresss " + address);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();

                prefsHelper.savePref(Constants.SAVE_FULL_ADDRESS, address);
                prefsHelper.savePref(Constants.SAVE_CITY_NAME, city);
                prefsHelper.savePref(Constants.SAVE_STATE, state);
                prefsHelper.savePref(Constants.SAVE_COUNTRY, country);
                prefsHelper.savePref(Constants.SAVE_POSTAL_CODE, postalCode);

              //  showProgress();
              //  initiateRestFragment();

            }
            catch (Exception ex) {
                ex.printStackTrace();
            }



    }

    public static String getTAG() {
        return TAG;
    }

    private void initiateRestFragment() {
        //  HomeFragment homeFragment = HomeFragment.newInstance(containerID);
        // LocationMapFragment locationMapFragment = LocationMapFragment.newInstance(containerID);
        RestaurantMain restaurantMain = new RestaurantMain();
        Bundle bundle = new Bundle();
        bundle.putString("SEARCHLOCATIONADDRESS", mPICKUP_ADDRESS);

        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, restaurantMain);
        restaurantMain.setArguments(bundle);
        transaction.addToBackStack(restaurantMain.getTag());
        transaction.commit();
        hideProgress();
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

