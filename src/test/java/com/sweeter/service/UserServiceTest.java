package com.sweeter.service;

import com.sweeter.domain.Role;
import com.sweeter.domain.User;
import com.sweeter.repos.UserRepo;
import java.util.Collections;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void addUser() {
        User user = new User();

        boolean isUserCreatad = userService.addUser(user);

        Assert.assertTrue(isUserCreatad);
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void addUserFalse() {
        User user = new User();
        user.setUsername("Jhon");
        
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Jhon");

        boolean isUserCreatad = userService.addUser(user);

        Assert.assertFalse(isUserCreatad);
        
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}
