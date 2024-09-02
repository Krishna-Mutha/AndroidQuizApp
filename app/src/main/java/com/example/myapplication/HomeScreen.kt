package com.example.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

@Composable
fun HomeScreen(navController: NavController,dbref:FirebaseDatabase,modifier: Modifier = Modifier,name:String) {
    val firestoredbref = FirebaseFirestore.getInstance()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(painter = painterResource(id = R.drawable.baseline_account_circle_24) ,
            contentDescription = "User Logo",
            Modifier.size(74.dp))
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Hello $name!",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(23.dp))
        Button(onClick = {saveQuizToFirestore(firestoredbref,generalKnowledgeQuiz)}
        , colors = ButtonColors(Color.Black,Color.White,Color.LightGray,Color(0xFFaab7b8))) {
            Text(text = "Start your quiz")
        }
        HorizontalDivider(modifier=Modifier.padding(top=10.dp))
        Text(text = "Category",
            modifier= Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            fontWeight = FontWeight.Bold
        )
        LazyRow {
            firestoredbref.collection("quizzes").get()
        }
    }
}