package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.requests.CategoryRequest
import com.puce.apimentejuego.models.responses.CategoryResponse
import org.springframework.stereotype.Component


@Component
class CategoryMapper {

    fun toEntity(request: CategoryRequest): Category{
        return Category(
            slug = request.slug,
            questionsPerGame = request.questionsPerGame,
            description = request.description,
            shortDescription = request.shortDescription,
            difficulty = request.difficulty,
            title = request.title,
            duration_in_minutes = request.duration_in_minutes
        )
    }

    fun toResponse(category: Category): CategoryResponse {
        return CategoryResponse(
            id = category.id,
            slug = category.slug,
            questionsPerGame = category.questionsPerGame,
            description = category.description,
            shortDescription = category.shortDescription,
            difficulty = category.difficulty,
            title = category.title,
            duration_in_minutes = category.duration_in_minutes

        )
    }


}