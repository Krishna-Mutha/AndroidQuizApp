package com.example.myapplication

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.tasks.await
import java.io.File.separator
import kotlin.math.log

@Composable
fun QuizPage(navController: NavController,selectedQuiz:Int,selectedCategory:String,realtimedbref:FirebaseDatabase,firestoredbref:FirebaseFirestore,modifier: Modifier = Modifier) {
    val workSans= FontFamily(Font(R.font.work_sans))
    var outerBoxWidth = 0
    var lastSelectedOption by remember {
        mutableStateOf(mutableListOf<Int>())
    }
    var innerBoxWidth by remember {
        mutableStateOf(0)
    }
    var quiz by remember {
        mutableStateOf(Quiz("", emptyList(),""))
    }
    var questionCount by remember {
        mutableStateOf(0)
    }
    var selectedOption by remember {
        mutableStateOf(-1)
    }
    var optionError by remember {
        mutableStateOf("")
    }
    var score by remember {
        mutableStateOf(0)
    }
    var isFinished by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        try {
            val documentSnapshot = firestoredbref.collection("quizzes").document(selectedCategory).get().await()
            if (documentSnapshot.exists()) {
                val quizTitle = documentSnapshot.getString("quizTitle") ?: "No Title"
                val questionsData = documentSnapshot.get("questions") as? List<Map<String, Any>> ?: emptyList()

                if (selectedQuiz >= 0) {
                    val questions = questionsData.map { questionMap ->
                        Question(
                            correctAnswerIndex = (questionMap["correctAnswerIndex"] as? Long)?.toInt() ?: 0,
                            options = questionMap["options"] as? List<String> ?: emptyList(),
                            questionText = questionMap["questionText"] as? String ?: "No Question Text"
                        )
                    }
                    quiz=Quiz(categoryName = selectedCategory, questions = questions, quizTitle = quizTitle)
                }
            } else {
                Log.e("Firestore", "No such document for category: $selectedCategory")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching quiz", e)
        }
    }
    if(!isFinished){
    Column (modifier= Modifier
        .fillMaxWidth()
        .padding(top = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                modifier=Modifier.clickable {
                    if(questionCount>0){
                        questionCount-=1
                        val progressIncrement = outerBoxWidth / 11
                        innerBoxWidth -= progressIncrement
                        if(lastSelectedOption.last()==quiz.questions.getOrNull(questionCount)?.correctAnswerIndex){
                            score -= 1
                            Log.d("My tag","$score")
                            lastSelectedOption.removeLast()
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.width(50.dp))
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(20.dp))
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        outerBoxWidth = coordinates.size.width
                    }
                ) {}
                Box(
                    modifier = Modifier
                        .background(Color.Black, shape = RoundedCornerShape(20.dp))
                        .fillMaxHeight()
                        .width(innerBoxWidth.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .height(30.dp)
                .padding(top = 15.dp)
        )
        Text(
            text = "Question ${questionCount + 1}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 26.sp,
            fontFamily = workSans,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = quiz.questions.getOrNull(questionCount)?.questionText ?: "",
            fontFamily = workSans,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 25.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .clickable {
                    if (selectedOption == 0) {
                        selectedOption = -1
                    } else {
                        selectedOption = 0
                    }
                }
                .background(Color(0xFFEFEFF0), shape = RoundedCornerShape(40.dp))
                .border(
                    if (selectedOption == 0) 3.dp else 1.dp,
                    if (selectedOption == 0) Color(0xFF2ac5ee) else Color(0xFFAFB1B6),
                    shape = RoundedCornerShape(40.dp)
                )
                .height(130.dp)
                .width(130.dp)
            ) {
                Text(
                    text = quiz.questions.getOrNull(questionCount)?.options?.getOrNull(0) ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.width(40.dp))
            Box(modifier = Modifier
                .clickable {
                    if (selectedOption == 1) {
                        selectedOption = -1
                    } else {
                        selectedOption = 1
                    }
                }
                .background(Color(0xFFEFEFF0), shape = RoundedCornerShape(40.dp))
                .border(
                    if (selectedOption == 1) 3.dp else 1.dp,
                    if (selectedOption == 1) Color(0xFF2ac5ee) else Color(0xFFAFB1B6),
                    shape = RoundedCornerShape(40.dp)
                )
                .height(130.dp)
                .width(130.dp)
            ) {
                Text(
                    text = quiz.questions.getOrNull(questionCount)?.options?.getOrNull(1) ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .clickable {
                    if (selectedOption == 2) {
                        selectedOption = -1
                    } else {
                        selectedOption = 2
                    }
                }
                .background(Color(0xFFEFEFF0), shape = RoundedCornerShape(40.dp))
                .border(
                    if (selectedOption == 2) 3.dp else 1.dp,
                    if (selectedOption == 2) Color(0xFF2ac5ee) else Color(0xFFAFB1B6),
                    shape = RoundedCornerShape(40.dp)
                )
                .height(130.dp)
                .width(130.dp)
            ) {
                Text(
                    text = quiz.questions.getOrNull(questionCount)?.options?.getOrNull(2) ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.width(40.dp))
            Box(modifier = Modifier
                .clickable {
                    if (selectedOption == 3) {
                        selectedOption = -1
                    } else {
                        selectedOption = 3
                    }
                }
                .background(Color(0xFFEFEFF0), shape = RoundedCornerShape(40.dp))
                .border(
                    if (selectedOption == 3) 3.dp else 1.dp,
                    if (selectedOption == 3) Color(0xFF2ac5ee) else Color(0xFFAFB1B6),
                    shape = RoundedCornerShape(40.dp)
                )
                .height(130.dp)
                .width(130.dp)
            ) {
                Text(
                    text = quiz.questions.getOrNull(questionCount)?.options?.getOrNull(3) ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(90.dp))
        Text(
            text = optionError,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                if (selectedOption != -1) {
                    if (quiz.questions.getOrNull(questionCount)?.correctAnswerIndex == selectedOption) {
                        score += 1
                    }
                    lastSelectedOption+=selectedOption
                    optionError = ""
                    if (questionCount == 4) {
                        val progressionIncrement = outerBoxWidth / 11
                        innerBoxWidth += progressionIncrement
                        isFinished=true
                    } else {
                        questionCount += 1
                        val progressIncrement = outerBoxWidth / 11
                        innerBoxWidth += progressIncrement
                        selectedOption = -1
                    }
                } else {
                    optionError = "Please select an option"
                }
            },
            colors = ButtonDefaults.buttonColors(
                Color.Black,
                Color.White,
                Color.LightGray,
                Color(0xFFaab7b8)
            ),
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (questionCount == 4) "Submit" else "Next")
        }
    }
    }
    else{
        UserData.score[quiz.quizTitle]=score
        realtimedbref.getReference("Users").child(UserData.name).child(quiz.quizTitle).child("Score").setValue(score)
        Column(modifier=Modifier.fillMaxSize()) {
            Text(text = "Score",
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 150.dp),
                fontFamily = righteousFont,
                fontSize = 45.sp,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(50.dp))
            Box (modifier= Modifier
                .background(Color(0xFFEFEFF0), shape = RoundedCornerShape(20))
                .border(3.dp, Color(0xFFAFB1B6), shape = RoundedCornerShape(20))
                .width(300.dp)
                .height(200.dp)
                .align(Alignment.CenterHorizontally)
            ){
                Text(text = "${((score.toDouble()/5.0)*100).toInt()}%",
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontFamily = righteousFont
                    )
                Text(text="$score/5",
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(top = 120.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    fontFamily = righteousFont)
            }
            Spacer(modifier = Modifier.height(200.dp))
            Button(onClick = {
                navController.navigate(Screens.home)
            },
                colors = ButtonDefaults.buttonColors(
                    Color.Black,
                    Color.White,
                    Color.LightGray,
                    Color(0xFFaab7b8)
                ),
                modifier = Modifier
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Back To Home")
            }
        }
    }
}
