package ua.com.booking.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.core.RoomCategoryType;
import ua.com.booking.hotel.entity.dto.PeriodAndCategoryDto;
import ua.com.booking.hotel.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private RoomService roomService;

    @Autowired
    RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/filter/by/period")
    public List<Room> getRoomsByFilterByPeriod(@RequestBody Period period) {
        return roomService.getAvailableRoomsBySpecifiedDays(period);
    }

    @GetMapping("/filter/by/category/{category}")
    public List<Room> getRoomsFilterByCategory(@PathVariable("category") RoomCategoryType category) {
        return roomService.getRoomsByCategory(category);
    }

    @PostMapping("/filter/by/period/category")
    public List<Room> getRoomsFilterByPeriodAndCategory(@RequestBody PeriodAndCategoryDto filter) {
        return roomService.getRoomsByPeriodAndCategory(filter.getPeriod(), filter.getCategoryType());
    }
}
