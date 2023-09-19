package com.example.cloudfilestorage.repository;

import com.example.cloudfilestorage.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser() {
        // arrange
        UserEntity user = new UserEntity();
        user.setUsername("sigma");
        user.setPassword("123456");

        // act
        UserEntity savedUser = userRepository.save(user);

        // assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThenOneUser() {
        int alreadyExistUsers = userRepository.findAll().size();

        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setPassword("123456");

        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setPassword("123456");

        userRepository.save(user1);
        userRepository.save(user2);

        List<UserEntity> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(Math.abs(alreadyExistUsers + 2));
    }
}
