package com.puce.apimentejuego.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class SubmitAnswersRequest(
    @JsonProperty("game_id")
    val gameId: Long,
    val answers: List<AnswerSubmission>
)

data class AnswerSubmission(
    @JsonProperty("question_id")
    val questionId: Long,
    @JsonProperty("selected_option_id")
    val selectedOptionId: Long? // Puede ser nulo si se acabó el tiempo
)

