package com.puce.apimentejuego.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "username", nullable = false, unique = true) // Agregado unique
    var username: String,

    @Column(name = "email", nullable = false, unique = true) // x2
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var password: String,


): BaseEntity()