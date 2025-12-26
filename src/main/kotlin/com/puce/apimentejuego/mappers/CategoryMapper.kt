package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.Category
import com.puce.apimentejuego.models.requests.CategoryRequest
import com.puce.apimentejuego.models.responses.CategoryResponse
import org.springframework.stereotype.Component


@Component
class CategoryMapper {

    fun toEntity(category: CategoryRequest): Category{
        return Category(
            slug = category.slug,
            questionsPerGame = category.questionsPerGame,
            description = category.description,
            shortDescription = category.shortDescription,
            difficulty = category.difficulty,
            title = category.title,
            duration = category.duration
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
            duration = category.duration
        )
    }


}