package com.antarikshc.theguardiannews.ui

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.antarikshc.theguardiannews.R
import com.antarikshc.theguardiannews.model.NewsData

class CustomAdapter(context: Context, resource: Int, list: ArrayList<NewsData>) : ArrayAdapter<NewsData>(context, resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val dataModel = getItem(position)
        val viewHolder: ViewHolder

        val result: View

        if (convertView == null) {

            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.custom_list, parent, false)
            viewHolder.apply {
                coverImage = convertView.findViewById(R.id.cover_image)
                txtTitle = convertView.findViewById(R.id.txt_title)
                txtAuthor = convertView.findViewById(R.id.txt_author)
                txtTime = convertView.findViewById(R.id.txt_time)
                txtCat = convertView.findViewById(R.id.txt_category)
            }

            result = convertView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            result = convertView
        }

        if (dataModel != null) {
            viewHolder.coverImage?.setImageBitmap(dataModel.image)
        }

        viewHolder.txtTitle?.setText(dataModel!!.title)

        if (dataModel.author == null) {
            viewHolder.txtAuthor?.setVisibility(View.GONE)  //Lets not waste space if author is N/A
        } else {
            viewHolder.txtAuthor?.setText(dataModel.author)
        }

        viewHolder.txtCat?.setText(dataModel.category)
        viewHolder.txtTime?.setText(getRelativeTime(dataModel.timeInMillis))

        return result
    }

    private fun getRelativeTime(timeInMillis: Long?): String {

        val now = System.currentTimeMillis()

        val difference = now - timeInMillis!!

        val relativeTime: CharSequence
        if (difference < 86400001) {
            relativeTime = DateUtils.getRelativeTimeSpanString(timeInMillis, now, DateUtils.MINUTE_IN_MILLIS)
        } else {
            relativeTime = DateUtils.getRelativeTimeSpanString(timeInMillis, now, DateUtils.DAY_IN_MILLIS)
        }

        return relativeTime.toString()
    }

    internal class ViewHolder {
        internal var coverImage: ImageView? = null
        internal var txtTitle: TextView? = null
        internal var txtAuthor: TextView? = null
        internal var txtCat: TextView? = null
        internal var txtTime: TextView? = null
    }
}