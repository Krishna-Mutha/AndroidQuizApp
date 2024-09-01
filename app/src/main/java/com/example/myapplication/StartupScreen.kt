package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

val righteousFont= FontFamily(Font(R.font.righteous_regular))
@Composable
fun StartupScreen(navController: NavController,dbref:FirebaseDatabase,modifier: Modifier = Modifier) {
    val logo= painterResource(id = R.drawable.logo)
    var name by remember {
        mutableStateOf("")
    }
    var isEnabled by remember {
        mutableStateOf(true)
    }
    var showNameError by remember {
        mutableStateOf(false)
    }
    var nameErrorText by remember{
        mutableStateOf("Name must be less than 24 characters")
    }
    Column(
        modifier=Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Image(painter = logo, contentDescription = "logo")
        Spacer(modifier=Modifier.height(30.dp))
        Text(
            text="Welcome",
            fontFamily = righteousFont,
            fontSize = 45.sp
        )
        Spacer(modifier=Modifier.height(70.dp))
        Text(
            text = "Name",
            color = Color.White,
            modifier = Modifier
                .background(Color(0xFFAFB1B6), shape = RoundedCornerShape(100.dp))
                .padding(10.dp)
                .width(100.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier=Modifier.height(25.dp))
        TextField(value = name ,
            onValueChange = {tempName->
                name=tempName
                showNameError = tempName.length >= 24
                nameErrorText = "Name must be less than 24 characters"
            },
            shape= RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.LightGray,
                disabledTextColor = Color(0xFFaab7b8)
            ),
            enabled = isEnabled,
            placeholder = {Text(text="Enter Name"
            , style = TextStyle(color=Color.Gray,
                fontSize = 16.sp
                )
            )},
            modifier = Modifier.width(280.dp)
                .height(50.dp)
        )
        if(showNameError){
        Text(text = nameErrorText,
            color=Color.Red,
            fontSize = 13.sp,

        )}
        Spacer(modifier = Modifier.height(100.dp))
        Button(onClick = {
                         if(name.trim().isNotEmpty()){
                             isEnabled=false
                             showNameError=false
                             userData.name=name
                             dbref.getReference("Users").child("name").setValue(userData.name)
                                 .addOnCompleteListener { task->
                                     if(task.isSuccessful){
                                         navController.navigate(Screens.home)
                                     }
                                     else{
                                         isEnabled=true
                                         nameErrorText="An error occurred"
                                         showNameError=true
                                     }
                                 }
                         }
                         else{
                             nameErrorText="Please enter a name"
                             showNameError=true
                         }},
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(Color.Black,Color.White,Color.LightGray,Color(0xFFaab7b8)),
            modifier = Modifier.width(170.dp)
        ) {
            Text(
                text="Submit"
            )
        }
    }
}