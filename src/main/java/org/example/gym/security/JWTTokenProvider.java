
package org.example.gym.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.gym.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JWTTokenProvider {

    public String generateToken(Authentication authentication) {
        Map<String, Object> claimsMap = new HashMap<>();
        String userId = "";
        String username = "";

        if (authentication.getPrincipal() instanceof UserEntity) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            userId = Long.toString(user.getId());
            username = user.getUsername();
            claimsMap.put("id", userId);
            claimsMap.put("username", username);
        }

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime()
                + SecurityConstants.EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");
        return Integer.parseInt(id);
    }
}
