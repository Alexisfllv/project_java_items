package corporation.proyect.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ItemResponseDTO(
        Integer id,
        String name,
        Integer quantity,
        Boolean active,
        LocalDateTime created,
        UUID uuid
){}
