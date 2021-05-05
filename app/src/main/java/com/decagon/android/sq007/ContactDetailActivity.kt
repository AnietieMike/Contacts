package com.decagon.android.sq007

import android.R.id
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.decagon.android.sq007.models.ContactsModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class ContactDetailActivity : AppCompatActivity() {

    lateinit var fullname: TextView
    lateinit var phone: TextView
    lateinit var label: TextView
    lateinit var editContact: FloatingActionButton
    lateinit var mContacts: ContactsModel
    lateinit var callIcon: ImageView
    lateinit var messageIcon: ImageView

    val MAKE_CALL_RQ = 1
    val SEND_SMS_RQ = 102

    private val SENT = "SMS_SENT"
    private val DELIVERED = "SMS_DELIVERED"
    private val MAX_SMS_MESSAGE_LENGTH = 160

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        fullname = findViewById(R.id.full_name)
        phone = findViewById(R.id.tv_phone_num)
        label = findViewById(R.id.tv_label)
        callIcon = findViewById(R.id.image_call)
        messageIcon = findViewById(R.id.image_message)

        mContacts = intent.getSerializableExtra("Contactdetail") as ContactsModel
        Log.d("ContactDetails", "onCreate: $mContacts")

        fullname.text = "${mContacts.firstName} ${mContacts.lastName}"
        phone.text = mContacts.phoneNumber
        label.text = "Mobile"

        onClickEditButton()
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
            checkForPermissions(
                android.Manifest.permission.CALL_PHONE,
                "Call application",
                MAKE_CALL_RQ
            )
            val phoneNumber = mContacts.phoneNumber
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel: $phoneNumber")
            startActivity(callIntent)
        }
    }

    private fun sendMessage() {
        messageIcon.setOnClickListener {
            checkForPermissions(
                android.Manifest.permission.SEND_SMS,
                "SMS application",
                SEND_SMS_RQ
            )
            composeSmsMessage(phone.text.toString(), "Hello World!")
        }
    }

    fun composeSmsMessage(phoneNumber: String, message: String) {
        val piSent = PendingIntent.getBroadcast(
            applicationContext,
            0, Intent(SENT), 0
        )
        val piDelivered = PendingIntent.getBroadcast(
            applicationContext,
            0, Intent(DELIVERED), 0
        )
        val smsManager = SmsManager.getDefault()
        val length: Int = message.length

        if (length > MAX_SMS_MESSAGE_LENGTH) {
            val messagelist = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(
                phoneNumber, null,
                messagelist, null, null
            )
        } else {
            smsManager.sendTextMessage(
                phoneNumber, null, message,
                piSent, piDelivered
            )
        }
    }

    private fun onClickEditButton() {

        editContact = findViewById(R.id.fab_edit_contact)
        editContact.setOnClickListener {
            val intent = Intent(this, CreateContactActivity::class.java)
            intent.putExtra("FirstName", mContacts.firstName)
            intent.putExtra("LastName", mContacts.lastName)
            intent.putExtra("Phone", mContacts.phoneNumber)
            intent.putExtra("ContactId", mContacts.id)

            Log.d("editContact3", "onClickEditFab: ${mContacts.id}")
            intent.putExtra("TESTING", "message")
            startActivity(intent)
        }
    }

    fun checkForPermissions(permission: String, name: String, requestCode: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        applicationContext,
                        "$name permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
            }
        }
        when (requestCode) {
            MAKE_CALL_RQ -> innerCheck("Call Application")
            SEND_SMS_RQ -> innerCheck("Messaging application")
        }
    }

    fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("Permission to access your $name is required to use this feature")
            setTitle("Permission required")
            setPositiveButton("Accept") { dialog, which ->
                ActivityCompat.requestPermissions(
                    this@ContactDetailActivity,
                    arrayOf(permission),
                    requestCode
                )
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}
