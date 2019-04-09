package ua.com.booking.hotel.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.booking.hotel.entity.Room;
import ua.com.booking.hotel.entity.core.RoomCategoryType;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ITRoomRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoomRepository roomRepository;

    private Room room1;

    private Room room2;

    @Before
    public void setup() {
        room1 = new Room(0, 601, RoomCategoryType.SINGLE, 100, new HashSet<>());
        room2 = new Room(0, 602, RoomCategoryType.SINGLE, 200, new HashSet<>());
        Room room3 = new Room(0, 603, RoomCategoryType.LUX, 300, new HashSet<>());

        entityManager.persistAndFlush(room1);
        entityManager.persistAndFlush(room2);
        entityManager.persistAndFlush(room3);

    }

    @Test
    public void whenFindAllByRoomCategoryThenReturnList() {
        List<Room> rooms = roomRepository.findAllByRoomCategory(RoomCategoryType.SINGLE);

        //include 5 rooms from default data
        assertThat(rooms, hasSize(7));
        assertThat(rooms, hasItems(room1, room2));
    }
}
