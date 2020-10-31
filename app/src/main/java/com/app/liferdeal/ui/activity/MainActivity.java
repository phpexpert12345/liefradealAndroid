package com.app.liferdeal.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.LoyalityPointsModel;
import com.app.liferdeal.model.splash.DataModel;
import com.app.liferdeal.ui.Database.Database;
import com.app.liferdeal.ui.activity.profile.AboutUsActivity;
import com.app.liferdeal.ui.activity.profile.ChangePassword;
import com.app.liferdeal.ui.activity.profile.ContactUs;
import com.app.liferdeal.ui.activity.profile.HelpSupportActivity;
import com.app.liferdeal.ui.activity.profile.LoyaltyPoints;
import com.app.liferdeal.ui.activity.profile.PrivacyAndPolicy;
import com.app.liferdeal.ui.activity.profile.ProfileActivity;
import com.app.liferdeal.ui.activity.profile.ReferEarnFrndActivity;
import com.app.liferdeal.ui.activity.restaurant.AddExtraActivity;
import com.app.liferdeal.ui.activity.restaurant.AddressActivity;
import com.app.liferdeal.ui.activity.restaurant.MyOrderActivity;
import com.app.liferdeal.ui.activity.restaurant.WriteAReviewActivity;
import com.app.liferdeal.ui.activity.tickets.TicketList;
import com.app.liferdeal.ui.adapters.DrawerItemCustomAdapter;
import com.app.liferdeal.ui.fragment.LocationMapFragment;
import com.app.liferdeal.ui.fragment.restaurant.RestaurantMain;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.GPSTracker;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] mNavigationDrawerItemTitles;
    public static DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ActionBarDrawerToggle mDrawerToggle;
    private PrefsHelper authPreference;
    private DataModel[] drawerItem;
    public static LinearLayout rl_main_left_drawer, lnr_edit;
    private String customerId, firstName, lastName, userPhone, userEmail, userFloor, userStreetName, userZipCode, city, state, userAddress;
    private String locationSearchAddress="", restaurantName = "", restaurantAddress = "", orderDate = "", orderTime = "", ordPrice = "", orderIdentifyno = "";
    Database database;
    LinearLayout editprofile, logonotshow;
    TextView username, usermobile, useremail;
    RelativeLayout myaccLayout, myorderLayout, menuLayout, logoutLayout, logoutLayout1;
    CircleImageView profileimage;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String CustomerId;
    public static String where_para = "", customerlocality_para = "";
    private ArrayList<String> selected_cusines;
    private ImageView img_logo, language_logo, img_current_img;
    private TextView tv_location, textView_name;
    private Double currentLatitude, currentLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authPreference = new PrefsHelper(this);

     /*   if (authPreference.getPref(Constants.USER_PROFILE_IMAGE).equals("") || (authPreference.getPref(Constants.USER_PROFILE_IMAGE).equals(null))) {

        } else {
            Picasso.get().load(authPreference.getUserPhoto()).into(profileimage);
        }
*/
        if (checkAndRequestPermissions()) {
        }
        lnr_edit =  findViewById(R.id.lnr_edit);
        textView_name =  findViewById(R.id.textView_name);
       // statusBarColor();
            AddExtraActivity.cart_Item_number = 0;
        database = new Database(MainActivity.this);
        database.delete();
//        database.deal_delete();
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList =  findViewById(R.id.main_nav_menu_recyclerview);

        rl_main_left_drawer = findViewById(R.id.rl_main_left_drawer);

        if (getIntent().getExtras()!=null)
        {
            locationSearchAddress = getIntent().getStringExtra("SEARCHADDRESS");
            System.out.println("==== SEARCHADDRESS in main : " + locationSearchAddress);
        }
        System.out.println("==== SEARCHADDRESS in main : " + locationSearchAddress);

      /*  if (getIntent().getExtras() != null) {
            firstName = getIntent().getStringExtra("firstName");
            lastName = getIntent().getStringExtra("lastName");
            userEmail = getIntent().getStringExtra("email");
            userPhone = getIntent().getStringExtra("phoneNumber");

            city = getIntent().getStringExtra("userCity");
            state = getIntent().getStringExtra("userState");
            userAddress = getIntent().getStringExtra("userAddress");
            restaurantName = getIntent().getStringExtra("restaurantAddress");
            restaurantAddress = getIntent().getStringExtra("restaurantName");
            orderDate = getIntent().getStringExtra("date");
            orderTime = getIntent().getStringExtra("time");
            ordPrice = getIntent().getStringExtra("orderPrice");
            orderIdentifyno = getIntent().getStringExtra("orderIdentifyno");
            username.setText(firstName + " " + lastName);
            usermobile.setText(userPhone);
            useremail.setText(userEmail);

        }*/

        GPSTracker trackerObj = new GPSTracker(this);
        currentLatitude = trackerObj.getLatitude();
        currentLongitude = trackerObj.getLongitude();
        getAddressFromCurrentLatLong(String.valueOf(currentLatitude), String.valueOf(currentLongitude));
        setupToolbar();
        selected_cusines = new ArrayList<>();

        String address = authPreference.getPref(Constants.SAVE_FULL_ADDRESS);
        System.out.println("=== address in main activity : " + address);
        String logo =  authPreference.getPref(Constants.APP_LOGO);
        String lnglogo =  authPreference.getPref(Constants.APP_LNG_ICON);
        System.out.println("==== ff : " + logo);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img_logo = toolbar.findViewById(R.id.logo_img);
        language_logo = toolbar.findViewById(R.id.language_logo);
        tv_location = toolbar.findViewById(R.id.tv_location);
        img_current_img = toolbar.findViewById(R.id.img_current_img);
        if (!locationSearchAddress.equalsIgnoreCase(""))
        {
            tv_location.setText(locationSearchAddress);

        }
        else
        {
            tv_location.setText(address);

        }

        // setToolbar();
        tv_location.setOnClickListener(this);
        if(logo!=null) {
            if (!logo.equalsIgnoreCase("")) {
                Glide.with(MainActivity.this).load(Uri.parse(logo)).into(img_logo);

            }
        }

        img_current_img.setOnClickListener(this);
        if(lnglogo!=null) {
            if (!lnglogo.equalsIgnoreCase("")) {
                Glide.with(MainActivity.this).load(Uri.parse(lnglogo)).into(language_logo);

            }
        }
        replaceHomeFragment();
        customerId = authPreference.getPref(Constants.CUSTOMER_ID);
        if(customerId!=null) {
            if (!customerId.isEmpty()) {
                textView_name.setText("");
                lnr_edit.setVisibility(View.VISIBLE);

                drawerItem = new DataModel[13];
                drawerItem[0] = new DataModel("Home", R.drawable.home);
                drawerItem[1] = new DataModel("My Account", R.drawable.myaccount);
                drawerItem[2] = new DataModel("My Order", R.drawable.myorder);
                drawerItem[3] = new DataModel("My Address", R.drawable.my_address);
                drawerItem[4] = new DataModel("My Review", R.drawable.review);
                drawerItem[5] = new DataModel("My Ticket", R.drawable.ic_my_ticket);
                drawerItem[6] = new DataModel("My Loyality Points", R.drawable.ic_loyalty_point);
                drawerItem[7] = new DataModel("Change Password", R.drawable.changepassword);
                drawerItem[8] = new DataModel("Refer a Friend", R.drawable.refer_earn);
                drawerItem[9] = new DataModel("Contact Us/Help", R.drawable.help_support);
                drawerItem[10] = new DataModel("Rate Us", R.drawable.help_support);
                drawerItem[11] = new DataModel("Language Setting", R.drawable.ic_lang);
                drawerItem[12] = new DataModel("Logout", R.drawable.logout);

            }
        }
        else {
            textView_name.setText("Guest User");
            lnr_edit.setVisibility(View.GONE);
            drawerItem = new DataModel[6];

            drawerItem[0] = new DataModel("Contact Us/Help", R.drawable.help_support);
            drawerItem[1] = new DataModel("About Us", R.drawable.about_us);
            drawerItem[2] = new DataModel("Terms of Service", R.drawable.terms_condition);
            drawerItem[3] = new DataModel("Privacy Policy", R.drawable.policy);
            drawerItem[4] = new DataModel("Rate Us", R.drawable.help_support);
            drawerItem[5] = new DataModel("Language Setting", R.drawable.ic_lang);

        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.nav_list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();


       /* editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UpdateProfileActivity.class);
                intent.putExtra("firstName", authPreference.getFirstName());
                intent.putExtra("lastName", authPreference.getLastName());
                intent.putExtra("email", authPreference.getUserEmail());
                intent.putExtra("phoneNumber", authPreference.getUserPhone());
                intent.putExtra("streetNumber", authPreference.getCompanyStreetNo());//floor
                intent.putExtra("streetName", authPreference.getCompanyStreet());
                intent.putExtra("zipCode", authPreference.getUserPostcode());
                intent.putExtra("userCity", authPreference.getUserCity());
                intent.putExtra("userState", authPreference.getUserState());
                intent.putExtra("userAddress", authPreference.getUserAddress());
                startActivity(intent);
                // finish();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
            }
        });*/
    }

    public void replaceHomeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RestaurantMain homeFragment = new RestaurantMain();
        fragmentManager.beginTransaction().replace(R.id.main_content, homeFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.tv_location:
                Intent i = new Intent(MainActivity.this, LocationMapFragment.class);
                startActivity(i);
                break;
            case R.id.img_current_img:
                Intent ii = new Intent(MainActivity.this, LocationMapFragment.class);
                startActivity(ii);
                break;

            default:
                break;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("===== drawer click : " + customerId + "position : " + position);
            if (customerId!=null) {
                System.out.println("===== drawer click if ");
                if (!customerId.isEmpty()) {
                    System.out.println("===== drawer click 1: " + customerId + "position : " + position);
                    selectItem(position);
                }
            }
            else {
                System.out.println("===== drawer click else ");
                selectItem1(position);
            }
        }
    }

    private void selectItem(int position) {
        System.out.println("===== drawer click 2: " + customerId + "position : " + position);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new RestaurantMain();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 1:
                startActivity(new Intent(this, ProfileActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 2:
                startActivity(new Intent(this, MyOrderActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 3:
                startActivity(new Intent(this, AddressActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 4:
                startActivity(new Intent(this, WriteAReviewActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 5:
                startActivity(new Intent(this, TicketList.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 6:
                startActivity(new Intent(this, LoyaltyPoints.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 7:
                 startActivity(new Intent(this, ChangePassword.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 8:
                startActivity(new Intent(this, ReferEarnFrndActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 9:
                startActivity(new Intent(this, ContactUs.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 10:
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 11:
                //startActivity(new Intent(this, ChangePasswordActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 12:
                // startActivity(new Intent(this, ReferEarnActivity.class));
                logoutDialog();
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(rl_main_left_drawer);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void selectItem1(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                startActivity(new Intent(this, HelpSupportActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 1:
                startActivity(new Intent(this, AboutUsActivity.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 2:
                startActivity(new Intent(this, PrivacyAndPolicy.class));
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 3:
                Intent intentO = new Intent(this, PrivacyAndPolicy.class);
                startActivity(intentO);
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 4:

                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 5:
                Intent intentOrder = new Intent(this, LoyaltyPoints.class);
                startActivity(intentOrder);
                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;
            case 6:

                mDrawerLayout.closeDrawer(rl_main_left_drawer);
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(rl_main_left_drawer);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    private void logoutDialog() {
        new AlertDialog.Builder(MainActivity.this)
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
                                    deleteDir(dir);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // new AuthPreference(HomeActivity.this).setCustomerId("");
                                authPreference.clearAllPref();//clear the local data
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryone));
        mDrawerToggle.syncState();
    }

    private void statusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }



    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("==== onnew " + intent);
        if (intent!=null) {
            String checkFromWhere = intent.getStringExtra("FROMWHERE");
            locationSearchAddress = intent.getStringExtra("SEARCHADDRESS");
            System.out.println("==== SEARCHADDRESS in new intent : " + locationSearchAddress);
            if (locationSearchAddress!=null) {
                if (!locationSearchAddress.equalsIgnoreCase("")) {
                    tv_location.setText(locationSearchAddress);

                }
            }
            if (checkFromWhere!=null) {
                if (checkFromWhere.equalsIgnoreCase("pagecusine")) {
                    System.out.println("==== onnew 1");
                    selected_cusines = intent.getStringArrayListExtra("SELECTEDCUSINE");
                    System.out.println("==== selected cusine in main activityu" + selected_cusines);
                    initiateRestFragment();
                } else if (checkFromWhere.equalsIgnoreCase("fromhome")) {
                    RestaurantMain locationMapFragment = new RestaurantMain();
                    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_content, locationMapFragment);
                    // transaction.addToBackStack(restaurantMain.getTag());
                    transaction.commit();
                } else if (checkFromWhere.equalsIgnoreCase("fromlocation")) {
                    RestaurantMain locationMapFragment = new RestaurantMain();
                    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_content, locationMapFragment);
                    // transaction.addToBackStack(restaurantMain.getTag());
                    transaction.commit();
                }
            }
        }
    }
    private void initiateRestFragment() {

        RestaurantMain restaurantMain = new RestaurantMain();
        Bundle args = new Bundle();
        args.putStringArrayList("SELECTEDCUSINES", selected_cusines);
        restaurantMain.setArguments(args);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, restaurantMain);
        // transaction.addToBackStack(restaurantMain.getTag());
        transaction.commit();
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

            authPreference.savePref(Constants.SAVE_FULL_ADDRESS, address);
            authPreference.savePref(Constants.SAVE_CITY_NAME, city);
            authPreference.savePref(Constants.SAVE_STATE, state);
            authPreference.savePref(Constants.SAVE_COUNTRY, country);
            authPreference.savePref(Constants.SAVE_POSTAL_CODE, postalCode);


        }
        catch (Exception ex) {
            ex.printStackTrace();
        }



    }
}