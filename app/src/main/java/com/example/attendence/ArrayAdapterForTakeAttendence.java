package com.example.attendence;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayAdapterForTakeAttendence extends BaseAdapter {

    private Context context;
    public static ArrayList<ModelClass> model_list;

    public ArrayAdapterForTakeAttendence(Context context,ArrayList<ModelClass> model_list)
    {
        this.context = context;
        this.model_list = model_list;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return model_list.size();
    }

    @Override
    public Object getItem(int position) {
        return model_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_take_attendence, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb);
            holder.sl = (TextView) convertView.findViewById(R.id.sl_no);
            holder.id = (TextView) convertView.findViewById(R.id.stud_id);
            holder.name = (TextView) convertView.findViewById(R.id.stud_name);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.sl.setText(model_list.get(position).getSl_no());
        holder.id.setText(model_list.get(position).getId_no());
        holder.name.setText(model_list.get(position).getName());

        holder.checkBox.setChecked(model_list.get(position).getSelected());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.checkBox.getTag(R.integer.btnplusview);
                Integer pos = (Integer)  holder.checkBox.getTag();

                //Toast.makeText(context, "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();

                if(model_list.get(pos).getSelected())
                {
                    model_list.get(pos).setSelected(false);
                }else {
                    model_list.get(pos).setSelected(true);
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {

        private CheckBox checkBox;
        private TextView sl,id,name;

    }
}
