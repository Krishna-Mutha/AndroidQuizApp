package com.example.myapplication

class Quiz(
    val quizTitle: String = "",
    val questions: List<Question> = listOf(),
    val category: String = ""
)

class Question(
    val questionText: String = "",
    val options: List<String> = listOf(),
    val correctAnswerIndex: Int = -1
)
