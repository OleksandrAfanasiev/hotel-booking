package ua.com.booking.hotel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.booking.hotel.entity.AdditionalOption;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.repository.BookedRoomRepository;
import ua.com.booking.hotel.service.BookedRoomService;

import java.util.List;
import java.util.Optional;

@Service
public class BookedRoomServiceImpl implements BookedRoomService {

    private BookedRoomRepository bookedRoomRepository;

    @Autowired
    BookedRoomServiceImpl(BookedRoomRepository bookedRoomRepository) {
        this.bookedRoomRepository = bookedRoomRepository;
    }

    @Override
    public List<BookedRoom> getBookedRoomsByUser(User user) {
        return bookedRoomRepository.findAllByUser(user);
    }

    @Override
    public Long getBookingTotalPriceByUser(User user) {
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAllByUser(user);
        if (!bookedRooms.isEmpty()) {
            return bookedRooms
                    .stream()
                    .map(BookedRoomServiceImpl::countOneBookAmountMoney)
                    .mapToLong(Long::longValue).sum();
        }
        return null;
    }

    @Override
    public List<BookedRoom> getAllBookedRooms() {
        return bookedRoomRepository.findAll();
    }

    @Override
    public Optional<BookedRoom> getBookedRoomById(long id) {
        return bookedRoomRepository.findById(id);
    }

    @Override
    public Long getBookingTotalPriceByReservation(BookedRoom bookedRoom) {
        return countOneBookAmountMoney(bookedRoom);
    }

    private static long countOneBookAmountMoney(BookedRoom bookedRoom) {
        long daysAmount = (bookedRoom.getBookedDates().getEndDate().getTime()
                - bookedRoom.getBookedDates().getStartDate().getTime())
                / (1000 * 60 * 60 * 24) + 1;

        return (bookedRoom.getRoom().getPrice()
                + bookedRoom.getRoom().getAdditionalOptions()
                .stream()
                .map(AdditionalOption::getPrice)
                .mapToLong(Long::longValue).sum())
                * daysAmount;
    }
}
