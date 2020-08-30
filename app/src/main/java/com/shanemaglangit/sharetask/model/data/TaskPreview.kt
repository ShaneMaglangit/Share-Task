package com.shanemaglangit.sharetask.model.data

import android.graphics.Color
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class TaskPreview(
    @Exclude var id: String = "",
    var title: String = "",
    var subject: String = "",
    var isGroup: Boolean = false,
    var progress: Int = 0,
    var progressMax: Int = 0,
    var iconColor: Int = Color.parseColor("#BCFBE4"),
    var dateUpdated: Long = System.currentTimeMillis()
) : Parcelable {
    companion object {
        fun createFromTask(task: Task): TaskPreview {
            return TaskPreview(
                title = task.title,
                subject = task.subject,
                progress = task.progress,
                progressMax = task.progressMax,
                isGroup = task.isGroup,
                iconColor = task.iconColor
            )
        }
    }
}