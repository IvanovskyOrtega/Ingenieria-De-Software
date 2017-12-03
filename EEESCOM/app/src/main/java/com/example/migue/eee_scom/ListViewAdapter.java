package com.example.migue.eee_scom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<String> ComponentsList = null;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context, List<String> ComponentsList) {
        mContext = context;
        this.ComponentsList = ComponentsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(ComponentsList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return ComponentsList.size();
    }

    @Override
    public String getItem(int position) {
        return ComponentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(ComponentsList.get(position));
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ComponentsList.clear();
        if (charText.length() == 0) {
            ComponentsList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
                if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                    ComponentsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
