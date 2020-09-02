package per.lai.forum.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import per.lai.forum.security.UserDetailsImpl;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private static String jwtSecret;
    private static int jwtExpirationMs;

    @Value("${macro.app.jwtSecret}")
    public  void setJwtSecret(String jwtSecret) {
        JwtUtils.jwtSecret = jwtSecret;
    }

    @Value("${macro.app.jwtExpirationMs}")
    public  void setJwtExpirationMs(int jwtExpirationMs) {
        JwtUtils.jwtExpirationMs = jwtExpirationMs;
    }

    public static String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64URL.encode(jwtSecret))
                .compact();
    }
    public static String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(TextCodec.BASE64URL.encode(jwtSecret)).parseClaimsJws(token).getBody().getSubject();
    }
    public static boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(TextCodec.BASE64URL.encode(jwtSecret)).parseClaimsJws(authToken);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
