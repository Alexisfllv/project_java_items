package corporation.proyect.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ItemRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name should not exceed 100 characters")
        String name,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity should be greater than or equal to 0")
        Integer quantity,

        @NotNull(message = "Active status is required")
        Boolean active

){}