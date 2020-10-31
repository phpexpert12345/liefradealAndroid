package com.app.liferdeal.ui.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.app.liferdeal.model.LanguageModel;
import com.app.liferdeal.model.restaurant.RestMenuCat;
import com.app.liferdeal.model.restaurant.SubItemsRecord;
import com.app.liferdeal.ui.activity.restaurant.AddClickDetails;
import com.app.liferdeal.ui.activity.restaurant.RestaurantDetails;
import com.bumptech.glide.Glide;

import java.util.List;

public class LanguageAdapter  extends RecyclerView.Adapter<LanguageAdapter.Holder> {
    private List<LanguageModel.LanguageListList> listCategory;
    private List<SubItemsRecord> listFilterSubCategory;
    private Context mContext;
    private RestaurantDetailsAdapter.RestaurantDetailsAdapterInterface restaurantDetailsAdapterInterface;

    public interface RestaurantDetailsAdapterInterface {
        void getClickData(int otemId, String itemName);
    }


    public LanguageAdapter(Context context, List<LanguageModel.LanguageListList> listSubCategory) {
        this.mContext = context;
        this.listCategory = listSubCategory;
      //  this.listFilterSubCategory = listFilterSubCategory;
      //  restaurantDetailsAdapterInterface = restaurantDetailsAdapterInterface1;

    }

    @NonNull
    @Override
    public LanguageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_fragment_row, parent, false);
        return new LanguageAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.Holder holder, final int position) {
        holder.tv_restaurant_name.setText(listCategory.get(position).getLangName());

        if (listCategory.get(position).getLangIcon() != null) {
            Glide.with(mContext).load(Uri.parse(listCategory.get(position).getLangIcon())).into(holder.iv_restaurant_logo);
        }



  /*      holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listFilterSubCategory.get(position).getSizeavailable().equalsIgnoreCase("yes")) {
                    restaurantDetailsAdapterInterface.getClickData(listFilterSubCategory.get(position).getItemID(), listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                    Intent i = new Intent(mContext, AddClickDetails.class);
                    i.putExtra("CLICKITEMID", listFilterSubCategory.get(position).getItemID());
                    i.putExtra("CLICKITEMNAME", listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                    mContext.startActivity(i);
                } else {
                    restaurantDetailsAdapterInterface.getClickData(listFilterSubCategory.get(position).getItemID(), listFilterSubCategory.get(position).getRestaurantPizzaItemName());
                }

            }
        });*/

    }


    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        public CardView card_subCategory;
        private TextView tv_restaurant_name, tv_plus, tv_restaurant_pizza_des, tv_restaurant_address, tv_restaurant_miles, txt_view_timer, txt_rest_min_order, txt_rest_free_order, txt_discount_avail;
        private ImageView iv_restaurant_logo;
        private RatingBar ratingBar;
        private LinearLayout lnr_discount_avail;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);

            iv_restaurant_logo = itemView.findViewById(R.id.iv_restaurant_logo);
        }
    }
}
