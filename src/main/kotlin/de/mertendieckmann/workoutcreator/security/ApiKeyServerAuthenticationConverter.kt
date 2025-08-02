package de.mertendieckmann.workoutcreator.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class ApiKeyServerAuthenticationConverter (
    private val headerName: String = "X-API-KEY",
): ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val apiKey = exchange.request.headers.getFirst(headerName)
            ?: exchange.request.queryParams.getFirst("api_key")
        return if (apiKey.isNullOrBlank()) Mono.empty()
        else Mono.just(UsernamePasswordAuthenticationToken(apiKey, apiKey))
    }
}