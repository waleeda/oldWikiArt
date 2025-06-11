package com.wikiart

object Secrets {
    const val IS_OSS = false
    const val FIELD_REPORT_EMAIL = "hello@email.com"

    object Api {
        object Client {
            const val PRODUCTION = "deadbeef"
            const val STAGING = "beefdead"
        }

        object Endpoint {
            const val PRODUCTION = "www.wikiart.org"
            const val STAGING = "www.wikiart.org"
        }
    }

    object BasicHTTPAuth {
        const val USERNAME = "usr"
        const val PASSWORD = "pswd"
    }

    object CanvasPopKeys {
        const val ACCESS_KEY = ""
        const val SECRET_KEY = ""
    }

    object KiteKeys {
        const val SECRET = ""
        const val PUBLIC_KEY = ""
    }
}
