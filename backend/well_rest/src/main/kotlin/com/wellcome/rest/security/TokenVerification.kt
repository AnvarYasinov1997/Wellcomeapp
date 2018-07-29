package com.wellcome.rest.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.pipeline.PipelinePhase
import io.ktor.util.AttributeKey

class TokenVerification {

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Nothing, TokenVerification> {
        private val tokenVerificationPhase = PipelinePhase("TokenVerification")
        override val key: AttributeKey<TokenVerification>
            get() = AttributeKey("tokenVerification")

        val firebaseTokenKey: AttributeKey<FirebaseToken> = AttributeKey("firebaseToken")

        override fun install(pipeline: ApplicationCallPipeline, configure: Nothing.() -> Unit): TokenVerification {
            pipeline.insertPhaseAfter(ApplicationCallPipeline.Infrastructure,
                tokenVerificationPhase)
            pipeline.intercept(tokenVerificationPhase) {
                context.application.log.info("verification token")
                val headers = call.request.headers
                if (headers.isEmpty() || headers["token"] == null) {
                    call.response.status(HttpStatusCode.Unauthorized)
                    finish()
                } else {
                    val token = headers["token"]!!
                    context.application.log.info("token $token")
                    try {
                        val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token, true)
                        context.application.log.info("firebase token $firebaseToken")
                        context.attributes.put(firebaseTokenKey, firebaseToken)

                    } catch (e: Exception) {
                        if (e is FirebaseAuthException && e.errorCode == "id-token-revoked") {
                            call.application.log.info("token reviked ${e.message}")
                            call.response.status(HttpStatusCode.Unauthorized)
                            finish()
                        } else {
                            call.application.log.info("token invalid ${e.message}")
                            call.response.status(HttpStatusCode.Unauthorized)
                            finish()
                        }
                    }
                }
            }
            return TokenVerification()
        }

    }
}