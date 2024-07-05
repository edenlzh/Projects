package proj.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Seat;

public class SeatMapper {
    public static Seat toSeat(String c){
        Seat seat = new Seat();
        seat.setLabel(c);
        return seat;
    }

    public static List<SeatDTO> toDTOList(List<Seat> seats){
        List<SeatDTO> seatList = new ArrayList<>();
        seats.forEach(p -> seatList.add(SeatMapper.toDTO(p)));
        return seatList;
    }

    public static SeatDTO toDTO(Seat s) {
        return new SeatDTO(s.getLabel(), s.getCost());
    }

    public static ArrayList<Seat> stringToSeat(List<String> seats){
        ArrayList<Seat> seatString = new ArrayList<>();
        seats.forEach(seat -> seatString.add(SeatMapper.toSeat(seat)));
        return seatString;
    }
}
