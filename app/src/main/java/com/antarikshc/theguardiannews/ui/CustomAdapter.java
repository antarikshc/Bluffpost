package com.antarikshc.theguardiannews.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antarikshc.theguardiannews.R;
import com.antarikshc.theguardiannews.model.NewsData;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<NewsData> {

    private ArrayList<NewsData> dataSet;
    private Context mContext;

    private static class ViewHolder {
        ImageView coverImage;
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtCat;
        TextView txtTime;
    }

    CustomAdapter(@NonNull Context context, ArrayList<NewsData> dataSet) {
        super(context, R.layout.custom_list, dataSet);
        this.dataSet = dataSet;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        NewsData dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list, parent, false);
            viewHolder.coverImage = convertView.findViewById(R.id.cover_image);
            viewHolder.txtTitle = convertView.findViewById(R.id.txt_title);
            viewHolder.txtAuthor = convertView.findViewById(R.id.txt_author);
            viewHolder.txtTime = convertView.findViewById(R.id.txt_time);
            viewHolder.txtCat = convertView.findViewById(R.id.txt_category);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        if (dataModel != null) {
            viewHolder.coverImage.setImageBitmap(dataModel.getImage());
        }

        viewHolder.txtTitle.setText(dataModel.getTitle());

        if (dataModel.getAuthor() == null) {
            viewHolder.txtAuthor.setVisibility(View.GONE);  //Lets not waste space if author is N/A
        } else {
            viewHolder.txtAuthor.setText(dataModel.getAuthor());
        }

        viewHolder.txtCat.setText(dataModel.getCategory());

        viewHolder.txtTime.setText(getRelativeTime(dataModel.getTimeInMillis()));

        return convertView;
    }

    private String getRelativeTime(Long timeInMillis) {

        Long now = System.currentTimeMillis();

        Long difference = now - timeInMillis;

        CharSequence relativeTime;
        if (difference < 86400001) {
            relativeTime = DateUtils.getRelativeTimeSpanString(timeInMillis, now, DateUtils.MINUTE_IN_MILLIS);
        } else {
            relativeTime = DateUtils.getRelativeTimeSpanString(timeInMillis, now, DateUtils.DAY_IN_MILLIS);
        }

        return relativeTime.toString();
    }
}
