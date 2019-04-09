package ua.com.booking.hotel.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.booking.hotel.entity.User;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ITUserRepositoryTest {

    private static final String USER_NAME = "Lemmy";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        User user = new User(0, USER_NAME);

        entityManager.persistAndFlush(user);
    }

    @Test
    public void whenExistsByNameInvokedOnExistingUser() {
        assertTrue(userRepository.existsByUsername(USER_NAME));
    }

    @Test
    public void whenExistsByNameInvokedOnNotExistingUser() {
        assertFalse(userRepository.existsByUsername("Rob"));
    }
}
