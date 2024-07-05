
package proj.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.service.domain.Concert;

/**
 * ConcertMapper class is a mapper for converting Concert domain object to ConcertDTO object.
 *  @author @curdledSoy
 */
public class ConcertMapper {
    /**
     * Converts the given Concert object to its corresponding ConcertDTO object.
     * 
     * @param c The Concert object to be converted.
     * @return The ConcertDTO object created from the given Concert object.
     *  @author @curdledSoy
     */
    public static ConcertDTO toDTO(Concert c) {
        ConcertDTO dto = new ConcertDTO(c.getId(), c.getTitle(), c.getImageName(), c.getBlurb());
        // Iterate through the list of performers and add them to the ConcertDTO object
        c.getPerformers()
                .forEach(performer -> dto.getPerformers()
                        .add(PerformerMapper.toDTO(performer)));
        // Add all the dates to the ConcertDTO object
        dto.getDates()
                .addAll(c.getDates());
        return dto;
    }
    /**
     * Converts the given list of Concert objects to their corresponding ConcertDTO objects.
     * 
     * @param concerts The list of Concert objects to be converted.
     * @return The list of ConcertDTO objects created from the given list of Concert objects.
     */
    public static List<ConcertDTO> toDTOList(List<Concert> concerts) {
        List<ConcertDTO> dtoList = new ArrayList<ConcertDTO>();
        // Iterate through the list of Concerts and convert each one to its corresponding ConcertDTO object
        concerts.forEach(concert -> dtoList.add(toDTO(concert)));
        return dtoList;

    }

    public static ConcertSummaryDTO toSummaryDTO(Concert c){
        ConcertSummaryDTO dto = new ConcertSummaryDTO(c.getId(), c.getTitle(), c.getImageName());
        return dto;
    }



    /**
     * Converts the given list of Concert objects to their corresponding ConcertSummaryDTO objects.
     * 
     * @param concerts The list of Concert objects to be converted.
     * @return The list of ConcertSummaryDTO objects created from the given list of Concert objects.
     */
    public static List<ConcertSummaryDTO> toSummaryDTOList(List<Concert> concerts) {
        List<ConcertSummaryDTO> dtoList = new ArrayList<ConcertSummaryDTO>();
        // Iterate through the list of Concerts and convert each one to its corresponding ConcertDTO object
        concerts.forEach(concert -> dtoList.add(toSummaryDTO(concert)));
        return dtoList;

    }

}