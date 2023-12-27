package org.hygorm10.demoparkapi.config.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets.UTF_8
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class JwtUtils {

    companion object {
        const val JWT_BEARER = "Bearer "
        const val JWT_AUTHORIZATION = "Authorization"
        private const val SECRET_KEY = "0123456789-0123456789-0123456789"
        private const val EXPIRE_DAYS = 0L
        private const val EXPIRE_HOURS = 0L
        private const val EXPIRE_MINUTES = 30L

        private fun generateKey(): Key {
            return Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(UTF_8))
        }

        private fun toExpireDate(start: Date): Date {
            val dateTime: LocalDateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val end: LocalDateTime = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES)
            return Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        }

        private fun getClaimsFromToken(token: String): Claims? {
            try {
                return Jwts.parser().setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).body
            } catch (e: JwtException) {
                println("Token inválido: ${e.message}")
            }

            return null
        }

        private fun refactorToken(token: String): String {
            if (token.contains(JWT_BEARER)) {
                return token.substring(JWT_BEARER.length)
            }
            return token
        }

        fun getUsernameFromToken(token: String): String? {
            return getClaimsFromToken(token)?.subject
        }

        fun isTokenValid(token: String): Boolean {
            try {
                Jwts.parser().setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token))
                return true
            } catch (e: JwtException) {
                println("Token inválido JWT: ${e.message}")
            }

            return false
        }

        fun createToken(username: String, role: String): JwtToken {
            val issuedAt = Date()
            val limit: Date = toExpireDate(issuedAt)
            val token: String = Jwts.builder()
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(limit)
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .setHeaderParam("typ", "JWT")
                .claim("role", role)
                .compact()

            return JwtToken(token)
        }
    }

}