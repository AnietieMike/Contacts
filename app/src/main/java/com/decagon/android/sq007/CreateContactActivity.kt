package com.decagon.android.sq007

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decagon.android.sq007.models.ContactsModel
import com.google.firebase.database.FirebaseDatabase

class CreateContactActivity : AppCompatActivity() {

    private lateinit var saveContact: Button

//    private lateinit var viewModel: ContactsViewModel

    private lateinit var firstname: EditText
    private lateinit var lastname: EditText
    private lateinit var phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contact)

        saveContact = findViewById(R.id.btn_save_contact)

        firstname = findViewById(R.id.et_first_name)
        lastname = findViewById(R.id.et_last_name)
        phone = findViewById(R.id.et_phone_number)

        val test = intent.getStringExtra("TESTING")
        if (test == null) {
            Log.d("saveContact", "onCreate: ")
            saveContacts()
        } else {
            Log.d("editContact", "onCreate: ")
            editContacts()
        }

//        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
//        viewModel.result.observe(this, Observer {
//            val message = if (it == null) {
//                getString(R.string.added_contact)
//            } else {
//                getString(R.string.error, it.message)
//            }
//
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        })
    }

    fun saveContacts() {

        saveContact.setOnClickListener {

            val dbContacts = FirebaseDatabase.getInstance().reference

            val firstName = firstname.text.toString().trim()
            val lastName = lastname.text.toString().trim()
            val phoneNumber = phone.text.toString().trim()

            if (firstName.isEmpty()) {
                firstname.error = "This field is required"
                return@setOnClickListener
            }
            if (phoneNumber.isEmpty()) {
                phone.error = "This field is absolutely required!"
                return@setOnClickListener
            }

            val contact = ContactsModel(firstName = firstName, lastName = lastName, phoneNumber = phoneNumber)
            contact.id = dbContacts.child("CONTACT").push().key

            Log.d("CreateContact", "saveContacts: $contact")

            dbContacts.child("CONTACT").child(contact.id!!).setValue(contact)

            val intent = Intent(this, ContactsListActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Contact created successfully", Toast.LENGTH_LONG).show()
        }
    }

    fun editContacts() {

        var dbContacts = FirebaseDatabase.getInstance().reference

        val fName = intent.getStringExtra("FirstName")
        val lName = intent.getStringExtra("LastName")
        val pNum = intent.getStringExtra("Phone")
        val contactId = intent.getStringExtra("ContactId")

        Log.d("editContacts", "editContacts: $contactId")

        firstname.setText(fName)
        lastname.setText(lName)
        phone.setText(pNum)

        saveContact.setOnClickListener {

            var newFirstName = firstname.text.toString().trim()
            var newLastName = lastname.text.toString().trim()
            var newPhoneNumber = phone.text.toString().trim()

            dbContacts.child("CONTACT").child(contactId!!).setValue(
                ContactsModel(
                    id = contactId, firstName = newFirstName, lastName = newLastName, phoneNumber = newPhoneNumber
                )
            )

            val intent = Intent(this, ContactsListActivity::class.java)
            intent.putExtra("FirstName", newFirstName)
            intent.putExtra("LastName", newLastName)
            intent.putExtra("Phone", newPhoneNumber)
            startActivity(intent)
        }
    }
}
