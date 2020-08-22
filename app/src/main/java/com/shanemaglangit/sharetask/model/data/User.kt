package com.shanemaglangit.sharetask.model.data

data class User(
    var username: String = "",
    var tasks: MutableList<String> = mutableListOf()
)