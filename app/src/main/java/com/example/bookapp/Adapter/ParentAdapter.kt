package com.example.bookapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.Model.BannerModel
import com.example.bookapp.Model.ChildModel
import com.example.bookapp.Model.ParentModel
import com.example.bookapp.R

class ParentAdapter(var context: Context, var model: List<ParentModel>) : RecyclerView.Adapter<ParentAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.parent_rv_layout,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.title.text = model[position].title

        var childAdapter :ChildAdapter
        childAdapter = ChildAdapter(context,model[position].childModelClass)
        val childLayout = LinearLayoutManager(context)
        childLayout.orientation = RecyclerView.HORIZONTAL
        holder.rv_child.layoutManager = childLayout
        holder.rv_child.adapter = childAdapter
        childAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return model.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title:  TextView = itemView.findViewById<TextView?>(R.id.rv_parent_title)
        var rv_child:  RecyclerView = itemView.findViewById<RecyclerView?>(R.id.rv_child)

    }
}