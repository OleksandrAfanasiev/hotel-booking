package ua.com.booking.hotel.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final long ID = 1;

    private static final String USER_NAME = "UserName";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @Before
    public void setUp() {
        user = new User(ID, USER_NAME);
    }

    @Test
    public void whenCreateUserReturnUser() {
        when(userRepository.existsByUsername(USER_NAME)).thenReturn(false);
        when(userRepository.saveAndFlush(user)).thenReturn(user);

        User result = userService.create(user);

        assertEquals(user, result);
    }

    @Test(expected = EntityExistsException.class)
    public void whenCreateUserWithExistsUserNameThrowException() {
        when(userRepository.existsByUsername(USER_NAME)).thenReturn(true);

        userService.create(user);
    }

    @Test
    public void whenGetUserByIdReturnUser() {
        when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(user));

        User result = userService.getUserById(ID).orElse(null);

        assertEquals(user, result);
    }

    @Test
    public void whenGetUserByIdReturnOptionalEmpty() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(ID);

        assertFalse(result.isPresent());
    }
}
