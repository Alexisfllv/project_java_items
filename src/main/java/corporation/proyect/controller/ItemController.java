package corporation.proyect.controller;

import corporation.proyect.dto.ItemRequestDTO;
import corporation.proyect.dto.ItemResponseDTO;
import corporation.proyect.pagination.PageResponseDTO;
import corporation.proyect.response.ResponseDTO;
import corporation.proyect.service.IItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Items", description = "API para gestionar Items")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @Operation(summary = "Listar todos los items")
    @GetMapping("/all")
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @Operation(summary = "Listar items con paginación")
    @GetMapping("/page")
    public ResponseEntity<PageResponseDTO<ItemResponseDTO>> pageItems(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(itemService.getAllItemsPageable(pageable));
    }

    @Operation(summary = "Obtener un item por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @Operation(summary = "Crear un nuevo item")
    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(@Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        return ResponseEntity.status(201).body(itemService.createItem(itemRequestDTO));
    }

    @Operation(summary = "Crear un nuevo item con respuesta personalizada")
    @PostMapping("/response")
    public ResponseEntity<ResponseDTO> createItemResponse(@Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        return ResponseEntity.status(201).body(itemService.createItemResponse(itemRequestDTO));
    }

    @Operation(summary = "Actualizar un item existente")
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@Valid  @RequestBody ItemRequestDTO itemRequestDTO,@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.updateItem(itemRequestDTO, id));
    }

    @Operation(summary = "Actualizar un item con respuesta personalizada")
    @PutMapping("/response/{id}")
    public ResponseEntity<ResponseDTO> updateItemResponse(@Valid  @RequestBody ItemRequestDTO itemRequestDTO,@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.updateItemResponse(itemRequestDTO, id));
    }

    @Operation(summary = "Eliminar un item por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un item con respuesta personalizada")
    @DeleteMapping("/response/{id}")
    public ResponseEntity<ResponseDTO> deleteItemResponse(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.deleteItemResponse(id));
    }
}
