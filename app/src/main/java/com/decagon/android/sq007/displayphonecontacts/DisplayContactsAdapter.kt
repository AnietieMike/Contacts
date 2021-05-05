package com.decagon.android.sq007.displayphonecontacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import kotlinx.android.synthetic.main.display_list_item.view.*

class DisplayContactsAdapter(private val people: ArrayList<DisplayContactsModel>) : RecyclerView.Adapter<DisplayContactsAdapter.ViewHolder>() {

    private var onCallListener: OnCallListener<DisplayContactsModel>? = null

    fun setListener(onCallListener: OnCallListener<DisplayContactsModel>) {
        this.onCallListener = onCallListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.display_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = people[position]
        holder.bindItems(contact)
        holder.itemView.findViewById<ImageButton>(R.id.ib_call).setOnClickListener {
            if (onCallListener != null) {
                onCallListener!!.onCall(contact)
            }
        }
        holder.itemView.findViewById<ImageButton>(R.id.ib_message).setOnClickListener {
            if (onCallListener != null) {
                onCallListener!!.onMessage(contact)
            }
        }
    }

    override fun getItemCount(): Int {
        return people.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(contact: DisplayContactsModel) {
            val tvName = itemView.findViewById<TextView>(R.id.tv_name)
            val tvNumber = itemView.findViewById<TextView>(R.id.tv_number)
            tvName.text = contact.name
            tvNumber.text = contact.number
        }
    }
}

interface OnCallListener<T> {

    fun onCall(t: T)

    fun onMessage(t: T)
}
