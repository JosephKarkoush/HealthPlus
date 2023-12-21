package com.example.healthplus

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (val mobilId : String? = null, val name : String? = null, val gender : String? = null, val age : String? = null, val weight : String? = null,val height : String? = null) {

}