package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.Option
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface OptionRepository: JpaRepository<Option, Long> {
}