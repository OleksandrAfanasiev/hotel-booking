package ua.com.booking.hotel.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.com.booking.hotel.service.BookedRoomService;
import ua.com.booking.hotel.service.RoomService;
import ua.com.booking.hotel.service.UserService;

import javax.persistence.EntityNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookedRoomController.class)
public class ExceptionHandlerControllerTest {

    private static final String URL = "/booked";

    private static final String MESSAGE = "No such user!";

    private static final long ID = 1;

    @Autowired
    private MockMvc server;

    @MockBean
    UserService userService;

    @MockBean
    BookedRoomService bookedRoomService;

    @MockBean
    RoomService roomService;

    @Test
    public void shouldInterceptEntityNotFoundExceptionWhenControllerThrow() throws Exception {

        when(userService.getUserById(ID)).thenThrow(new EntityNotFoundException(MESSAGE));

        MvcResult result = server
                .perform(get(URL.concat("/by/user/{userId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(MESSAGE, result.getResponse().getContentAsString());
    }
}
