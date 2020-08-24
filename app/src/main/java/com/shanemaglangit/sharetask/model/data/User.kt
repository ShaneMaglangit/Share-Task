package com.shanemaglangit.sharetask.model.data

data class User(
    var username: String? = null,
    var taskIdList: MutableList<String> = mutableListOf()
)