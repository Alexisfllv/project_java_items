package corporation.proyect.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ItemDetailResponseDTO(
        Integer id,
        String description,
        Integer quantity,
        ItemResponseDTO responseDTO
) {
}
