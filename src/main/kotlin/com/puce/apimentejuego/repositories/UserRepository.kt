package com.puce.apimentejuego.repositories

import com.puce.apimentejuego.models.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: JpaRepository<User, Long> {

}