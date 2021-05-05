package com.decagon.android.sq007

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.models.ContactsModel
import com.decagon.android.sq007.models.NODE_CONTACTS
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class ContactsViewModel : ViewModel() {

    private val dbContacts = FirebaseDatabase.getInstance().getReference(NODE_CONTACTS)

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result

    fun addContacts(contact: ContactsModel) {
    }
}
