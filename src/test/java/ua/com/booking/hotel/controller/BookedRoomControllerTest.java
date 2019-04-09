package ua.com.booking.hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.entity.core.RoomCategoryType;
import ua.com.booking.hotel.entity.dto.BookRoomDto;
import ua.com.booking.hotel.service.BookedRoomService;
import ua.com.booking.hotel.service.RoomService;
import ua.com.booking.hotel.service.UserService;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookedRoomController.class)
public class BookedRoomControllerTest {

    private static final String URL = "/booked";

    private static final String URL_CREATE = "/booked/create";

    private static final long ID = 1;

    private static final Timestamp START_DATE = Timestamp.valueOf("2019-10-10 0:0:0.0");

    private static final Timestamp END_DATE = Timestamp.valueOf("2019-11-11 0:0:0.0");

    private static final long TOTAL_PRICE = 500;

    @Autowired
    private MockMvc server;

    @MockBean
    BookedRoomService bookedRoomService;

    @MockBean
    UserService userService;

    @MockBean
    RoomService roomService;

    private BookRoomDto bookRoomDto;

    private BookedRoom bookedRoom;

    private Room room;

    private User user;

    private Period period;

    @Before
    public void setUp() {
        room = new Room(ID, 101, RoomCategoryType.SINGLE, 100, new HashSet<>());
        user = new User(ID, "UserName");
        period = new Period(START_DATE, END_DATE);
        bookedRoom = new BookedRoom(ID, room, user, period);
        bookRoomDto = new BookRoomDto(ID, ID, period);
    }

    @Test
    public void successCreateNewBookedRoom() throws Exception {
        when(roomService.getRoomById(ID)).thenReturn(Optional.ofNullable(room));
        when(userService.getUserById(ID)).thenReturn(Optional.ofNullable(user));
        when(roomService.bookRoom(room, user, period)).thenReturn(bookedRoom);
        server
                .perform(post(URL_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(bookRoomDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(roomService, times(1)).getRoomById(ID);
        verify(userService, times(1)).getUserById(ID);
        verify(roomService, times(1)).bookRoom(room, user, period);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(roomService);
    }

    @Test
    public void failedWhenCreateNewBookedRoomWhenUserNotFound() throws Exception {
        when(roomService.getRoomById(ID)).thenReturn(Optional.ofNullable(room));
        when(userService.getUserById(ID)).thenReturn(Optional.empty());
        server
                .perform(post(URL_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(bookRoomDto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(roomService, times(1)).getRoomById(ID);
        verify(userService, times(1)).getUserById(ID);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(roomService);
    }

    @Test
    public void failedWhenCreateNewBookedRoomWhenRoomNotFound() throws Exception {
        when(roomService.getRoomById(ID)).thenReturn(Optional.empty());
        server
                .perform(post(URL_CREATE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(bookRoomDto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(roomService, times(1)).getRoomById(ID);
        verifyNoMoreInteractions(roomService);
    }

    @Test
    public void returnListOfBookedRoomsWhenGetBookedRoomsByUser() throws Exception {
        when(userService.getUserById(ID)).thenReturn(Optional.ofNullable(user));
        when(bookedRoomService.getBookedRoomsByUser(user)).thenReturn(Collections.singletonList(bookedRoom));
        server
                .perform(get(URL.concat("/by/user/{userId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(ID);
        verify(bookedRoomService, times(1)).getBookedRoomsByUser(user);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookedRoomService);
    }

    @Test
    public void returnNotFoundWhenGetBookedRoomsByUserIfUserNotFound() throws Exception {
        when(userService.getUserById(ID)).thenReturn(Optional.empty());
        server
                .perform(get(URL.concat("/by/user/{userId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void returnTotalPriceWhenGetTotalPriceByUser() throws Exception {
        when(userService.getUserById(ID)).thenReturn(Optional.ofNullable(user));
        when(bookedRoomService.getBookingTotalPriceByUser(user)).thenReturn(TOTAL_PRICE);
        server
                .perform(get(URL.concat("/total/price/by/user/{userId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(TOTAL_PRICE)));

        verify(userService, times(1)).getUserById(ID);
        verify(bookedRoomService, times(1)).getBookingTotalPriceByUser(user);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(bookedRoomService);
    }

    @Test
    public void returnNotFoundWhenGetTotalPriceByUserIfUserNotFound() throws Exception {
        when(userService.getUserById(ID)).thenReturn(Optional.empty());
        server
                .perform(get(URL.concat("/total/price/by/user/{userId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void returnTotalPriceWhenGetTotalPriceByReservation() throws Exception {
        when(bookedRoomService.getBookedRoomById(ID)).thenReturn(Optional.ofNullable(bookedRoom));
        when(bookedRoomService.getBookingTotalPriceByReservation(bookedRoom)).thenReturn(TOTAL_PRICE);
        server
                .perform(get(URL.concat("/total/price/by/reservation/{bookingId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(TOTAL_PRICE)));

        verify(bookedRoomService, times(1)).getBookedRoomById(ID);
        verify(bookedRoomService, times(1)).getBookingTotalPriceByReservation(bookedRoom);
        verifyNoMoreInteractions(bookedRoomService);
    }

    @Test
    public void returnNotFoundWhenGetTotalPriceByReservationWhenReservationNotFound() throws Exception {
        when(bookedRoomService.getBookedRoomById(ID)).thenReturn(Optional.empty());
        server
                .perform(get(URL.concat("/total/price/by/reservation/{bookingId}"), String.valueOf(ID)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(bookedRoomService, times(1)).getBookedRoomById(ID);
        verifyNoMoreInteractions(bookedRoomService);
    }

    @Test
    public void returnListWhenGetAllBookedRooms() throws Exception {
        when(bookedRoomService.getAllBookedRooms()).thenReturn(Collections.singletonList(bookedRoom));
        server
                .perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookedRoomService, times(1)).getAllBookedRooms();
        verifyNoMoreInteractions(bookedRoomService);
    }
}
