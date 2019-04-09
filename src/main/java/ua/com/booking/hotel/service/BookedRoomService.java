package ua.com.booking.hotel.service;

import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.User;

import java.util.List;
import java.util.Optional;

public interface BookedRoomService {

    List<BookedRoom> getBookedRoomsByUser(User user);

    Long getBookingTotalPriceByUser(User user);

    List<BookedRoom> getAllBookedRooms();

    Optional<BookedRoom> getBookedRoomById(long id);

    Long getBookingTotalPriceByReservation(BookedRoom bookedRoom);
}
