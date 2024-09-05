package com.example.myapplication

data class Quiz(
    val categoryName: String = "",
    val questions: List<Question> = emptyList(),
    val quizTitle: String = ""
)

data class Question(
    val correctAnswerIndex: Int = 0,
    val options: List<String?> = emptyList(),
    val questionText: String? = ""
)
