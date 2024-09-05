package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.math.log

@Composable
fun HomeScreen(navController: NavController, dbref: FirebaseDatabase,firestoredbref:FirebaseFirestore, modifier: Modifier = Modifier, name: String) {
    var categoriesList by remember {
        mutableStateOf(listOf<String>())
    }
    var selectedQuiz by remember {
        mutableStateOf(-1)
    }
    var selectedCategory by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        firestoredbref.collection("quizzes").get()
            .addOnSuccessListener { result ->
                val categories = result.map { it.id }
                categoriesList = categories
            }
    }
    var selectionError by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp), // Add padding at the bottom to avoid overlap
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = "User Logo",
                modifier = Modifier.size(74.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Hello $name!",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(23.dp))
            Button(
                onClick = {
                    if(selectedQuiz==-1){
                        selectionError="Please select a quiz"
                    }
                    else{
                        navController.navigate(Screens.quiz+"/$selectedQuiz/$selectedCategory")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Start your quiz")
            }
            Text(text=selectionError,
                fontWeight = FontWeight.Bold,
                color=Color.Red
            )
            HorizontalDivider()
            Text(
                text = "Category",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                fontWeight = FontWeight.Bold
            )
            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
            ) {
                items(categoriesList) { category ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_hide_image_24),
                            contentDescription = "Category Logo",
                            modifier = Modifier
                                .border(1.dp, Color.Gray, CircleShape)
                                .size(50.dp)
                                .padding(10.dp)
                        )
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(80.dp)
                                .height(70.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                }
            }
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 15.dp)
            ) {
                items(categoriesList) { category ->
                    Text(
                        text = category,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(3) { count ->
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        if (selectedQuiz == count && selectedCategory == category) {
                                            selectedQuiz = -1
                                            selectedCategory = ""
                                        } else {
                                            selectedQuiz = count
                                            selectedCategory = category
                                            selectionError = ""
                                        }
                                    }
                                    .width(250.dp)
                                    .height(200.dp)
                                    .border(
                                        if (selectedQuiz == count && selectedCategory == category) 3.dp else 1.dp,
                                        if (selectedQuiz == count && selectedCategory == category) Color(
                                            0xFF2ac5ee
                                        ) else Color.Gray,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(vertical = 20.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .height(130.dp)
                                            .width(200.dp)
                                            .background(
                                                Color.LightGray,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_hide_image_24),
                                            contentDescription = "Quiz Logo",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                    Text(text = "Quiz ${count + 1}")
                                }
                            }
                            Spacer(Modifier.width(30.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Column(modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { }) {
                    Image(painter = painterResource(id = R.drawable.baseline_home_24), contentDescription = "Home",
                        modifier=Modifier.align(Alignment.CenterHorizontally))
                    Text(text = "Home", color = Color.Black)
                }
                Column(modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { }) {
                    Image(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription = "Profile",
                        modifier=Modifier.align(Alignment.CenterHorizontally))
                    Text(text = "Profile", color = Color.Black)
                }
                Column(modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { }) {
                    Image(painter = painterResource(id = R.drawable.baseline_settings_24), contentDescription = "Settings",
                        modifier=Modifier.align(Alignment.CenterHorizontally))
                    Text(text = "Settings", color = Color.Black)
                }

            }
        }
    }
}
