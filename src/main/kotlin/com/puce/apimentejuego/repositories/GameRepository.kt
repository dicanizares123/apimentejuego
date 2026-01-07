package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface GameRepository : JpaRepository<Game, Long> {
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long): Optional<Game>
    fun findByUserIdAndCategoryIdIn(userId: Long, categoryIds: List<Long>): List<Game>
}
