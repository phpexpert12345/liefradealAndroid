package com.app.liferdeal.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.app.liferdeal.model.restaurant.RestaurantDetailsModel;
import com.app.liferdeal.ui.activity.restaurant.AddClickDetails;
import com.app.liferdeal.ui.activity.restaurant.RestaurantDetails;
import com.app.liferdeal.ui.activity.restaurant.RestaurantPhotoGallery;
import com.bumptech.glide.Glide;

import java.util.List;

    public class Restaurant_Details_quick extends RecyclerView.Adapter<Restaurant_Details_quick.Holder> {
        private List<RestaurantDetailsModel.RestaurantMencategory> listCategory;
        // private List<ModelSubCategory>listFilterSubCategory;
        private Context mContext;
        private RestaurantDetailsQuickInterface restaurantDetailsQuickInterface;
        private int selectedPosition;
        private int selectedPositionItem=0;
        public static Restaurant_Details_quick.Holder mHolder;
        public interface RestaurantDetailsQuickInterface
        {
            public void getRestaaurantQuickClickData(String restId, int catId, String categoryImage);

        }
        public Restaurant_Details_quick(Context context, List<RestaurantDetailsModel.RestaurantMencategory>listSubCategory, RestaurantDetailsQuickInterface restaurantDetailsQuickInterface1){
            this.mContext=context;
            this.restaurantDetailsQuickInterface = restaurantDetailsQuickInterface1;
            this.listCategory=listSubCategory;
            //this.selectedPosition = -1;
            this.selectedPosition = 0;

        }
        @NonNull
        @Override
        public Restaurant_Details_quick.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_details_quick,parent,false);
            return new Restaurant_Details_quick.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Restaurant_Details_quick.Holder holder, final int position) {
            holder.servicename.setText(listCategory.get(position).getCategoryName());
            System.out.println("===== positon in quickdetails : " + position);
            System.out.println("===== positon in quickdetails1 : " + selectedPosition);
          /* if (selectedPositionItem == position)
           {
               holder.servicename.setBackgroundResource(R.drawable.circle_background);
                restaurantDetailsQuickInterface.getRestaaurantQuickClickData(listCategory.get(0).getRestaurantId(), listCategory.get(0).getCategoryID());
           }*/
            if (position == selectedPosition) {
                mHolder = holder;

                updateSelectedView();
            } else {
                updateNonSelected(holder);
            }

            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    restaurantDetailsQuickInterface.getRestaaurantQuickClickData(listCategory.get(position).getRestaurantId(), listCategory.get(position).getCategoryID());


                }
            });*/


           /* holder.img_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, RestaurantPhotoGallery.class);
                    mContext.startActivity(i);
                }
            });*/

        }


        private void updateNonSelected(Holder holder) {
            if (selectedPosition == -1)
            {
                    }
            else
            {
                holder.servicename.setBackgroundColor(mContext.getResources().getColor(R.color.color_lighteGray));
                mHolder.servicename.setTextColor(Color.BLACK);
               /* if (selectedPosition == 0) {
                    holder.servicename.setTextColor(Color.WHITE);
                }
                else
                {
                    holder.servicename.setBackgroundColor(mContext.getResources().getColor(R.color.color_lighteGray));
                    mHolder.servicename.setTextColor(Color.BLACK);
                }*/
            }

        }


        private void updateSelectedView() {
            System.out.println("==== selected positin in updateselectedview : " + selectedPosition);
            if (selectedPosition==0)
            {
                mHolder.servicename.setTextColor(Color.WHITE);
            }
            mHolder.servicename.setBackgroundResource(R.drawable.circle_background);
            mHolder.servicename.setTextColor(Color.WHITE);
            restaurantDetailsQuickInterface.getRestaaurantQuickClickData(listCategory.get(selectedPosition).getRestaurantId(), listCategory.get(selectedPosition).getCategoryID(), listCategory.get(selectedPosition).getCategoryImg());

        }

        @Override
        public int getItemCount() {
            return listCategory.size();
        }

        @Override
        public void onViewRecycled(@NonNull Holder holder) {
            super.onViewRecycled(holder);
            if (selectedPosition == holder.getAdapterPosition()) {

                mHolder = holder;
            }
        }

        class  Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public CardView card_subCategory;
            private TextView servicename;
            private ImageView iv_restaurant_logo;
            private RatingBar ratingBar;
            private LinearLayout lnr_discount_avail, img_view;
            public Holder(@NonNull View itemView) {
                super(itemView);
                servicename = itemView.findViewById(R.id.servicename);
                servicename.setOnClickListener(this);


            }
            @Override
            public void onClick(View view) {
                if (getAdapterPosition() == selectedPosition)
                {


                }
                else
                    {
                    selectedPosition = getAdapterPosition();

                    if (null != mHolder) {
                        updateNonSelected(mHolder);
                    }
                    mHolder = this;
                }
                updateSelectedView();
            }

        }
    }

