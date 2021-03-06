package com.decagon.android.sq007.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.models.ContactsModel
import kotlinx.android.synthetic.main.contacts_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class ContactsListAdapter(
    private val contacts: ArrayList<ContactsModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contacts_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            full_name.text = "${contacts[position].firstName?.capitalize()} ${contacts[position].lastName?.capitalize()}"
            findViewById<TextView>(R.id.tv_profile_icon).text =
                contacts[position].firstName?.take(1)?.toUpperCase(Locale.ROOT)
        }
    }

    inner class ViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onContactClick(position, contacts)
            }
        }
    }
}

interface OnItemClickListener {
    fun onContactClick(position: Int, contacts: ArrayList<ContactsModel>)
}
