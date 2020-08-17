package com.shanemaglangit.sharetask.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Task(
    @Exclude var id: String = "",
    var title: String = "",
    var subject: String = "",
    var description: String = "",
    var group: Boolean = false,
    var progress: Int = 0,
    var checkboxes: List<String> = mutableListOf(),
    var members: HashMap<String, String> = HashMap(),
    var dateUpdated: Timestamp = Timestamp.now()
)