package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.GameAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameAnswerRepository : JpaRepository<GameAnswer, Long> {
    fun findByGameId(gameId: Long): List<GameAnswer>
}

