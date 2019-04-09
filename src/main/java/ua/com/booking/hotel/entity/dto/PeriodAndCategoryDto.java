package ua.com.booking.hotel.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.booking.hotel.entity.Period;
import ua.com.booking.hotel.entity.core.RoomCategoryType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodAndCategoryDto {

    private Period period;

    private RoomCategoryType categoryType;
}
