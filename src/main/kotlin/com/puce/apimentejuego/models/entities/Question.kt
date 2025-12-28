package com.puce.apimentejuego.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table


@Entity
@Table(name = "questions")
data class Question(

    // Relación Muchos a Uno: Muchas preguntas pertenecen a una categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(name = "question", nullable = false)
    var question: String,

    @Column(name = "explanation", nullable = false)
    var explanation: String,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean
): BaseEntity()
