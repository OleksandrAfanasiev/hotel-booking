package ua.com.booking.hotel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Period {

    @Column(name = "start_date")
    @NonNull
    private Timestamp startDate;

    @Column(name = "end_date")
    @NonNull
    private Timestamp endDate;
}
