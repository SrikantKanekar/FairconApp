package com.example.faircon.business.interactors.auth

class AuthInteractors(
    val attemptLogin: AttemptLogin,
    val attemptRegistration: AttemptRegistration,
    val checkPreviousUser: CheckPreviousUser
)