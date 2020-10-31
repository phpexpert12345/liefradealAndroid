package com.app.liferdeal.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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
import com.app.liferdeal.model.restaurant.RestaurantMainModel;
import com.app.liferdeal.ui.activity.restaurant.RestaurantDetails;
import com.app.liferdeal.util.Constants;
import com.app.liferdeal.util.PrefsHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class RestaurantMainAdapter extends RecyclerView.Adapter<RestaurantMainAdapter.Holder> {
    private List<RestaurantMainModel.SearchResult> listCategory;
   // private List<ModelSubCategory>listFilterSubCategory;
    private Context mContext;
    private PrefsHelper prefsHelper;


    public RestaurantMainAdapter(Context context, List<RestaurantMainModel.SearchResult>listSubCategory){
        this.mContext=context;
        this.listCategory=listSubCategory;
        prefsHelper = new PrefsHelper(mContext);

    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_search_row,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.tv_restaurant_name.setText(listCategory.get(position).getRestaurantName());
        if (listCategory.get(position).getSponsorAvailable().equalsIgnoreCase("Yes"))
        {
            holder.lnr_sponsered.setVisibility(View.VISIBLE);
        }
        if (!listCategory.get(position).getHygienRatingValue().equalsIgnoreCase("0")) {
            holder.tv_restaurant_total_review.setText("(" + listCategory.get(position).getHygienRatingValue() +")");
        }
        if (listCategory.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("open"))
        {
            holder.lnr_open_close.setBackgroundResource(R.drawable.circle_background);
            holder.tv_item_discount_open_close.setText(listCategory.get(position).getRestaurantTimeStatus1());
        }
       else if (listCategory.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("Preorder"))
        {
            holder.lnr_open_close.setBackgroundResource(R.drawable.circle_back_orange);
            holder.tv_item_discount_open_close.setText(listCategory.get(position).getRestaurantTimeStatus1());
        }
        else if (listCategory.get(position).getRestaurantTimeStatus1().equalsIgnoreCase("closed"))
        {
            holder.lnr_open_close.setBackgroundResource(R.drawable.circle_back_red);
            holder.tv_item_discount_open_close.setText(listCategory.get(position).getRestaurantTimeStatus1());
        }

       if(listCategory.get(position).getRestaurantCuisine()!=null) {
           if (!listCategory.get(position).getRestaurantCuisine().equalsIgnoreCase(" ")) {
               holder.tv_restaurant_cusine_name.setText(listCategory.get(position).getRestaurantCuisine());

           } else {
               holder.tv_restaurant_cusine_name.setVisibility(View.GONE);
           }
       }
        Currency hh = Currency.getInstance(""+prefsHelper.getPref(Constants.APP_CURRENCY));
        String jj =  hh.getSymbol();
        holder.tv_restaurant_address.setText(listCategory.get(position).getRestaurantAddress());
        holder.txt_view_timer.setText(listCategory.get(position).getRestaurantAvarageDeliveryTime());
        holder.txt_rest_free_order.setText(jj + " " + listCategory.get(position).getRestaurantDeliverycharge());
        holder.txt_rest_min_order.setText(listCategory.get(position).getRestaurantMinimumorder());
        holder.tv_restaurant_miles.setText("(" + listCategory.get(position).getRestaurantDeliveryDistance() +")");
        if (listCategory.get(position).getRestaurantLogo()!=null) {
            Glide.with(mContext).load(Uri.parse(listCategory.get(position).getRestaurantLogo())).into(holder.iv_restaurant_logo);
        }

        if (!listCategory.get(position).getRatingValue().equalsIgnoreCase("null"))
        {
            holder.ratingBar.setRating(Float.parseFloat(listCategory.get(position).getRatingValue()));
        }

        if (!listCategory.get(position).getDiscountAvailable().equalsIgnoreCase(""))
        {
            holder.lnr_discount_avail.setVisibility(View.VISIBLE);
            holder.txt_discount_avail.setText(listCategory.get(position).getDiscountAvailable() + " % Off");
        }

        System.out.println("===== rating vlue : " + listCategory.get(position).getRatingValue());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(mContext, RestaurantDetails.class);
                i.putExtra("RESTID", listCategory.get(position).getId());
                i.putExtra("RESTName", listCategory.get(position).getRestaurantName());
                i.putExtra("RESTCOVER", listCategory.get(position).getRestaurantCover());
                i.putExtra("RESTLOGO", listCategory.get(position).getRestaurantLogo());
                i.putExtra("RESTADDRESS", listCategory.get(position).getRestaurantAddress());
                i.putExtra("RESTISOPEN", listCategory.get(position).getRestaurantTimeStatus1());
                i.putExtra("RATINGBARDATA", listCategory.get(position).getRatingValue());

                mContext.startActivity(i);
            }
        });
      /*  holder.card_subCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityBase.listDiscription.clear();
                activityBase.SubParentID = listSubCategory.get(position).getSubParentID();
                activityBase.setCurrent_fragment(new FragmentItemList(),FragmentItemList.FRAG_TITEL);
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    class  Holder extends RecyclerView.ViewHolder{
        public CardView card_subCategory;
        private TextView tv_restaurant_total_review,tv_item_discount_open_close,tv_restaurant_name, tv_restaurant_cusine_name, tv_restaurant_address, tv_restaurant_miles, txt_view_timer, txt_rest_min_order,txt_rest_free_order, txt_discount_avail;
        private ImageView iv_restaurant_logo;
        private RatingBar ratingBar;
        private LinearLayout lnr_discount_avail,lnr_open_close, lnr_sponsered;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_restaurant_name = itemView.findViewById(R.id.tv_restaurant_name);
            tv_item_discount_open_close = itemView.findViewById(R.id.tv_item_discount_open_close);
            tv_restaurant_cusine_name = itemView.findViewById(R.id.tv_restaurant_cusine_name);
            tv_restaurant_address = itemView.findViewById(R.id.tv_restaurant_address);
            tv_restaurant_miles = itemView.findViewById(R.id.tv_restaurant_miles);
            txt_view_timer = itemView.findViewById(R.id.txt_view_timer);
            txt_rest_min_order = itemView.findViewById(R.id.txt_rest_min_order);
            txt_rest_free_order = itemView.findViewById(R.id.txt_rest_free_order);
            lnr_discount_avail = itemView.findViewById(R.id.lnr_discount_avail);
            txt_discount_avail = itemView.findViewById(R.id.txt_discount_avail);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            iv_restaurant_logo = itemView.findViewById(R.id.iv_restaurant_logo);
            lnr_open_close = itemView.findViewById(R.id.lnr_open_close);
            tv_restaurant_total_review = itemView.findViewById(R.id.tv_restaurant_total_review);
            lnr_sponsered = itemView.findViewById(R.id.lnr_sponsered);
        }
    }
    public void updateList(List<RestaurantMainModel.SearchResult> list, String places){
        System.out.println("===== checkfromwher places 1 : " + places);
        listCategory = list;
        notifyDataSetChanged();
    }
}
