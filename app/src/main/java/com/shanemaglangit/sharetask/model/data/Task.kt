package com.shanemaglangit.sharetask.model.data

import android.graphics.Color
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Task(
    @Exclude var id: String = "",
    var title: String = "",
    var subject: String = "",
    var description: String = "",
    var isGroup: Boolean = false,
    var progress: Int = 0,
    var progressMax: Int = 0,
    var iconColor: Int = Color.parseColor("#BCFBE4"),
    var members: HashMap<String, String> = HashMap(),
    var files: HashMap<String, String> = HashMap(),
    var dateUpdated: Long = System.currentTimeMillis()
) : Parcelable