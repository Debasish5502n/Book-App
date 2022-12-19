package com.example.bookapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.Model.BannerModel
import com.example.bookapp.R

class BannerAdapter(var context: Context,var mode: List<BannerModel>) : RecyclerView.Adapter<BannerAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.layout_banner_item,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Glide.with(context).load( mode[position].url).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return mode.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView:  ImageView= itemView.findViewById<ImageView?>(R.id.banner_image)

    }
}