package com.shanemaglangit.sharetask.data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Task(
    @Exclude var id: String = "",
    var title: String = "",
    var subject: String = "",
    var description: String = "",
    var group: Boolean = false,
    var checkboxes: List<Checkbox> = mutableListOf(),
    var members: HashMap<String, Boolean> = HashMap()
)