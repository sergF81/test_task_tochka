package com.github


import android.content.Context

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View


class RecyclerViewAdapter(
    context: Context?,
    private val userArray: MutableList<String>,
    private val myListener: MyListener
) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private val inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.recyclerview_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = userArray[position]
        holder.nameView.setOnClickListener(View.OnClickListener {
            myListener.MyClick(userArray, position)
        })
    }

    override fun getItemCount(): Int {
        return userArray.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.textViewUserRow)
    }

    init {
        inflater = LayoutInflater.from(context)
    }

    interface MyListener {
        fun MyClick(userArray: MutableList<String>, position: Int)
    }
}