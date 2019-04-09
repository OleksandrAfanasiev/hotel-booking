package ua.com.booking.hotel.entity;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "booked_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class BookedRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id", foreignKey = @ForeignKey(name = "FK_booked_rooms_rooms"))
    @NonNull
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_booked_rooms_users"))
    @NonNull
    private User user;

    @Column
    @NonNull
    private Period bookedDates;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id:", this.id)
                .add("Room:", this.room)
                .add("User:", this.user)
                .add("Booked Days:", this.bookedDates)
                .omitNullValues()
                .toString();
    }
}
