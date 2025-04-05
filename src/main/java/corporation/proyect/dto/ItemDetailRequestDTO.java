package corporation.proyect.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ItemDetailRequestDTO(
        String description,
        Integer quantity,
        Integer itemId
) {
}
