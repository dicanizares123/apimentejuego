package com.puce.apimentejuego.models.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class QuestionWithOptionsResponse(
    @JsonProperty("question_id")
    val questionId: Long,
    val question: String,
    @JsonProperty("category_id")
    val categoryId: Long,
    val options: List<OptionSimpleResponse>
)

data class OptionSimpleResponse(
    @JsonProperty("question_option_id")
    val questionOptionId: Long, // ID de question_options
    @JsonProperty("possible_answer")
    val possibleAnswer: String
)

