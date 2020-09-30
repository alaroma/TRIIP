package com.example.triip;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<ListAdapter> {

    private LayoutInflater inflater;
    private int layout;
    private List<ListAdapter> labels;

    public Adapter(Context context, int resource, ArrayList<ListAdapter> labels) {
        super(context, resource, labels);
        this.labels = labels;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        @SuppressLint("ViewHolder") View view=inflater.inflate(this.layout, parent, false);

        ImageView labelView = (ImageView) view.findViewById(R.id.photocar);
        TextView nameView = (TextView) view.findViewById(R.id.textView_labelname);
        TextView dataView = (TextView) view.findViewById(R.id.textView_datalabel);
        TextView tagsView = (TextView) view.findViewById(R.id.textView_tagslabel);

        ListAdapter state = labels.get(position);

       // labelView.setImageURI(state.getImage());
      //  labelView.setImageBitmap(state.getImage());
        nameView.setText(state.getName());
        dataView.setText(state.getData());
        tagsView.setText(state.getTag());
        return view;
    }
}