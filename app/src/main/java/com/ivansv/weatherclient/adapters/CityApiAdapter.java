package com.ivansv.weatherclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivansv.weatherclient.entities.CityApi;
import com.ivansv.weatherclient.interfaces.OnRecyclerItemClickListener;


public class CityApiAdapter extends RecyclerView.Adapter<CityApiAdapter.CityViewHolder> {

    private CityApi[] listData;
    private OnRecyclerItemClickListener listener;

    public void setListData(CityApi[] listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public CityApi getCity(int position) {
        return listData[position];
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        CityApi city = listData[position];
        holder.tvCity.setText(city.getName());
        holder.tvCountry.setText(city.getCountry().getLocalizedName());

    }

    @Override
    public int getItemCount() {
        if (listData == null) {
            return 0;
        }
        return listData.length;
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        TextView tvCity;
        TextView tvCountry;

        CityViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(android.R.id.text1);
            tvCountry = (TextView) itemView.findViewById(android.R.id.text2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
