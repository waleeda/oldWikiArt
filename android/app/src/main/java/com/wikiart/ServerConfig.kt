package com.wikiart

import java.net.URL

enum class EnvironmentType { PRODUCTION, STAGING }

interface ServerConfigType {
    val apiBaseUrl: URL
    val environment: EnvironmentType
}

data class ServerConfig(
    override val apiBaseUrl: URL,
    override val environment: EnvironmentType = EnvironmentType.PRODUCTION
) : ServerConfigType {
    companion object {
        private const val DEFAULT_API_ENDPOINT = "www.wikiart.org"

        val production: ServerConfigType =
            ServerConfig(
                URL("https://${Secrets.Api.Endpoint.PRODUCTION.ifBlank { DEFAULT_API_ENDPOINT }}"),
                EnvironmentType.PRODUCTION
            )

        val staging: ServerConfigType =
            ServerConfig(
                URL("https://${Secrets.Api.Endpoint.STAGING.ifBlank { DEFAULT_API_ENDPOINT }}"),
                EnvironmentType.STAGING
            )

        fun config(environment: EnvironmentType) = when(environment) {
            EnvironmentType.PRODUCTION -> production
            EnvironmentType.STAGING -> staging
        }
    }
}
