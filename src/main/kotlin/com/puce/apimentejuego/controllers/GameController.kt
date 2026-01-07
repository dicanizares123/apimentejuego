package com.puce.apimentejuego.controllers

import com.puce.apimentejuego.models.requests.GameRequest
import com.puce.apimentejuego.models.requests.SubmitAnswersRequest
import com.puce.apimentejuego.models.responses.GameResponse
import com.puce.apimentejuego.models.responses.GameResultResponse
import com.puce.apimentejuego.services.GameService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/games")
class GameController(
    private val gameService: GameService
) {

    @PostMapping("/start")
    fun startOrGetGame(@RequestBody request: GameRequest): GameResponse {
        return gameService.startOrGetGame(request)
    }


    @PostMapping("/submit")
    fun submitAnswers(@RequestBody request: SubmitAnswersRequest): GameResultResponse {
        return gameService.submitAnswers(request)
    }

    @GetMapping("/scores")
    fun getScores(
        @RequestParam userId: Long,
        @RequestParam categoryIds: List<Long>
    ): List<GameResponse> {
        return gameService.getScoresByCategories(userId, categoryIds)
    }
}
