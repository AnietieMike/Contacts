package com.decagon.android.sq007.models

import com.google.firebase.database.Exclude
import java.io.Serializable

data class ContactsModel(
    @Exclude
    var id: String? = null,
    var firstName: String? = "",
    var lastName: String? = "",
    var phoneNumber: String? = ""
) : Serializable
