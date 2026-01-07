package com.puce.apimentejuego.models.entities

import jakarta.persistence.*

@Entity
@Table(name = "game_answers")
data class GameAnswer(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    var game: Game,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    var question: Question,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_option_id", nullable = true) // Puede ser nulo si no respondió
    var selectedOption: QuestionOption? = null,

    @Column(name = "is_correct", nullable = false)
    var isCorrect: Boolean = false

) : BaseEntity()

