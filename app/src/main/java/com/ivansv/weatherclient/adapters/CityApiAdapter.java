package com.ivansv.weatherclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivansv.weatherclient.entities.CityApi;

/**
 * Created by Ivan on 6/1/2017.
 */

public class CityApiAdapter extends RecyclerView.Adapter<CityApiAdapter.Holder> {

    private CityApi[] data;

    public CityApiAdapter(CityApi[] data) {
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityApiAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CityApi city = data[position];
        holder.city.setText(city.getName());
        holder.country.setText(city.getCountry().getLocalizedName());

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView city;
        public TextView country;

        public Holder(View itemView) {
            super(itemView);
            city = (TextView) itemView.findViewById(android.R.id.text1);
            country = (TextView) itemView.findViewById(android.R.id.text2);
        }
    }
}
