package com.example.dat.contentprovider1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dat.contentprovider1.Model.Animal;
import com.example.dat.contentprovider1.R;

import java.util.ArrayList;

/**
 * Created by DAT on 10/29/2015.
 */
public class CustomAdapterAnimals extends BaseAdapter {
    Context context;
    ArrayList<Animal> animals;

    public CustomAdapterAnimals(Context context, ArrayList<Animal> data) {
        this.context = context;
        this.animals = data;
    }

    public void clearAll() {
        animals.clear();
    }

    public void addAll(ArrayList<Animal> data) {
        animals.addAll(data);
    }

    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public Object getItem(int position) {
        return animals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Animal animal = (Animal) getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_animal, parent, false);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewName.setText(animal.getName());
        viewHolder.textViewType.setText(animal.getType());

        return convertView;
    }

    private ViewHolder createViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.textViewName = (TextView) view.findViewById(R.id.textViewCustomItemName);
        holder.textViewType = (TextView) view.findViewById(R.id.textViewCustomItemType);

        return holder;
    }

    static class ViewHolder {
        TextView textViewName;
        TextView textViewType;
    }
}
