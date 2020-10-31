package com.app.liferdeal.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.FoodItemSizeModel;
import com.app.liferdeal.model.restaurant.RestMenuCat;
import com.app.liferdeal.model.restaurant.SubItemsRecord;
import com.app.liferdeal.ui.activity.restaurant.AddClickDetails;
import com.app.liferdeal.ui.activity.restaurant.AddExtraActivity;
import com.app.liferdeal.ui.activity.restaurant.RestaurantDetails;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class RestaurantFoodItemSizeAdapter extends RecyclerView.Adapter<RestaurantFoodItemSizeAdapter.Holder> {
    private List<FoodItemSizeModel.RestaurantMenItemsSize> listCategory;
    private Context mContext;
    RadioButton globalradio=null;
    private RestaurantFoodSizeItemInterface restaurantFoodSizeItemInterface;
    private PrefsHelper prefsHelper;


    public interface RestaurantFoodSizeItemInterface
    {
        void getrvcheckeddata(int itemId, int itemSizeId, String extraAvail, String foodItemName, String pizzaPrice);
    }
    public RestaurantFoodItemSizeAdapter(Context context, List<FoodItemSizeModel.RestaurantMenItemsSize>listSubCategory,RestaurantFoodSizeItemInterface restaurantFoodSizeItemInterface1){
        this.mContext=context;
        restaurantFoodSizeItemInterface = restaurantFoodSizeItemInterface1;
        this.listCategory=listSubCategory;
        prefsHelper = new PrefsHelper(mContext);

    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_click_details,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.txtitemname.setText(listCategory.get(position).getRestaurantPizzaItemName());
        //String hh = Constants.getCurrencySymbol(prefsHelper.getPref(Constants.APP_CURRENCY));
        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
        System.out.println("==== hh in size  : " + hh);
       String jj =  hh.getSymbol();
        System.out.println("==== jj in size  : " + jj);
        holder.txtprice.setText(jj+"  " + listCategory.get(position).getRestaurantPizzaItemPrice());
       // holder.radioButton.setChecked(lastSelectedPosition == position);

        holder.cv_RecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton checked_rb = holder.rbitem;// (RadioButton) view;
                if(globalradio != null) {
                    globalradio.setChecked(false);
                    System.out.println("==== checked not null");
                    restaurantFoodSizeItemInterface.getrvcheckeddata(listCategory.get(position).getFoodItemID(), listCategory.get(position).getFoodItemSizeID(),listCategory.get(position).getExtraavailable(), listCategory.get(position).getRestaurantPizzaItemName(), listCategory.get(position).getRestaurantPizzaItemPrice());
                }
                else
                {
                    System.out.println("==== checked  null");
                    restaurantFoodSizeItemInterface.getrvcheckeddata(listCategory.get(position).getFoodItemID(), listCategory.get(position).getFoodItemSizeID(),listCategory.get(position).getExtraavailable(),listCategory.get(position).getRestaurantPizzaItemName(),listCategory.get(position).getRestaurantPizzaItemPrice());

                }
                globalradio=checked_rb;
                globalradio.setChecked(true);
            }
        });

        holder.rbitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton checked_rb = (RadioButton) view;
                if(globalradio != null) {
                    globalradio.setChecked(false);
                    System.out.println("==== checked not null");
                    restaurantFoodSizeItemInterface.getrvcheckeddata(listCategory.get(position).getFoodItemID(), listCategory.get(position).getFoodItemSizeID(),listCategory.get(position).getExtraavailable(), listCategory.get(position).getRestaurantPizzaItemName(), listCategory.get(position).getRestaurantPizzaItemPrice());
                }
                else
                {
                    System.out.println("==== checked  null");
                    restaurantFoodSizeItemInterface.getrvcheckeddata(listCategory.get(position).getFoodItemID(), listCategory.get(position).getFoodItemSizeID(),listCategory.get(position).getExtraavailable(),listCategory.get(position).getRestaurantPizzaItemName(),listCategory.get(position).getRestaurantPizzaItemPrice());

                }
                globalradio=checked_rb;
            }
        });

    }


    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    class  Holder extends RecyclerView.ViewHolder{
        public CardView card_subCategory;
        private TextView txtitemname, txtprice;
        private RadioButton rbitem;
        private CheckBox cbitem;
        private LinearLayout cv_RecyclerView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtitemname = itemView.findViewById(R.id.txtitemname);
            txtprice = itemView.findViewById(R.id.txtprice);
            cbitem = (CheckBox) itemView.findViewById(R.id.cbitem);
            rbitem = (RadioButton) itemView.findViewById(R.id.rbitem);
            cv_RecyclerView = itemView.findViewById(R.id.cv_RecyclerView);

        }
    }
}
