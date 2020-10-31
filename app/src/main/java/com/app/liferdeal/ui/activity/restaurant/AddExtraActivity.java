package com.app.liferdeal.ui.activity.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.FoodExtraModel;
import com.app.liferdeal.model.restaurant.FoodItemSizeModel;
import com.app.liferdeal.network.retrofit.ApiInterface;
import com.app.liferdeal.network.retrofit.RFClient;
import com.app.liferdeal.ui.Database.Database;
import com.app.liferdeal.ui.Database.model.CartModel;
import com.app.liferdeal.ui.activity.MainActivity;
import com.app.liferdeal.ui.adapters.RestaurantFoodItemExtraAdapter;
import com.app.liferdeal.ui.adapters.RestaurantFoodItemSizeAdapter;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddExtraActivity extends AppCompatActivity implements View.OnClickListener, RestaurantFoodItemExtraAdapter.RestaurantFoodItemExtraAdapterInterface {
    private PrefsHelper prefsHelper;
    private ProgressDialog progressDialog;
    private ApiInterface apiInterface;
    private int itemId, itemSizeId, restDetailsItemId;
    private RecyclerView food_item_extra_list_rcv;
    private TextView btn_add_to_cart, txt_view_name,txt_add_extra_total,txt_view_cusine_name,txt_currect_symbol;
    private ImageView img_back;
    Database database;
    ArrayList<Integer> extra_item_list_id;
    ArrayList<Integer> temp_extra_item_list_id;
    ArrayList<String> extra_item_list_name;
    ArrayList<String> extra_item_list_price;
    public static int cart_Item_number ;
    private String TotalPriceWithPizzaItemAndSize,SUBCATCLICKITEMDesc, restDealisItemId, restDetailsItemName, restDetailsItemPrice, sizePizzaPrice,selectFoodItemName,mainClickRestName, mainClickRestId;
private String currencySymbol="";
    ArrayList<String> tempExtraItemidWithSizeIdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_exta);
        init();
    }

    private void init()
    {
        prefsHelper = new PrefsHelper(this);
        database = new Database(AddExtraActivity.this);
        txt_view_name = findViewById(R.id.txt_view_name);
        txt_add_extra_total = findViewById(R.id.txt_add_extra_total);
        txt_view_cusine_name = findViewById(R.id.txt_view_cusine_name);
        txt_currect_symbol = findViewById(R.id.txt_currect_symbol);
        img_back = findViewById(R.id.img_back);
        tempExtraItemidWithSizeIdd = new ArrayList<>();
        extra_item_list_id = new ArrayList<>();
        temp_extra_item_list_id = new ArrayList<>();
        extra_item_list_name = new ArrayList<>();
        extra_item_list_price = new ArrayList<>();
        itemId = getIntent().getIntExtra("ITEMID",0);
        itemSizeId = getIntent().getIntExtra("FOODITEMSIZEID",0);
        sizePizzaPrice = getIntent().getStringExtra("sizePizzaPrice");
        selectFoodItemName = getIntent().getStringExtra("selectFoodItemName");
        mainClickRestName = getIntent().getStringExtra("mainClickRestName");
        mainClickRestId = getIntent().getStringExtra("mainClickRestId");
        restDetailsItemId = getIntent().getIntExtra("SUBCATCLICKITEMID",0);
        restDetailsItemName = getIntent().getStringExtra("SUBCATCLICKITEMNAME");
        restDetailsItemPrice = getIntent().getStringExtra("SUBCATCLICKITEMPRICE");
        SUBCATCLICKITEMDesc = getIntent().getStringExtra("SUBCATCLICKITEMDesc");
        TotalPriceWithPizzaItemAndSize = getIntent().getStringExtra("TotalPriceWithPizzaItemAndSize");

        if (restDetailsItemId!=0)
        {
            restDealisItemId = String.valueOf(restDetailsItemId);
        }
        System.out.println("=== whole data : " + "itemId : " + itemId + "itemSizeId" + itemSizeId );
        System.out.println("=== whole data 1 : " + "sizePizzaPrice" + sizePizzaPrice+ " selectFoodItemName " + selectFoodItemName + "mainClickRestName : " + mainClickRestName + "mainClickRestIDe : " + mainClickRestId);
        System.out.println("=== whole data 1 :" +  "SUBCATCLICKITEMID"  + restDealisItemId + " SUBCATCLICKITEMNAME : " + restDetailsItemName + "SUBCATCLICKITEMPRICE :" + restDetailsItemPrice);

        System.out.println("=== clickItemtId : " + itemId);
        food_item_extra_list_rcv = findViewById(R.id.food_item_extra_list_rcv);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddExtraActivity.this);
        food_item_extra_list_rcv.setLayoutManager(mLayoutManager);
        food_item_extra_list_rcv.setItemAnimator(new DefaultItemAnimator());

        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
         currencySymbol =  hh.getSymbol();

        txt_view_name.setText(restDetailsItemName);
        txt_view_cusine_name.setText(SUBCATCLICKITEMDesc);
        txt_add_extra_total.setText(currencySymbol + " " + ""+String.format("%.2f", Double.parseDouble(TotalPriceWithPizzaItemAndSize)));
        getItemExtraData(itemId, itemSizeId);
        btn_add_to_cart.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void getItemExtraData(int clickItemtId, int itemSizeId)
    {
        apiInterface = RFClient.getClient().create(ApiInterface.class);
        Observable<FoodExtraModel> observable = apiInterface.getFoodItemExtra(prefsHelper.getPref(Constants.API_KEY), Constants.LNG_CODE,clickItemtId,itemSizeId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FoodExtraModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FoodExtraModel searchResult) {
                        showProgress();
                        setAdapterCategory(searchResult.getMenuItemExtraGroup());
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

    private void setAdapterCategory(List<FoodExtraModel.MenuItemExtraGroup> list){
        RestaurantFoodItemExtraAdapter adapterCategory = new RestaurantFoodItemExtraAdapter(AddExtraActivity.this,list, list.get(0).getSubExtraItemsRecord(), AddExtraActivity.this, itemId, itemSizeId);
        food_item_extra_list_rcv.setAdapter(adapterCategory);
        hideProgress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_back:
                finish();
                break;

            case R.id.btn_add_to_cart:
              /*  double total = 0.0;
                int extaId;
                int qunt = 0;
                double price1 = 0.0;
                if (extra_item_list_price.size()!=0)
                {

                    for (int i =0;i<extra_item_list_price.size();i++)
                    {
                        total = total+Double.parseDouble(extra_item_list_price.get(i));
                    }
                }

                double sizePizzaPricee=0.0;
                double restDetailsItemPricee=0.0;
                if (!sizePizzaPrice.equalsIgnoreCase(""))
                {
                     sizePizzaPricee = Double.parseDouble(sizePizzaPrice);
                }

                if (!restDetailsItemPrice.equalsIgnoreCase(""))
                {
                    restDetailsItemPricee = Double.parseDouble(restDetailsItemPrice);
                }*/
                double total = 0.0;
                int extaId;
                int qunt = 0;
                double price1 = 0.0;

                double sizePizzaPricee = 0.0;
                double restDetailsItemPricee = 0.0;
                if (!sizePizzaPrice.equalsIgnoreCase("")) {
                    sizePizzaPricee = Double.parseDouble(sizePizzaPrice);
                }
                double price = 0.0;
                if (!restDetailsItemPrice.equalsIgnoreCase("")) {
                    restDetailsItemPricee = Double.parseDouble(restDetailsItemPrice);
                }
                if (extra_item_list_price.size() != 0) {

                    for (int i = 0; i < extra_item_list_price.size(); i++) {
                        total = total + Double.parseDouble(extra_item_list_price.get(i));
                    }
                  //  total = total + restDetailsItemPricee;
                    total = total + Double.parseDouble(TotalPriceWithPizzaItemAndSize);
                }


                SQLiteDatabase db = database.getReadableDatabase();


                Cursor cursor = db.rawQuery("SELECT * FROM item_table where item_id='" + restDetailsItemId + "'", null);
                if (cursor.moveToNext()) {
                    qunt = Integer.parseInt(cursor.getString(7)) + 1;
                    price = Double.parseDouble(restDetailsItemPrice) * qunt;
                    if (extra_item_list_id.size() > 0) {
                        qunt = Integer.parseInt(cursor.getString(7));
                        price1 = Double.parseDouble(cursor.getString(6));
                        price1 = price1 / qunt;
                        price1 = (double) Math.round(price1 * 100) / 100;
                        qunt = qunt + 1;
                        price1 = price1 * qunt;
                        database.update_item(String.valueOf(restDetailsItemId), qunt, price);
                        database.update_extra_quantity(getIntent().getStringExtra("itemid"), "" + extra_item_list_id, price1, qunt);
                    } else {
                        database.update_item(String.valueOf(restDetailsItemId), qunt, price);
                    }
                } else {
                    if (extra_item_list_id.size() > 0)
                        database.InsertItem(String.valueOf(restDetailsItemId), restDetailsItemName, "0", "0", "" + extra_item_list_id, "" + extra_item_list_name, total, 1);
                    else {
                        database.InsertItem(String.valueOf(restDetailsItemId), restDetailsItemName, "0", "0", "0", "0", Double.parseDouble(restDetailsItemPrice), 1);

                    }
                    cart_Item_number = cart_Item_number + 1;
                    RestaurantDetails.tvCartItemCount.setText("" + cart_Item_number);
                    Intent i = new Intent(this, RestaurantDetails.class);
                    i.putExtra("SIZEITEMID", itemSizeId);
                    i.putExtra("tempExtraItemidWithSizeIdd", tempExtraItemidWithSizeIdd);
                    //i.putExtra("", ext);
                    startActivity(i);
                }
                database.close();
                break;

            default:
                break;
        }
    }

    private String extraItemidWithSizeId="", tempExtraItemidWithSizeId="";

    @Override
    public void getrvcheckeddata(ArrayList<String> temp_extraId, ArrayList<Integer> extraId, ArrayList<String> extraName, ArrayList<String> extraPrice) {
       tempExtraItemidWithSizeIdd = temp_extraId;
        extra_item_list_id = extraId;
        extra_item_list_name = extraName;
        extra_item_list_price = extraPrice;
        System.out.println("=== extra add inactivity: " + extra_item_list_id + extra_item_list_name + extra_item_list_price);

        System.out.println("===== extraItemidWithSizeId tempExtraItemidWithSizeIdd: " + tempExtraItemidWithSizeIdd);
        double total=0.0;
        if (extra_item_list_price.size()!=0)
        {

            for (int i =0;i<extra_item_list_price.size();i++)
            {
                 total = total+Double.parseDouble(extra_item_list_price.get(i));
            }
        }

        total = total + Double.parseDouble(TotalPriceWithPizzaItemAndSize);
        System.out.println("=== total price after aff pizz and size price : " + total);

        txt_add_extra_total.setText(currencySymbol + " " + ""+String.format("%.2f", total));


    }
}
