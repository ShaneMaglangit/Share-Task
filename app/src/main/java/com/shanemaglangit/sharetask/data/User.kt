package com.shanemaglangit.sharetask.data

data class User(
    var username: String = "",
    var tasks: MutableList<String> = mutableListOf()
)