package de.mertendieckmann.workoutcreator.security

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.*

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(SecurityProps::class)
class SecurityConfig(
    private val props: SecurityProps
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authManager = ApiKeyReactiveAuthenticationManager(props.apiKeys.toSet())
        val converter = ApiKeyServerAuthenticationConverter()

        val whitelist: ServerWebExchangeMatcher = OrServerWebExchangeMatcher(
            ServerWebExchangeMatchers.pathMatchers(HttpMethod.OPTIONS, "/**"),
            ServerWebExchangeMatchers.pathMatchers(
                "/swagger-ui/**", "/v3/api-docs/**",
                "/actuator/health/**", "/actuator/info"
            )
        )

        val protectedMatcher = NegatedServerWebExchangeMatcher(whitelist)

        val entryPoint: ServerAuthenticationEntryPoint = HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)

        val authFilter = AuthenticationWebFilter(authManager).apply {
            setServerAuthenticationConverter(converter)
            setRequiresAuthenticationMatcher(protectedMatcher)
            setAuthenticationFailureHandler(ServerAuthenticationEntryPointFailureHandler(entryPoint))
        }

        return http
            .csrf { it.disable() }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
            .authorizeExchange {
                it.matchers(whitelist).permitAll()
                    .anyExchange().authenticated()
            }
            .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }
}