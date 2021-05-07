package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.displayphonecontacts.DisplayPhoneContactsActivity
import com.decagon.android.sq007.models.ContactsModel
import com.decagon.android.sq007.util.ContactsListAdapter
import com.decagon.android.sq007.util.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ContactsListActivity : AppCompatActivity(), OnItemClickListener {

    var contactsList: ArrayList<ContactsModel> = ArrayList()
    lateinit var databaseRef: DatabaseReference
    lateinit var addContact: FloatingActionButton

//    lateinit var contacts: ArrayList<ContactsModel>

//    private val contactsAdapter = ContactsListAdapter(contactsList, this)
    private lateinit var contactsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)

        contactsRecyclerView = findViewById(R.id.rv_contacts)

        setupAddContactFab()
        setupContacts()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contacts_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_display_contacts -> {
                val myContactsIntent = Intent(this, DisplayPhoneContactsActivity::class.java)
                startActivity(myContactsIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAddContactFab() {

        addContact = findViewById(R.id.fab_add_contacts)
        addContact.setOnClickListener {
            val intent = Intent(this, CreateContactActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onContactClick(position: Int, contacts: ArrayList<ContactsModel>) {
        val contact = contacts[position]

        val contactIcon = findViewById<TextView>(R.id.tv_profile_icon)
        contactIcon.text = contact.firstName!![0].toString().trim()
        val alphabet = contactIcon.text.toString()

        val intent = Intent(this, ContactDetailActivity::class.java)
        intent.putExtra("Contactdetail", contact)
        intent.putExtra("Icon", alphabet)

        startActivity(intent)
    }

    private fun setupContacts() {

        contactsRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        databaseRef = FirebaseDatabase.getInstance().getReference("CONTACT")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                contactsList.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val contact = userSnapshot.getValue<ContactsModel>()
                        if (contact?.phoneNumber != null) {
                            contactsList.add(contact)
                        }
                    }
                    val adapter = ContactsListAdapter(contactsList, this@ContactsListActivity)
                    contactsList.sortWith(
                        compareBy {
                            it.firstName?.capitalize()
                        }
                    )
                    contactsRecyclerView.adapter = adapter
                }
            }
        })
    }
}
