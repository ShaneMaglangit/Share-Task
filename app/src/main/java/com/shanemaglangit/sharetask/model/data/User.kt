package com.shanemaglangit.sharetask.model.data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    @Exclude var userId: String? = null,
    var username: String? = null,
    var email: String? = null
)