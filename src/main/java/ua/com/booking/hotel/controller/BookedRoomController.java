package ua.com.booking.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.entity.dto.BookRoomDto;
import ua.com.booking.hotel.service.BookedRoomService;
import ua.com.booking.hotel.service.RoomService;
import ua.com.booking.hotel.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/booked")
public class BookedRoomController {

    private BookedRoomService bookedRoomService;

    private UserService userService;

    private RoomService roomService;

    @Autowired
    BookedRoomController(BookedRoomService bookedRoomService,
                         UserService userService,
                         RoomService roomService) {
        this.bookedRoomService = bookedRoomService;
        this.userService = userService;
        this.roomService = roomService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BookedRoom bookRoom(@RequestBody BookRoomDto bookRoomDto) {
        Room room = roomService.getRoomById(bookRoomDto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such room with id: '%d'.", bookRoomDto.getRoomId())));
        User user = userService.getUserById(bookRoomDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such user with id: '%d'.", bookRoomDto.getUserId())));
        return roomService.bookRoom(room, user, bookRoomDto.getPeriod());
    }

    @GetMapping("/by/user/{userId}")
    public List<BookedRoom> getBookedRoomsByUser(@PathVariable("userId") long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such user with id: '%d'", userId)));
        return bookedRoomService.getBookedRoomsByUser(user);
    }

    @GetMapping("/total/price/by/user/{userId}")
    public Long getTotalPriceByUser(@PathVariable("userId") long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such user with id: '%d'", userId)));
        return bookedRoomService.getBookingTotalPriceByUser(user);
    }

    @GetMapping("/total/price/by/reservation/{bookingId}")
    public Long getTotalPriceByUserByBooking(@PathVariable("bookingId") long bookingId) {
        BookedRoom bookedRoom = bookedRoomService.getBookedRoomById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No such reservation with id: '%d'", bookingId)));
        return bookedRoomService.getBookingTotalPriceByReservation(bookedRoom);
    }

    @GetMapping
    public List<BookedRoom> getAllBookedRooms() {
        return bookedRoomService.getAllBookedRooms();
    }
}
