package ua.com.booking.hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.service.UserService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String URL = "/user";

    @Autowired
    private MockMvc server;

    @MockBean
    private UserService userService;

    @Test
    public void successCreateNewUser() throws Exception {
        String username = "James";
        User user = new User(0, username);

        when(userService.create(user)).thenReturn(user);
        server
                .perform(post(URL.concat("/create"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(username))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(user)));

        verify(userService, times(1)).create(user);
        verifyNoMoreInteractions(userService);
    }
}
