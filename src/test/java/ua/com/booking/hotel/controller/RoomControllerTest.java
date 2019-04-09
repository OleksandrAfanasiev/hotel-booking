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
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.core.RoomCategoryType;
import ua.com.booking.hotel.entity.dto.PeriodAndCategoryDto;
import ua.com.booking.hotel.service.RoomService;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    private static final String URL = "/room";

    private static final Timestamp START_DATE = Timestamp.valueOf("2019-10-10 0:0:0.0");

    private static final Timestamp END_DATE = Timestamp.valueOf("2019-11-11 0:0:0.0");

    private static final RoomCategoryType CATEGORY_TYPE = RoomCategoryType.SINGLE;

    @Autowired
    private MockMvc server;

    @MockBean
    RoomService roomService;

    private List<Room> rooms;

    private Period period;

    private PeriodAndCategoryDto filter;

    @Before
    public void setUp() {
        Room room = new Room(1, 101, CATEGORY_TYPE, 100, new HashSet<>());
        rooms = Collections.singletonList(room);
        period = new Period(START_DATE, END_DATE);
        filter = new PeriodAndCategoryDto(period, CATEGORY_TYPE);
    }

    @Test
    public void returnListWhenGetRoomsByFilterByPeriod() throws Exception {
        when(roomService.getAvailableRoomsBySpecifiedDays(period)).thenReturn(rooms);
        server
                .perform(post(URL.concat("/filter/by/period"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(period)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(rooms)));

        verify(roomService, times(1)).getAvailableRoomsBySpecifiedDays(period);
        verifyNoMoreInteractions(roomService);
    }

    @Test
    public void returnListWhenGetRoomsFilterByCategory() throws Exception {
        when(roomService.getRoomsByCategory(CATEGORY_TYPE)).thenReturn(rooms);
        server
                .perform(get(URL.concat("/filter/by/category/{category}"), CATEGORY_TYPE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(rooms)));

        verify(roomService, times(1)).getRoomsByCategory(CATEGORY_TYPE);
        verifyNoMoreInteractions(roomService);
    }

    @Test
    public void returnListWhenGetRoomsFilterByPeriodAndCategory() throws Exception {
        when(roomService.getRoomsByPeriodAndCategory(period, CATEGORY_TYPE)).thenReturn(rooms);
        server
                .perform(post(URL.concat("/filter/by/period/category"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(filter)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(rooms)));

        verify(roomService, times(1)).getRoomsByPeriodAndCategory(period, CATEGORY_TYPE);
        verifyNoMoreInteractions(roomService);
    }
}
