package com.example.faircon.model

data class Faircon(
    val parameter: Parameter = Parameter(),
    val controller: Controller = Controller(),
    val mode: Mode = Mode.OFF,
    val status: Status = Status.STABLE
)