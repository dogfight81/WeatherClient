package com.ivansv.weatherclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.entities.City;
import com.ivansv.weatherclient.interfaces.OnRecyclerItemClickListener;

import io.realm.RealmResults;

/**
 * Created by Ivan on 30-May-17.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder>{

    private RealmResults<City> listData;
    private OnRecyclerItemClickListener onItemClickListener;

    public void setListData(RealmResults<City> data) {
        this.listData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (listData != null) {
            return listData.size();
        }
        return 0;
    }

    class CityViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCityName;
        private TextView tvCountry;

        CityViewHolder(View itemView) {
            super(itemView);
            tvCityName = (TextView) itemView.findViewById(R.id.tv_city_name);
            tvCountry = (TextView) itemView.findViewById(R.id.tv_country);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(listData.get(getAdapterPosition()));
                    }
                }
            });
        }

        void bind() {
            City city = listData.get(getAdapterPosition());
            tvCityName.setText(city.getCityName());
            tvCountry.setText(city.getCountry());
        }

    }
}
