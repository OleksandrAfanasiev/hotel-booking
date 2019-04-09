package ua.com.booking.hotel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.entity.core.RoomCategoryType;
import ua.com.booking.hotel.repository.BookedRoomRepository;
import ua.com.booking.hotel.repository.RoomRepository;
import ua.com.booking.hotel.service.RoomService;

import javax.persistence.EntityExistsException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private RoomRepository roomRepository;

    private BookedRoomRepository bookedRoomRepository;

    @Autowired
    RoomServiceImpl(RoomRepository roomRepository, BookedRoomRepository bookedRoomRepository) {
        this.roomRepository = roomRepository;
        this.bookedRoomRepository = bookedRoomRepository;
    }


    @Override
    public List<Room> getRoomsByCategory(RoomCategoryType roomCategory) {
        return roomRepository.findAllByRoomCategory(roomCategory);
    }

    @Override
    public List<Room> getRoomsByPeriodAndCategory(Period period, RoomCategoryType roomCategory) {
        checkIsStartDateEarlierThenEndDate(period);
        return getAvailableRoomsBySpecifiedDays(period)
                .stream()
                .filter(room -> room.getRoomCategory().equals(roomCategory))
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> getAvailableRoomsBySpecifiedDays(Period period) {
        checkIsStartDateEarlierThenEndDate(period);
        addHoursToPeriod(period);
        List<Room> bookedRoomsBySpecifiedDays = getBookedRoomsByPeriod(period);
        if (bookedRoomsBySpecifiedDays.isEmpty()) {
            return roomRepository.findAll();
        }
        return roomRepository.findAll()
                .stream()
                .filter(room -> !bookedRoomsBySpecifiedDays.contains(room))
                .collect(Collectors.toList());
    }

    @Override
    public BookedRoom bookRoom(Room room, User user, Period period) {
        checkIsStartDateEarlierThenEndDate(period);
        addHoursToPeriod(period);
        if (getBookedRoomsByPeriod(period).contains(room)) {
            throw new EntityExistsException(String.format("Room: '%s' already booked on this dates: from '%s' to '%s'.", room, period.getStartDate(), period.getEndDate()));
        }
        return bookedRoomRepository.saveAndFlush(new BookedRoom(0, room, user, period));
    }

    @Override
    public Optional<Room> getRoomById(long id) {
        return roomRepository.findById(id);
    }

    private List<Room> getBookedRoomsByPeriod(Period period) {
        return bookedRoomRepository.findAllBookedRoomsByDates(period.getStartDate(), period.getEndDate())
                .stream()
                .map(BookedRoom::getRoom)
                .collect(Collectors.toList());
    }

    private void addHoursToPeriod(Period period) {
        period.getStartDate().setTime(period.getStartDate().getTime() + 13 * 60 * 60 * 1000);
        period.getEndDate().setTime(period.getEndDate().getTime() + 12 * 60 * 60 * 1000);
    }

    private void checkIsStartDateEarlierThenEndDate(Period period) {
        if (period.getStartDate().after(period.getEndDate()) || period.getStartDate().before(Timestamp.from(Instant.now())) ) {
            throw new IllegalArgumentException(String.format("Start date: '%s' should be earlier then end date '%s', and after today.", period.getStartDate(), period.getEndDate()));
        }
    }
}
