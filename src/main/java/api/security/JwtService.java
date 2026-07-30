package api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String secret;

    public JwtService(@Value("${api.security.token.secret}") String secret) {
        this.secret = secret;
    }

    public String generatedToken(String email){
        Algorithm algorithm= Algorithm.HMAC256(secret);

    return  JWT.create()
                .withIssuer("api-auth")
                .withSubject(email)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis()+ 24*60*60*10000))
                .sign(algorithm);
    }

    public boolean verifyToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWT.require(algorithm)
                    .withIssuer("api-auth")
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token){
        Algorithm algorithm= Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("api-auth")
                .build()
                .verify(token)
                .getSubject();
    }

}
