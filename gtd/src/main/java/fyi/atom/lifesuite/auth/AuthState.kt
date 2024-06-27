package fyi.atom.lifesuite.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthState(
    val accessToken: String,
    val idToken: String,
    val refreshToken: String
)