package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface CategoyRepositoy: JpaRepository<Category, Long> {

}