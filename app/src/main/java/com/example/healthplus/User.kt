package com.example.healthplus

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (val mobilId : String? = null, val name : String? = null, val gender : String? = null, val age : Double? = 0.0, val weight : Double? = 0.0,val height : Double? = 0.0) {

}