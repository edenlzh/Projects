package proj.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

import proj.concert.service.domain.Booking;
import proj.concert.common.dto.BookingDTO;
import proj.concert.common.dto.SeatDTO;

/**
 * Mapper class for converting between Booking objects and BookingDTO
 * objects.
 */
public class BookingMapper {

    public static BookingDTO toDTO(Booking b) {
        List<SeatDTO> seatDTOs = SeatMapper.toDTOList(b.getSeats());
        return new BookingDTO(b.getConcertId(), b.getDate(), seatDTOs);
    }

}
