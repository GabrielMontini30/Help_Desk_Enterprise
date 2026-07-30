package api.repository;

import api.entity.User;
import api.factory.UserFactory;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return user when email exists")
     void findByEmail_WhenEmailExists_ShouldReturnUser(){
        User user= UserFactory.createValidUser();

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(result).isPresent();

        Assertions.assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.get().getRole()).isEqualTo(user.getRole());
        Assertions.assertThat(result.get().getName()).isEqualTo(user.getName());
        Assertions.assertThat(result.get().getDepartment()).isEqualTo(user.getDepartment());

    }

    @Test
    @DisplayName("should return empty when does not exist")
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty(){

        Optional<User> result = userRepository.findByEmail("notexistemail@gmail.com");

        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmail_WhenEmailExists_ShouldReturnTrue(){
        User user= UserFactory.createValidUser();
        userRepository.save(user);

        boolean result = userRepository.existsByEmail(user.getEmail());

        Assertions.assertThat(result).isTrue();

    }

    @Test
    @DisplayName("should return false when does not exist email")
    void  existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse(){

        boolean result = userRepository.existsByEmail("notexistemail@gmail.com");

        Assertions.assertThat(result).isFalse();
    }

}
