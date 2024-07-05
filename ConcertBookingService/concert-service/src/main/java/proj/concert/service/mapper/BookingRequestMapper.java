package proj.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

import proj.concert.service.domain.Booking;
import proj.concert.common.dto.BookingRequestDTO;

/**
 * Mapper class for converting between Booking objects and BookingDTO
 * objects.
 */
public class BookingRequestMapper {

    public static Booking toDomain(BookingRequestDTO c) {
        return new Booking(c.getConcertId(), c.getDate(), SeatMapper.stringToSeat(c.getSeatLabels()));
    }

    public static List<Booking> listToDomain(List<BookingRequestDTO> bookings) {
        List<Booking> domainList = new ArrayList<>();
        bookings.forEach(p -> domainList.add(BookingRequestMapper.toDomain(p)));
        return domainList;
    }
}
