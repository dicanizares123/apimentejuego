package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository: JpaRepository<Question, Long> {

    fun findByCategoryId(categoryId: Long): List<Question>

}