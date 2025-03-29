package com.steerify.Helpers.config;


import com.steerify.Entities.JwtUser;
import com.steerify.Helpers.JwtService.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.SignatureException;


@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);

                if (jwtService.isTokenValidStructure(token)) {
                    String email = jwtService.extractUsername(token);

                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                        if (userDetails instanceof JwtUser jwtUser) {
                            if (jwtService.validateToken(token, jwtUser)) {
                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());
                                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            }
                        }
                    }
                }
            }
        } catch (ExpiredJwtException ex) {
            handleJwtException(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (MalformedJwtException ex) {
            handleJwtException(response, "Invalid token", HttpServletResponse.SC_FORBIDDEN);
            return;
        } catch (Exception ex) {
            handleJwtException(response, "Authentication error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleJwtException(HttpServletResponse response, String message, int statusCode)
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(
                String.format("{\"error\": \"%s\", \"status\": %d}", message, statusCode)
        );
    }
}



