package com.puce.apimentejuego.models.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.OneToMany


@Entity
@Table(name = "categories")
data class Category (

    @Column(name = "slug", nullable = false )
    var slug: String,

    @Column(name = "questions_per_game", nullable = false)
    var questionsPerGame: Int,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "short_description", nullable = false)
    var shortDescription: String,

    @Column(name = "difficulty", nullable = false)
    var difficulty: String,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "duration_in_minutes", nullable = false)
    var duration_in_minutes: Int

): BaseEntity(){

    // Relación Uno a Muchos: Una categoría tiene muchas preguntas
    // mappedBy = "category" debe coincidir con el nombre de la variable en Question.kt
    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore // Evita bucles infinitos si devuelves la entidad directamente en el JSON
    var questions: MutableList<Question> = mutableListOf()
}