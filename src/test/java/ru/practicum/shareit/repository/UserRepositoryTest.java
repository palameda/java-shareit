package ru.practicum.shareit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.config.AppConfig;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.db.UserDbRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@Import(AppConfig.class)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDbRepository userRepository;

    @Test
    public void contextLoads() {
        assertThat(entityManager, is(notNullValue()));
    }

    @Test
    @DisplayName("Тест медота save")
    public void testShouldSaveUserSuccessfully() {
        User testUser = User.builder()
                .name("Name")
                .email("user@user.com")
                .build();
        assertThat(testUser.getId(), equalTo(null));

        userRepository.save(testUser);
        assertThat(testUser.getId(), is(notNullValue()));
    }

    @Test
    @DisplayName("Тест медота update")
    public void testShouldUpdateUserSuccessfully() {
        User testUser = User.builder()
                .name("Name")
                .email("user@user.com")
                .build();
        userRepository.save(testUser);

        User updateUser = User.builder()
                .id(testUser.getId())
                .name("Update")
                .email("update@user.com")
                .build();
        userRepository.save(updateUser);

        assertThat(testUser.getId(), equalTo(updateUser.getId()));
        assertThat(testUser.getName(), equalTo(updateUser.getName()));
        assertThat(testUser.getEmail(), equalTo(updateUser.getEmail()));
    }

    @Test
    @DisplayName("Тест медота findById")
    public void testShouldFindUserById() {
        User testUser = User.builder()
                .name("Name")
                .email("user@user.com")
                .build();
        userRepository.save(testUser);

        Optional<User> foundUser = userRepository.findById(testUser.getId());
        assertThat(foundUser.isPresent(), is(Boolean.TRUE));
        assertThat(foundUser.get().getId(), equalTo(testUser.getId()));
        assertThat(foundUser.get().getName(), equalTo(testUser.getName()));
        assertThat(foundUser.get().getEmail(), equalTo(testUser.getEmail()));
    }

    @Test
    @DisplayName("Тест медота findAll")
    public void testShouldFindAllUsers() {
        User user1 = User.builder()
                .name("Name1")
                .email("user1@user.com")
                .build();
        User user2 = User.builder()
                .name("Name2")
                .email("user2@user.com")
                .build();
        assertThat(userRepository.findAll().isEmpty(), is(Boolean.TRUE));

        userRepository.save(user1);
        userRepository.save(user2);
        assertThat(userRepository.findAll().isEmpty(), is(Boolean.FALSE));
        assertThat(userRepository.findAll().size(), equalTo(2));
    }

    @Test
    @DisplayName("Тест медота delete")
    public void testShouldDeleteUserById() {
        User user1 = User.builder()
                .name("Name1")
                .email("user1@user.com")
                .build();
        userRepository.save(user1);
        User user2 = User.builder()
                .name("Name2")
                .email("user2@user.com")
                .build();
        userRepository.save(user2);
        assertThat(userRepository.findAll().size(), equalTo(2));

        userRepository.deleteById(user2.getId());
        assertThat(userRepository.findAll().size(), equalTo(1));
    }
}
