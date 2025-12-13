package com.puce.apimentejuego.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "users")
data class User(

    @Column(name = "first_name")
    var firstName: String,

    @Column(name = "last_name")
    var lastName: String,

    @Column(name = "username")
    var username: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "password_hash")
    var password: String,

): BaseEntity()