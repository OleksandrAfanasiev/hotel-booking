package ua.com.booking.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.User;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {

    @Query("SELECT c FROM BookedRoom c WHERE :startDate BETWEEN c.bookedDates.startDate AND c.bookedDates.endDate " +
            "OR :endDate BETWEEN c.bookedDates.startDate AND c.bookedDates.endDate " +
            "OR :startDate < c.bookedDates.startDate AND :endDate > c.bookedDates.endDate ")
    List<BookedRoom> findAllBookedRoomsByDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    List<BookedRoom> findAllByUser(User user);
}
