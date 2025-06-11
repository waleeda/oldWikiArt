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
        val production: ServerConfigType = ServerConfig(URL("https://${Secrets.Api.Endpoint.PRODUCTION}"), EnvironmentType.PRODUCTION)
        val staging: ServerConfigType = ServerConfig(URL("https://${Secrets.Api.Endpoint.STAGING}"), EnvironmentType.STAGING)

        fun config(environment: EnvironmentType) = when(environment) {
            EnvironmentType.PRODUCTION -> production
            EnvironmentType.STAGING -> staging
        }
    }
}
