//package com.project.backend.repositories;
//
//import com.project.backend.TestUtil;
//import com.project.backend.models.School;
//import com.project.backend.models.User;
//import com.project.backend.repositories.repos.SchoolRepository;
//import com.project.backend.repositories.repos.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.List;
//import java.util.Optional;
//
//import static com.project.backend.TestUtil.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private SchoolRepository schoolRepository;
//
//    private User user1;
//    private User user2;
//
//    private School school;
//
//    @BeforeEach
//    void setUp() {
//        user1 = userRepository.save(createUser("TEACHER", "teacher@gmail.com", "keycloak1"));
//        user2 = userRepository.save(createUser("PARENT", "parent@gmail.com", "keycloak2"));
//
//        school = schoolRepository.save(createSchool("New School", user1));
//
//        assignUsersToSchool(school, user1, user2);
//
//        userRepository.saveAll(List.of(user1, user2));
//    }
//
//    @Test
//    public void findByEmail_success() {
//        String email = user1.getEmail();
//
//        var foundUser = userRepository.findByEmail(email);
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getEmail()).isEqualTo(email);
//    }
//
//    @Test
//    public void findByEmail_notFound() {
//        String nonExistingEmail = "notfound@test.com";
//
//        Optional<User> foundUser = userRepository.findByEmail(nonExistingEmail);
//
//        assertThat(foundUser).isEmpty();
//    }
//
//    @Test
//    public void existsByEmail_success() {
//        String email = user2.getEmail();
//
//        boolean exists = userRepository.existsByEmail(email);
//
//        assertThat(exists).isTrue();
//    }
//
//    @Test
//    public void existsByEmail_notFound() {
//        String nonExistingEmail = "notfound@test.com";
//
//        boolean exists = userRepository.existsByEmail(nonExistingEmail);
//
//        assertThat(exists).isFalse();
//    }
//
//    @Test
//    public void findByKeycloakUserId_success() {
//        String keycloakUserId = user1.getKeycloakUserId();
//
//        var foundUser = userRepository.findByKeycloakUserId(keycloakUserId);
//
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getKeycloakUserId()).isEqualTo(keycloakUserId);
//    }
//
//    @Test
//    public void findByKeycloakUserId_notFound() {
//        String nonExistingKeycloakUserId = "keycloakNotFound";
//
//        Optional<User> foundUser = userRepository.findByKeycloakUserId(nonExistingKeycloakUserId);
//
//        assertThat(foundUser).isEmpty();
//    }
//
//    @Test
//    public void countAllBySchoolIdAndRoleIs() {
//        long schoolId = school.getId();
//
//        long count = userRepository.countAllBySchool_IdAndRoleIs(schoolId, "TEACHER");
//        assertEquals(1, count);
//
//        user2.setRole("TEACHER");
//        userRepository.save(user2);
//
//        count = userRepository.countAllBySchool_IdAndRoleIs(schoolId, "TEACHER");
//        assertEquals(2, count);
//
//        user1.setSchool(null);
//        user2.setSchool(null);
//        userRepository.saveAll(List.of(user1, user2));
//
//        count = userRepository.countAllBySchool_IdAndRoleIs(schoolId, "TEACHER");
//        assertEquals(0, count);
//    }
//}
