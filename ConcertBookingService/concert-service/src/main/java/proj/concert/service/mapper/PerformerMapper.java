package proj.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Performer;

/**
 * Mapper class for converting between Performer objects and PerformerDTO
 * objects.
 */
public class PerformerMapper {

    public static PerformerDTO toDTO(Performer c) {
        return new PerformerDTO(c.getId(), c.getName(), c.getImageName(), c.getGenre(), c.getBlurb());
    }

    public static List<PerformerDTO> toDTOList(List<Performer> performers) {
        List<PerformerDTO> dtoList = new ArrayList<>();
        performers.forEach(p -> dtoList.add(PerformerMapper.toDTO(p)));
        return dtoList;
    }
}