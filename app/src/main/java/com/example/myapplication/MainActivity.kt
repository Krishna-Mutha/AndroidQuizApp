package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val realtimedbref = FirebaseDatabase.getInstance("https://test-app-ff64f-default-rtdb.asia-southeast1.firebasedatabase.app")
        setContent {
            val navController=rememberNavController()
            val initialScreen = if(UserData.name.isNotEmpty()) Screens.home else Screens.startup
            MyApplicationTheme {
                NavHost(navController = navController, startDestination = initialScreen) {
                    composable(route = Screens.startup){
                        StartupScreen(navController,realtimedbref)
                    }
                    composable(route=Screens.home){
                        HomeScreen(navController,realtimedbref,name=UserData.name)
                    }
                }
            }
        }
    }
}
