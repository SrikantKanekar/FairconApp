package com.example.faircon.business.domain.model

data class Faircon(
    val parameter: Parameter = Parameter(),
    val controller: Controller = Controller(),
    val mode: Mode = Mode.IDLE,
    val status: Status = Status.STABLE
)