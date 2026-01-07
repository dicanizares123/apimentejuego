package com.puce.apimentejuego.models.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "games")
data class Game(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(name = "score", nullable = false)
    var score: Int = 0,

    @Column(name = "started_at", nullable = false)
    var startedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "finished_at")
    var finishedAt: LocalDateTime? = null

) : BaseEntity()
