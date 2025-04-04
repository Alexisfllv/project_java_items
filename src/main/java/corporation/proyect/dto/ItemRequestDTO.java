package corporation.proyect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;


public record ItemRequestDTO(

        @Schema(description = "Name of the item", example = "Laptop")

        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name should not exceed 100 characters")
        String name,

        @Schema(description = "Quantity available", example = "10")
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity should be greater than or equal to 0")
        Integer quantity,

        @Schema(description = "Whether the item is active", example = "true")
        @NotNull(message = "Active status is required")
        Boolean active

){}