package com.puce.apimentejuego.models.entities

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Column


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

    @Column(name = "duration", nullable = false)
    var duration: Int

): BaseEntity()