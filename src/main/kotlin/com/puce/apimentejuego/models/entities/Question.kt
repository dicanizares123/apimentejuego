package com.puce.apimentejuego.models.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table


@Entity
@Table(name = "questions")
data class Question(
    /**
     *
     * IMPORTANTE CAMBIAR EL TIPO EN EL CAMPO question DE STRING A TEXT
     * PARA PERMITIR PREGUNTAS MÁS LARGAS, ASI COMO EN explanation TAMBIÉN
     *
     * TIPO STRING: VARCHAR(255)
     * TIPO TEXT: PERMITE MÁS CARACTERES
     *
     * */
    // Relación Muchos a Uno: Muchas preguntas pertenecen a una categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,
    // CAMBIAR A TIPO TEXT
    @Column(name = "question", nullable = false)
    var question: String,
    // CAMBIAR A TIPO TEXT
    @Column(name = "explanation", nullable = false)
    var explanation: String,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean
): BaseEntity(){
    // Relación Uno a Muchos: Una pregunta puede tener muchas opciones a través de QuestionOptions
    // mappedBy = "question" debe coincidir con el nombre de la variable en QuestionOptions.kt
    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore // Evita bucles infinitos si devuelves la entidad directamente en el JSON
    var questionOptions: MutableList<QuestionOption> = mutableListOf()
}
