package ua.com.booking.hotel.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.booking.hotel.entity.BookedRoom;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.User;
import ua.com.booking.hotel.entity.core.RoomCategoryType;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ITBookedRoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    private BookedRoom bookedRoom1;

    private BookedRoom bookedRoom2;

    private BookedRoom bookedRoom3;

    private User user1;

    @Before
    public void setup() {
        Room room = new Room(0, 601, RoomCategoryType.SINGLE, 100, new HashSet<>());

        user1 = new User(0, "FirstUser");
        User user2 = new User(0, "SecondUser");

        Period period1 = new Period(Timestamp.valueOf("2016-10-10 0:0:0.0"), Timestamp.valueOf("2016-11-11 0:0:0.0"));
        Period period2 = new Period(Timestamp.valueOf("2016-08-12 0:0:0.0"), Timestamp.valueOf("2016-09-21 0:0:0.0"));
        Period period3 = new Period(Timestamp.valueOf("2016-02-16 0:0:0.0"), Timestamp.valueOf("2016-03-25 0:0:0.0"));

        bookedRoom1 = new BookedRoom(0, room, user1, period1);
        bookedRoom2 = new BookedRoom(0, room, user2, period2);
        bookedRoom3 = new BookedRoom(0, room, user1, period3);

        entityManager.persist(room);

        entityManager.persist(user1);
        entityManager.persist(user2);

        entityManager.persistAndFlush(bookedRoom1);
        entityManager.persistAndFlush(bookedRoom2);
        entityManager.persistAndFlush(bookedRoom3);
    }

    @Test
    public void whenFindAllByUserThenReturnList() {
        List<BookedRoom> bookedRooms = bookedRoomRepository.findAllByUser(user1);

        assertThat(bookedRooms, hasSize(2));
        assertThat(bookedRooms, hasItems(bookedRoom1, bookedRoom3));
    }

    @Test
    public void whenFindAllBookedRoomsByDatesThenReturnList() {
        List<BookedRoom> bookedRooms = bookedRoomRepository
                .findAllBookedRoomsByDates(Timestamp.valueOf("2016-09-21 0:0:0.0"), Timestamp.valueOf("2016-11-20 0:0:0.0"));

        assertThat(bookedRooms, hasSize(2));
        assertThat(bookedRooms, hasItems(bookedRoom1, bookedRoom2));
    }
}
