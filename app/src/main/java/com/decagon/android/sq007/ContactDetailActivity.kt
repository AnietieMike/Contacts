package com.decagon.android.sq007

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.models.ContactsModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var fullname: TextView
    private lateinit var phone: TextView
    private lateinit var label: TextView
    private lateinit var editContact: FloatingActionButton
    private lateinit var mContacts: ContactsModel
    private lateinit var callIcon: ImageView
    private lateinit var messageIcon: ImageView
    private lateinit var profileIcon: TextView

    private val PERMISSIONS_REQUEST_MAKE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        fullname = findViewById(R.id.full_name)
        phone = findViewById(R.id.tv_phone_num)
        label = findViewById(R.id.tv_label)
        callIcon = findViewById(R.id.image_call)
        messageIcon = findViewById(R.id.image_message)
        profileIcon = findViewById(R.id.tv_icon_profile)

        mContacts = intent.getSerializableExtra("Contactdetail") as ContactsModel
        Log.d("ContactDetails", "onCreate: $mContacts")
        profileIcon.text = intent.getStringExtra("Icon")

        fullname.text = "${mContacts.firstName} ${mContacts.lastName}"
        phone.text = mContacts.phoneNumber
        label.text = getString(R.string.mobile)

        editContact()
        makeCall()
        sendMessage()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_share_contact -> {
                val shareContactIntent = Intent()
                shareContactIntent.action = Intent.ACTION_SEND
                shareContactIntent.type = "text/plain"
                shareContactIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Name: ${
                    mContacts.firstName +
                        " " + mContacts.lastName
                    }\n Phone: ${mContacts.phoneNumber}"
                )
                startActivity(Intent.createChooser(shareContactIntent, getString(R.string.send_to)))
                true
            }
            R.id.menu_delete_contact -> {
                var contactId = mContacts.id
                var mPostReference = FirebaseDatabase.getInstance().reference
                    .child("CONTACT").child(contactId!!)
                mPostReference.removeValue()
                var intent = Intent(this, ContactsListActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    applicationContext,
                    "Contact deleted successfully",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeCall() {
        callIcon.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), PERMISSIONS_REQUEST_MAKE_CALL)
            } else {
                Toast.makeText(applicationContext, "Call permission granted", Toast.LENGTH_SHORT).show()

                val phoneNumber = mContacts.phoneNumber
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel: $phoneNumber")
                startActivity(callIntent)
            }
        }
    }

    private fun sendMessage() {
        val phoneNumber = mContacts.phoneNumber
        messageIcon.setOnClickListener {
            val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:".plus(phoneNumber)))
            ContextCompat.startActivity(this, smsIntent, null)
        }
    }

    private fun editContact() {

        editContact = findViewById(R.id.fab_edit_contact)
        editContact.setOnClickListener {
            val intent = Intent(this, CreateContactActivity::class.java)
            intent.putExtra("FirstName", mContacts.firstName)
            intent.putExtra("LastName", mContacts.lastName)
            intent.putExtra("Phone", mContacts.phoneNumber)
            intent.putExtra("ContactId", mContacts.id)
            intent.putExtra("CHECKING", "message")
            startActivity(intent)
        }
    }

//    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            when {
//                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
//                    Toast.makeText(
//                        applicationContext,
//                        "$name permission granted",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                shouldShowRequestPermissionRationale(permission) -> showDialog(
//                    permission,
//                    name,
//                    requestCode
//                )
//
//                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
//            }
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_MAKE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall()
            } else {
                Toast.makeText(this, "Call permission refused", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun showDialog(permission: String, name: String, requestCode: Int) {
//        val builder = AlertDialog.Builder(this)
//
//        builder.apply {
//            setMessage("Permission to access your $name is required to use this feature")
//            setTitle("Permission required")
//            setPositiveButton("OK") { dialog, which ->
//                ActivityCompat.requestPermissions(
//                    this@ContactDetailActivity,
//                    arrayOf(permission),
//                    requestCode
//                )
//            }
//        }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
}
