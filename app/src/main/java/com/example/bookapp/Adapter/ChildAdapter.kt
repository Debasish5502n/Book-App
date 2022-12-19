package com.example.bookapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.Model.BannerModel
import com.example.bookapp.Model.ChildModel
import com.example.bookapp.R

class ChildAdapter(var context: Context, var model: List<ChildModel>) : RecyclerView.Adapter<ChildAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.rv_child_layout,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Glide.with(context).load( model[position].image).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView:  ImageView= itemView.findViewById<ImageView?>(R.id.iv_child_item)

    }
}