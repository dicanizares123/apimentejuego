package com.puce.apimentejuego.models.entities

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Id
import java.time.LocalDateTime


/**
 * Clase que refencia los atributos comunes de las entidades en la base de datos
 */

@MappedSuperclass
abstract class BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()


}