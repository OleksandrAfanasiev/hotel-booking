package ua.com.booking.hotel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.booking.hotel.entity.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRoomDto {

    private long roomId;

    private long userId;

    private Period period;
}
