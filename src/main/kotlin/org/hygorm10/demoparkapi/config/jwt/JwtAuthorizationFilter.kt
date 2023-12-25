package org.hygorm10.demoparkapi.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthorizationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var detailsService: JwtUserDetailService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(JwtUtils.JWT_AUTHORIZATION)
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            println("JWT Token está nulo, vazio ou não inicializado com Bearer")
            filterChain.doFilter(request, response)
            return
        }

        if (!JwtUtils.isTokenValid(token)) {
            println("JWT Token inválido ou expirado")
            filterChain.doFilter(request, response)
            return
        }

        val username = JwtUtils.getUsernameFromToken(token)

        toAuthentication(request, username!!)

        filterChain.doFilter(request, response)
    }

    private fun toAuthentication(request: HttpServletRequest, username: String) {
        val userDetails = detailsService.loadUserByUsername(username)

        val authenticationToken = UsernamePasswordAuthenticationToken
            .authenticated(userDetails, null, userDetails.authorities)

        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authenticationToken
    }
}