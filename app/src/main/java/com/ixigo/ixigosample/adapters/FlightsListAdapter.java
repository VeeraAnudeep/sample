package com.ixigo.ixigosample.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixigo.ixigosample.R;
import com.ixigo.ixigosample.dbManager.contracts.FlightsContract;

/**
 * Created by veera on 26/7/16.
 */
public class FlightsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Cursor mCursor;
    Context mContext;
    LayoutInflater layoutInflater;

    public FlightsListAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.p_list_item, parent, false);
        return new FlightItemViewHolder(view);
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        if (holder instanceof FlightItemViewHolder) {
            FlightItemViewHolder viewHolder = (FlightItemViewHolder) holder;
            viewHolder.price.setText(mCursor.getString(FlightsContract.FlightDataEntry.INDEX_PRICE));
            viewHolder.klass.setText(mCursor.getString(FlightsContract.FlightDataEntry.INDEX_KLASS));
            viewHolder.startTime.setText(mCursor.getString(FlightsContract.FlightDataEntry.INDEX_TAKE_OF_TIME));
            viewHolder.endTime.setText(mCursor.getString(FlightsContract.FlightDataEntry.INDEX_LANDING_TIME));
            viewHolder.airlines.setText(mCursor.getString(FlightsContract.FlightDataEntry.INDEX_AIRLINE));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    public class FlightItemViewHolder extends RecyclerView.ViewHolder {
        public TextView startTime;
        public TextView endTime;
        public TextView klass;
        public TextView price;
        public TextView airlines;

        public FlightItemViewHolder(View itemView) {
            super(itemView);
            airlines = (TextView) itemView.findViewById(R.id.airline);
            startTime = (TextView) itemView.findViewById(R.id.arrivalTime);
            endTime = (TextView) itemView.findViewById(R.id.departureTime);
            klass = (TextView) itemView.findViewById(R.id.klass);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}
