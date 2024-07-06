package fyi.atom.lifesuite.auth

import kotlinx.serialization.Serializable

/**
 * https://clickup.com/api/developer-portal/faq/#how-are-dates-formatted-in-clickup
 * "OAuth access tokens do not expire at this time."
 */
@Serializable
data class AuthState(
    val accessToken: String
)