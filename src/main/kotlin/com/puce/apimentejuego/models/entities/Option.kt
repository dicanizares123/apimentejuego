package com.puce.apimentejuego.models.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table


@Entity
@Table(name = "options")
data class Option (

    @Column(name = "possible_answer", nullable = false)
    var possibleAnswer: String,

): BaseEntity(){
    // Relación Uno a Muchos: Una opción puede estar en muchas preguntas
    // mappedBy = "option" debe coincidir con el nombre de la variable en QuestionOptions.kt
    @OneToMany(mappedBy = "option", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonIgnore // Evita bucles infinitos si devuelves la entidad directamente en el JSON
    var questionOptions: MutableList<QuestionOption> = mutableListOf()
}