package api.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
public class JwtServiceTest {


    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("my-secret-key");
    }

    @Test
    @DisplayName("Should generate a valid token")
    void generateToken_ShouldReturnToken() {
        String token = jwtService.generatedToken("teste@gmail.com");

        Assertions.assertThat(token).isNotBlank();

    }

    @Test
    @DisplayName("Should return true when token is valid")
    void verifyToken_WhenTokenIsValid_ShouldReturnTrue(){
        String token=jwtService.generatedToken("teste@gmail.com");

        boolean result = jwtService.verifyToken(token);

        Assertions.assertThat(token).isNotBlank();
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when token is invalid")
    void verifyToken_WhenTokenIsInvalid_ShouldReturnFalse() {

        boolean result = jwtService.verifyToken("token-invalido");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should extract username from valid token")
    void extractUsername_ShouldReturnEmail(){
        String email= "test@gmail.com";
        String token= jwtService.generatedToken(email);
        String username= jwtService.extractUsername(token);

        Assertions.assertThat(token).isNotBlank();
        Assertions.assertThat(username).isEqualTo(email);
    }
}
