package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.QuestionOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionOptionRepository : JpaRepository<QuestionOption, Long> {
}

