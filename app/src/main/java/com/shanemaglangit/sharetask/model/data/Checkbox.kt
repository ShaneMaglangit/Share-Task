package com.shanemaglangit.sharetask.model.data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Checkbox(
    @Exclude var id: String = "",
    var details: String = "",
    var dateCreated: Long = System.currentTimeMillis(),
    var checked: Boolean = false
)