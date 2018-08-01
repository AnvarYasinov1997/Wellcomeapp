package com.wellcome.rest.security

import com.google.api.client.json.webtoken.JsonWebSignature
import com.google.firebase.auth.FirebaseToken
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.log
import io.ktor.util.AttributeKey

class TestTokenVerification {

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Nothing, TestTokenVerification> {
        override val key: AttributeKey<TestTokenVerification> = AttributeKey("TestTokenVerification")

        override fun install(pipeline: ApplicationCallPipeline, configure: Nothing.() -> Unit): TestTokenVerification {
            pipeline.insertPhaseAfter(ApplicationCallPipeline.Infrastructure,
                TokenVerification.tokenVerificationPhase)
            pipeline.intercept(TokenVerification.tokenVerificationPhase) {
                context.application.log.info("test verification token")
                val token = createFakeToken()
                context.attributes.put(TokenVerification.firebaseTokenKey, token)
                context.application.log.info(token.uid)
            }
            return TestTokenVerification()
        }

        private fun createFakeToken(): FirebaseToken {
            val payloadC = Class.forName("com.google.firebase.auth.FirebaseToken\$FirebaseTokenImpl\$Payload")
            val payloadConstructor = payloadC.getDeclaredConstructor()
            payloadConstructor.isAccessible = true
            val payload = payloadConstructor.newInstance()
            val fieldSubject = payloadC.superclass.superclass.getDeclaredField("subject")
            fieldSubject.isAccessible = true
            fieldSubject.set(payload, "FAKEUID!!!")

            val header = JsonWebSignature.Header()
            val signatureByte = "fake".toByteArray()
            val signedContentByte = "fake".toByteArray()


            val tokenImplC = Class.forName("com.google.firebase.auth.FirebaseToken\$FirebaseTokenImpl")
            val tokenImplConstructor = tokenImplC.declaredConstructors[0]
            tokenImplConstructor.isAccessible = true
            val tokenImpl = tokenImplConstructor.newInstance(header, payload, signatureByte, signedContentByte)

            val firebaseTokenC = Class.forName("com.google.firebase.auth.FirebaseToken")
            val firebaseTokenConstructor = firebaseTokenC.declaredConstructors[0]
            firebaseTokenConstructor.isAccessible = true
            return firebaseTokenConstructor.newInstance(tokenImpl) as FirebaseToken
        }
    }
}