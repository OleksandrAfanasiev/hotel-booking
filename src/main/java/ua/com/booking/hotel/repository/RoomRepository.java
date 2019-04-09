package ua.com.booking.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.core.RoomCategoryType;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByRoomCategory(RoomCategoryType roomCategory);
}
