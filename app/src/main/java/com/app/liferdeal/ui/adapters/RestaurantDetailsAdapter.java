package com.app.liferdeal.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.restaurant.RestDetailClickFoodModel;
import com.app.liferdeal.model.restaurant.RestMenuCat;
import com.app.liferdeal.model.restaurant.SubItemsRecord;
import com.app.liferdeal.ui.activity.restaurant.AddClickDetails;
import com.app.liferdeal.ui.activity.restaurant.RestaurantDetails;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.Currency;
import java.util.List;

public class RestaurantDetailsAdapter extends RecyclerView.Adapter<RestaurantDetailsAdapter.Holder> {
    private List<RestMenuCat> listCategory;
    private List<SubItemsRecord>listFilterSubCategory;
    private Context mContext;
    private RestaurantDetailsAdapterInterface restaurantDetailsAdapterInterface;
    private String mainClickRestId, mainClickRestName;
    private PrefsHelper prefsHelper;
    private String pizzaglobcategoryImage="";

    public interface RestaurantDetailsAdapterInterface
    {
        void getClickData(int otemId, String itemName);
    }


    public RestaurantDetailsAdapter(Context context, List<RestMenuCat>listSubCategory,  List<SubItemsRecord>listFilterSubCategory, RestaurantDetailsAdapterInterface restaurantDetailsAdapterInterface1, String maniRestClickId, String mainRestClickName, String globcategoryImage){
        this.mContext=context;
        this.listCategory=listSubCategory;
        this.listFilterSubCategory = listFilterSubCategory;
        restaurantDetailsAdapterInterface = restaurantDetailsAdapterInterface1;
        this.mainClickRestId = maniRestClickId;
        this.mainClickRestName = mainRestClickName;
        prefsHelper = new PrefsHelper(mContext);
        this.pizzaglobcategoryImage = globcategoryImage;

    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row_list,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        holder.tv_restaurant_name.setText(listFilterSubCategory.get(position).getRestaurantPizzaItemName());
        holder.tv_restaurant_pizza_des.setText(listFilterSubCategory.get(position).getResPizzaDescription());
        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
        String jj =  hh.getSymbol();
        holder.tv_item_cost.setText(jj+"  " + listFilterSubCategory.get(position).getRestaurantPizzaItemPrice());
        if (!listFilterSubCategory.get(position).getRestaurantPizzaItemOldPrice().equalsIgnoreCase(""))
        {
            holder.tv_item_cost_old.setText(jj+"  " + listFilterSubCategory.get(position).getRestaurantPizzaItemOldPrice());
            holder.tv_item_cost_old.setPaintFlags(holder.tv_item_cost_old.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        if (listFilterSubCategory.get(position).getFoodType()!=null) {
            Glide.with(mContext).load(Uri.parse(listFilterSubCategory.get(position).getFoodType())).into(holder.iv_restaurant_logo);
        }
        System.out.println("==== list size : " + listFilterSubCategory.size());
        System.out.println("==== list size 1: " + listFilterSubCategory.get(position));
      //  if (listFilterSubCategory.get(position) == listFilterSubCategory.size())

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent i = new Intent(mContext, RestaurantDetails.class);
                mContext.startActivity(i);*/
                if (listFilterSubCategory.get(position).getSizeavailable().equalsIgnoreCase("yes"))
                {
                    restaurantDetailsAdapterInterface.getClickData(listFilterSubCategory.get(position).getItemID(), listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                    Intent i = new Intent(mContext, AddClickDetails.class);
                    i.putExtra("CLICKITEMID", listFilterSubCategory.get(position).getItemID());
                    i.putExtra("CLICKITEMNAME", listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                    i.putExtra("CLICKITEMPRICE", listFilterSubCategory.get(position).getRestaurantPizzaItemPrice());
                    i.putExtra("CLICKPIZZDESC", listFilterSubCategory.get(position).getResPizzaDescription());
                    i.putExtra("mainClickRestId", mainClickRestId);
                    i.putExtra("mainClickRestName", mainClickRestName);
                    mContext.startActivity(i);
                }
                else
                {
                    restaurantDetailsAdapterInterface.getClickData(listFilterSubCategory.get(position).getItemID(), listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                }

            }
        });

        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listFilterSubCategory.get(position).getSizeavailable().equalsIgnoreCase("yes"))
                {
                    restaurantDetailsAdapterInterface.getClickData(listFilterSubCategory.get(position).getItemID(), listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                    Intent i = new Intent(mContext, AddClickDetails.class);
                    i.putExtra("CLICKITEMID", listFilterSubCategory.get(position).getItemID());
                    i.putExtra("CLICKITEMNAME", listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                    i.putExtra("CLICKITEMPRICE", listFilterSubCategory.get(position).getRestaurantPizzaItemPrice());
                    i.putExtra("CLICKPIZZDESC", listFilterSubCategory.get(position).getResPizzaDescription());
                    i.putExtra("mainClickRestId", mainClickRestId);
                    i.putExtra("mainClickRestName", mainClickRestName);
                    mContext.startActivity(i);
                }
                else
                {
                   restaurantDetailsAdapterInterface.getClickData(listFilterSubCategory.get(position).getItemID(), listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return listFilterSubCategory.size();
    }

    class  Holder extends RecyclerView.ViewHolder{
        public CardView card_subCategory;
        private TextView tv_restaurant_name,tv_plus, tv_restaurant_pizza_des,tv_item_cost, tv_item_cost_old,tv_restaurant_address, tv_restaurant_miles, txt_view_timer, txt_rest_min_order,txt_rest_free_order, txt_discount_avail;
        private ImageView iv_restaurant_logo, shop_img_places_cat;
        private RatingBar ratingBar;
        private LinearLayout lnr_discount_avail;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);
            tv_restaurant_pizza_des = itemView.findViewById(R.id.tv_restaurant_pizza_des);
            tv_plus = itemView.findViewById(R.id.tv_plus);
            tv_item_cost = itemView.findViewById(R.id.tv_item_cost);
            tv_item_cost_old = itemView.findViewById(R.id.tv_item_cost_old);
            iv_restaurant_logo = itemView.findViewById(R.id.iv_restaurant_logo);

        }
    }
}
