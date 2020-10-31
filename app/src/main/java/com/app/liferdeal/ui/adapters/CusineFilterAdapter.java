package com.app.liferdeal.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.liferdeal.R;
import com.app.liferdeal.model.LanguageModel;
import com.app.liferdeal.model.restaurant.CusineFilterModel;
import com.app.liferdeal.model.restaurant.SubItemsRecord;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CusineFilterAdapter extends RecyclerView.Adapter<CusineFilterAdapter.Holder> {
    private List<CusineFilterModel.CuisineList> listCategory;
    private List<SubItemsRecord> listFilterSubCategory;
    private Context mContext;
    ArrayList<String> selected_cusines;
    ArrayList<Integer> selected_cusines_id;
    private CusineFilterAdapterInterface cusineFilterAdapterInterface;

    public interface CusineFilterAdapterInterface {
        void getClickData( ArrayList<Integer> extraId,  ArrayList<String> extraName);
    }


    public CusineFilterAdapter(Context context, List<CusineFilterModel.CuisineList> listSubCategory, CusineFilterAdapterInterface cusineFilterAdapterInterface1) {
        this.mContext = context;
        this.listCategory = listSubCategory;
        this.selected_cusines = new ArrayList<>();
        this.selected_cusines_id = new ArrayList<>();
        //  this.listFilterSubCategory = listFilterSubCategory;
        cusineFilterAdapterInterface = cusineFilterAdapterInterface1;

    }

    @NonNull
    @Override
    public CusineFilterAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cusinefilterrow, parent, false);
        return new CusineFilterAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CusineFilterAdapter.Holder holder, final int position) {
        holder.txtitemname.setText(listCategory.get(position).getCuisineName());

        holder.cbitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cbitem.isChecked()) {
                    System.out.println("==== checkbox is click");
                    selected_cusines_id.add(listCategory.get(position).getId());
                    selected_cusines.add(holder.txtitemname.getText().toString());
                    cusineFilterAdapterInterface.getClickData(selected_cusines_id, selected_cusines);
                }
                else
                {
                    selected_cusines_id.remove(listCategory.get(position).getId());
                    selected_cusines.remove(holder.txtitemname.getText().toString());
                    cusineFilterAdapterInterface.getClickData(selected_cusines_id, selected_cusines);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView txtitemname;
        private CheckBox cbitem;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtitemname = itemView.findViewById(R.id.txtitemname);
            cbitem = (CheckBox) itemView.findViewById(R.id.cbitem);

          //  iv_restaurant_logo = itemView.findViewById(R.id.iv_restaurant_logo);
        }
    }
}
