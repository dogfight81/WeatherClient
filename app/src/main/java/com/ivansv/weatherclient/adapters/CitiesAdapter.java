package com.ivansv.weatherclient.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivansv.weatherclient.R;
import com.ivansv.weatherclient.entities.CityRealm;
import com.ivansv.weatherclient.interfaces.OnRecyclerItemClickListener;

import io.realm.RealmResults;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder>{

    private RealmResults<CityRealm> listData;
    private OnRecyclerItemClickListener onItemClickListener;
    private String queryText;

    public void setListData(RealmResults<CityRealm> data) {
        this.listData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public CityRealm getCity(int position) {
        return listData.get(position);
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
            tvCountry = (TextView) itemView.findViewById(R.id.tv_current_condition);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        void bind() {
            CityRealm city = listData.get(getAdapterPosition());
//            tvCityName.setText(city.getCityName());
//            tvCountry.setText(city.getCountry());

            String cityName = city.getCityName();
            String lowerCaseCityName = cityName.toLowerCase();
            if (queryText != null && queryText.length() > 0 && lowerCaseCityName.contains(queryText)) {
                SpannableString highlightedText = new SpannableString(cityName);
                int spanStart = lowerCaseCityName.indexOf(queryText);
                while (spanStart >= 0) {
                    int spanEnd = spanStart + queryText.length();
                    highlightedText.setSpan(new BackgroundColorSpan(Color.YELLOW), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanStart = lowerCaseCityName.indexOf(queryText, spanEnd);
                }
                tvCityName.setText(highlightedText);
            } else {
                tvCityName.setText(cityName);
            }
            tvCountry.setText(city.getCountry());

        }

    }
}
