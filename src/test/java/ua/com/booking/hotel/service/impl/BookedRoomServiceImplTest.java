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
public class BookedRoomServiceImplTest {

    private static final long ID = 1;

    @Mock
    private BookedRoomRepository bookedRoomRepository;

    @InjectMocks
    private BookedRoomServiceImpl bookedRoomService;

    private User user;

    private List<BookedRoom> bookedRooms;

    private BookedRoom bookedRoom;

    @Before
    public void setUp() {
        Room room = new Room(ID, 101, RoomCategoryType.SINGLE, 100, new HashSet<>());
        user = new User(ID, "UserName");
        Period period = new Period(Timestamp.valueOf("2019-10-10 0:0:0.0"), Timestamp.valueOf("2019-10-14 0:0:0.0"));
        bookedRoom = new BookedRoom(ID, room, user, period);
        bookedRooms = new ArrayList<>(Collections.singleton(bookedRoom));

    }

    @Test
    public void whenGetBookedRoomsByUserReturnList() {
        when(bookedRoomRepository.findAllByUser(user)).thenReturn(bookedRooms);

        List<BookedRoom> result = bookedRoomService.getBookedRoomsByUser(user);

        assertEquals(bookedRooms, result);
    }

    @Test
    public void whenGetAllBookedRoomsReturnList() {
        when(bookedRoomRepository.findAll()).thenReturn(bookedRooms);

        List<BookedRoom> result = bookedRoomService.getAllBookedRooms();

        assertEquals(bookedRooms, result);
    }

    @Test
    public void whenGetBookingTotalPriceByUserReturnPrice() {
        Long totalPrice = 500L;
        when(bookedRoomRepository.findAllByUser(user)).thenReturn(bookedRooms);

        Long result = bookedRoomService.getBookingTotalPriceByUser(user);

        assertEquals(totalPrice, result);
    }

    @Test
    public void whenGetBookingTotalPriceByReservationReturnPrice() {
        Long totalPrice = 500L;

        Long result = bookedRoomService.getBookingTotalPriceByReservation(bookedRoom);

        assertEquals(totalPrice, result);
    }

    @Test
    public void whenBookedRoomByIdReturnBookedRoom() {
        when(bookedRoomRepository.findById(ID)).thenReturn(Optional.ofNullable(bookedRoom));

        BookedRoom result = bookedRoomService.getBookedRoomById(ID).orElse(null);

        assertEquals(bookedRoom, result);
    }

    @Test
    public void whenBookedRoomByIdReturnOptionalEmpty() {
        when(bookedRoomRepository.findById(ID)).thenReturn(Optional.empty());

        Optional<BookedRoom> result = bookedRoomService.getBookedRoomById(ID);

        assertFalse(result.isPresent());
    }
}
