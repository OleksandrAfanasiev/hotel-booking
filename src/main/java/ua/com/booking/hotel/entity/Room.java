package ua.com.booking.hotel.entity;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ua.com.booking.hotel.entity.core.RoomCategoryType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private long roomNumber;

    @Column
    @Enumerated(EnumType.STRING)
    @NonNull
    private RoomCategoryType roomCategory;

    @Column
    private long price;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "rooms_additional_options",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "additional_option_id")}
    )
    private Set<AdditionalOption> additionalOptions = new HashSet<>();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id:", this.id)
                .add("Room Number:", this.roomNumber)
                .add("Price:", this.price)
                .omitNullValues()
                .toString();
    }
}
