package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.entities.Game
import com.puce.apimentejuego.models.entities.User
import com.puce.apimentejuego.models.responses.GameResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GameMapper {

    fun toEntity(user: User, category: Category): Game {
        return Game(
            user = user,
            category = category,
            score = 0,
            startedAt = LocalDateTime.now()
        )
    }

    fun toResponse(game: Game): GameResponse {
        return GameResponse(
            id = game.id,
            userId = game.user.id,
            categoryId = game.category.id,
            score = game.score,
            startedAt = game.startedAt,
            finishedAt = game.finishedAt
        )
    }
}

