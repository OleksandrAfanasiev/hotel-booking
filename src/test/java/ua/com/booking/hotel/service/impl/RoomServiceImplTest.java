package ua.com.booking.hotel.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.entity.core.RoomCategoryType;
import ua.com.booking.hotel.repository.BookedRoomRepository;
import ua.com.booking.hotel.repository.RoomRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomServiceImplTest {

    private static final Timestamp START_DATE = Timestamp.valueOf("2019-10-10 0:0:0.0");

    private static final Timestamp END_DATE = Timestamp.valueOf("2019-10-15 0:0:0.0");

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookedRoomRepository bookedRoomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room1;

    private Room room2;

    private Room room3;

    private BookedRoom bookedRoom1;

    private BookedRoom bookedRoom2;

    private User user;

    private Period period1;

    private Period incorrectPeriod;

    private List<Room> rooms;

    @Before
    public void setUp() {
        room1 = new Room(1, 101, RoomCategoryType.SINGLE, 100, new HashSet<>());
        room2 = new Room(2, 201, RoomCategoryType.SINGLE, 100, new HashSet<>());
        room3 = new Room(3, 301, RoomCategoryType.DOUBLE, 200, new HashSet<>());

        user = new User(1, "UserName");

        period1 = new Period(START_DATE, END_DATE);
        Period period2 = new Period(Timestamp.valueOf("2016-10-10 0:0:0.0"), Timestamp.valueOf("2016-10-15 0:0:0.0"));
        incorrectPeriod = new Period(END_DATE, START_DATE);

        bookedRoom1 = new BookedRoom(1, room1, user, period1);
        bookedRoom2 = new BookedRoom(2, room2, user, period2);

        rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
    }

    @Test
    public void whenGetRoomsByCategoryReturnList() {
        when(roomRepository.findAllByRoomCategory(RoomCategoryType.SINGLE)).thenReturn(rooms);

        List<Room> result = roomService.getRoomsByCategory(RoomCategoryType.SINGLE);

        assertEquals(rooms, result);
    }

    @Test
    public void whenGetRoomsByPeriodAndCategoryReturnList() {
        List<Room> expectResult = Collections.singletonList(room2);

        when(bookedRoomRepository.findAllBookedRoomsByDates(START_DATE, END_DATE)).thenReturn(Collections.singletonList(bookedRoom1));
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getRoomsByPeriodAndCategory(period1, RoomCategoryType.SINGLE);

        assertEquals(expectResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGetRoomsByPeriodAndCategoryThrowsExceptionIfDatesInputIncorrect() {
        roomService.getRoomsByPeriodAndCategory(incorrectPeriod, RoomCategoryType.SINGLE);
    }

    @Test
    public void whenGetAvailableRoomsBySpecifiedDaysReturnList() {
        List<Room> expectResult = new ArrayList<>();
        expectResult.add(room2);
        expectResult.add(room3);

        when(bookedRoomRepository.findAllBookedRoomsByDates(START_DATE, END_DATE)).thenReturn(Collections.singletonList(bookedRoom1));
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAvailableRoomsBySpecifiedDays(period1);

        assertEquals(expectResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenGetAvailableRoomsBySpecifiedDaysThrowsExceptionIfDatesInputIncorrect() {
        roomService.getAvailableRoomsBySpecifiedDays(incorrectPeriod);
    }

    @Test
    public void whenBookRoomReturnBookedRoom() {

        period1.getStartDate().setTime(period1.getStartDate().getTime() + 13 * 60 * 60 * 1000);
        period1.getEndDate().setTime(period1.getEndDate().getTime() + 12 * 60 * 60 * 1000);

        when(bookedRoomRepository.findAllBookedRoomsByDates(START_DATE, END_DATE)).thenReturn(Collections.singletonList(bookedRoom1));
        when(bookedRoomRepository.saveAndFlush(new BookedRoom(0, room2, user, period1))).thenReturn(bookedRoom2);

        BookedRoom bookedRoom = roomService.bookRoom(room2, user, period1);

        assertEquals(bookedRoom2, bookedRoom);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenBookRoomThenThrowsExceptionIfDatesInputIncorrect() {
        roomService.bookRoom(room2, user, incorrectPeriod);
    }

    @Test
    public void whenGetRoomsByIdReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.ofNullable(room1));

        Room result = roomService.getRoomById(1L).orElse(null);

        assertEquals(room1, result);
    }

    @Test
    public void whenGetRoomsByIdReturnOptionalEmpty() {
        when(roomRepository.findById(11L)).thenReturn(Optional.empty());

        Optional<Room> result = roomService.getRoomById(11L);

        assertFalse(result.isPresent());
    }
}
