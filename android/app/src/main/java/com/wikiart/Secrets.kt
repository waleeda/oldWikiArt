package com.wikiart

object Secrets {
    val IS_OSS = BuildConfig.IS_OSS
    val FIELD_REPORT_EMAIL = BuildConfig.FIELD_REPORT_EMAIL

    object Api {
        object Client {
            val PRODUCTION = BuildConfig.API_CLIENT_PRODUCTION
            val STAGING = BuildConfig.API_CLIENT_STAGING
        }

        object Endpoint {
            val PRODUCTION = BuildConfig.API_ENDPOINT_PRODUCTION
            val STAGING = BuildConfig.API_ENDPOINT_STAGING
        }
    }

    object BasicHTTPAuth {
        val USERNAME = BuildConfig.BASIC_HTTP_AUTH_USERNAME
        val PASSWORD = BuildConfig.BASIC_HTTP_AUTH_PASSWORD
    }

    object CanvasPopKeys {
        val ACCESS_KEY = BuildConfig.CANVAS_POP_ACCESS_KEY
        val SECRET_KEY = BuildConfig.CANVAS_POP_SECRET_KEY
    }

    object KiteKeys {
        val SECRET = BuildConfig.KITE_SECRET
        val PUBLIC_KEY = BuildConfig.KITE_PUBLIC_KEY
    }
}
