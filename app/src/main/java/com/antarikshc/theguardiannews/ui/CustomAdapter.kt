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
import java.util.*

class CustomAdapter(
        mContext: Context,
        dataSet: ArrayList<NewsData>
) : ArrayAdapter<NewsData>(mContext, R.layout.custom_list, dataSet) {

    internal class ViewHolder {
        lateinit var coverImage: ImageView
        lateinit var txtTitle: TextView
        lateinit var txtAuthor: TextView
        lateinit var txtCat: TextView
        lateinit var txtTime: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dataModel = getItem(position)
        var viewHolder = ViewHolder()

        val result = if (convertView == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false)
            viewHolder.coverImage = view.findViewById(R.id.cover_image)
            viewHolder.txtTitle = view.findViewById(R.id.txt_title)
            viewHolder.txtAuthor = view.findViewById(R.id.txt_author)
            viewHolder.txtTime = view.findViewById(R.id.txt_time)
            viewHolder.txtCat = view.findViewById(R.id.txt_category)
            view.tag = viewHolder
            view
        } else {
            viewHolder = convertView.tag as ViewHolder
            convertView
        }

        viewHolder.run {
            dataModel?.let {
                coverImage.setImageBitmap(it.image)
                txtTitle.text = it.title
                txtAuthor.also { txt ->
                    if (it.author == null) {
                        txt.text = it.author
                    } else {
                        txt.visibility = View.GONE //Lets not waste space if author is N/A
                    }
                }
                txtCat.text = it.category
                txtTime.text = it.timeInMillis?.let { time ->
                    getRelativeTime(time)
                }
            }
        }

        return result
    }

    private fun getRelativeTime(timeInMillis: Long): String {
        val now = System.currentTimeMillis()
        val difference = now - timeInMillis
        return DateUtils.getRelativeTimeSpanString(timeInMillis, now, if (difference < 86400001) {
            DateUtils.MINUTE_IN_MILLIS
        } else {
            DateUtils.DAY_IN_MILLIS
        }).toString()
    }
}
