package de.mertendieckmann.workoutcreator.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security")
data class SecurityProps(val apiKeys: List<String> = emptyList())