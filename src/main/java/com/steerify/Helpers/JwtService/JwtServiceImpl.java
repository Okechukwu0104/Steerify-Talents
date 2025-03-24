package com.steerify.Helpers.JwtService;

import com.steerify.Entities.JwtUser;
import com.steerify.Helpers.LoginResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Override
    public LoginResponseDto generateToken(JwtUser loggedInUser) {
        String token = Jwts.builder()
                .setSubject(loggedInUser.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        return new LoginResponseDto(
                loggedInUser.getId(),
                loggedInUser.getFirstName(),
                loggedInUser.getLastName(),
                loggedInUser.getEmail(),
                token,
                loggedInUser.getRole()
        );
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Boolean validateToken(String token, JwtUser user) {
        return extractUsername(token).equals(user.getEmail()) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())).build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}