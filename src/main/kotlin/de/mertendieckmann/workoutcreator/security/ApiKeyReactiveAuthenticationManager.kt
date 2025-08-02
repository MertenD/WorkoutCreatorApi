package de.mertendieckmann.workoutcreator.security

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import reactor.core.publisher.Mono

class ApiKeyReactiveAuthenticationManager(
    private val validKeys: Set<String>
): ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val key = (authentication.credentials ?: authentication.principal)?.toString()
        return if (key != null && validKeys.contains(key)) {
            val auth = UsernamePasswordAuthenticationToken(
                "api-client", null, listOf(SimpleGrantedAuthority("ROLE_API_CLIENT"))
            )
            Mono.just(auth)
        } else {
            Mono.error(BadCredentialsException("Invalid API Key"))
        }
    }
}