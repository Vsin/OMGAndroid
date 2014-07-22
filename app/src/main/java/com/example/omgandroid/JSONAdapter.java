package com.example.omgandroid;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Vsin on 7/21/14.
 */
public class JSONAdapter extends BaseAdapter {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;
    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
            return mJsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int i) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}
