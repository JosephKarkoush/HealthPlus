package com.example.healthplus

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils.replace
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL





class MainActivity : AppCompatActivity() {

    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = findViewById<BottomNavigationView>(R.id.bottomNav)
        navView.setupWithNavController(navController)

//here the controll of user existation happens
        retrieveUserData(getAndroidId(this)) { userData ->
            if (userData != null) {
                //If user is in the database do nothing
            } else {
                //if the user is not in the data base take him to the profil fragment
                navController.navigate(R.id.profileFragment)
            }
        }


        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        toolbar.setupWithNavController(navController, appBarConfiguration)









    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }








    private fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }





    private fun retrieveUserData(deviceImei: String, callback: (User?) -> Unit) {
        val userRef = myRef.child("users").child(deviceImei)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists
                    val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                    val gender = dataSnapshot.child("gender").getValue(String::class.java) ?: ""
                    val age = dataSnapshot.child("age").getValue(String::class.java) ?: ""
                    val height = dataSnapshot.child("height").getValue(String::class.java) ?: ""
                    val lastWeight = dataSnapshot.child("lastWeight").getValue(String::class.java) ?: ""

                    val weightEntries = mutableListOf<WeightEntry>()
                    val weightSnapshot = dataSnapshot.child("weight")

                    for (entrySnapshot in weightSnapshot.children) {
                        val value = entrySnapshot.child("value").getValue(String::class.java) ?: ""
                        val nowDate = entrySnapshot.child("date").getValue(String::class.java) ?: ""
                        val calories = entrySnapshot.child("calories").getValue(String::class.java) ?: ""

                        val weightEntry = WeightEntry(value, nowDate, calories)
                        weightEntries.add(weightEntry)
                    }

                    val userData = User(name, gender, age, height, lastWeight, weightEntries)
                    callback(userData)
                } else {
                    // User data doesn't exist
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }


}