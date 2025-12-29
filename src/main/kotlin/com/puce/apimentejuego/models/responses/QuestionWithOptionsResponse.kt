package com.puce.apimentejuego.models.responses

data class QuestionWithOptionsResponse(
    val id_question: Long,
    val question: String,
    val category_id: Long,
    val options: List<OptionSimpleResponse>
)

data class OptionSimpleResponse(
    val question_option_id: Long, // ID de question_options
    val possible_answer: String
)

