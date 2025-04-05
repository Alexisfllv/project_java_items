package corporation.proyect.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ItemDTO(
        Integer id,
        String name,
        Integer queantity,
        Boolean active,
        LocalDateTime CRATED,
        UUID uuid
) {
}
