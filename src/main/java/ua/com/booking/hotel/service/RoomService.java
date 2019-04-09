package ua.com.booking.hotel.service;

import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.entity.core.RoomCategoryType;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    List<Room> getRoomsByCategory(RoomCategoryType roomCategory);

    List<Room> getRoomsByPeriodAndCategory(Period period, RoomCategoryType roomCategory);

    List<Room> getAvailableRoomsBySpecifiedDays(Period period);

    BookedRoom bookRoom(Room room, User user, Period period);

    Optional<Room> getRoomById(long id);
}