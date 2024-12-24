package com.example.dz16contentprovider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView

class ContactAdapter (private val contacts: List<ContactModel>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private lateinit var callContact: CallContact
    private lateinit var messenger: MessengerContact

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        callContact = recyclerView.context as CallContact
        messenger = recyclerView.context as MessengerContact
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameMTV = itemView.findViewById<MaterialTextView>(R.id.name_mtv)
        val phoneMTV = itemView.findViewById<MaterialTextView>(R.id.phone_mtv)
        val callFAB = itemView.findViewById<FloatingActionButton>(R.id.call_fab)
        val smsFAB = itemView.findViewById<FloatingActionButton>(R.id.sms_fab)

        fun onBind(item: ContactModel) {
            callFAB.visibility = View.GONE
            smsFAB.visibility = View.GONE
            nameMTV.text = item.name
            phoneMTV.text = item.phone
            if (item.phone != null) {
                itemView.setOnClickListener {
                    callFAB.visibility = View.VISIBLE
                    smsFAB.visibility = View.VISIBLE
                }
                callFAB.setOnClickListener { callContact.call(item.phone) }
                smsFAB.setOnClickListener { messenger.createMessage(item.phone) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}