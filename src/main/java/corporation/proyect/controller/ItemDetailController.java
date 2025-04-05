package corporation.proyect.controller;


import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.service.IItemDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/details")
@RequiredArgsConstructor
public class ItemDetailController {


    //service
    private final IItemDetailService itemDetailService;

    //listas

    @GetMapping
    public ResponseEntity<List<ItemDetailResponseDTO>> findAll() {
        return ResponseEntity.ok(itemDetailService.listarItemDetail());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(itemDetailService.buscarxid(id));
    }


    @PostMapping
    public ResponseEntity<ItemDetailResponseDTO> createItemDetail(@RequestBody ItemDetailRequestDTO itemDetailRequestDTO) {
        return ResponseEntity.status(200).body(itemDetailService.registrarItemDetail(itemDetailRequestDTO));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ItemDetailResponseDTO> modificar (@PathVariable Integer id, @RequestBody ItemDetailRequestDTO itemDetailRequestDTO) {
        return ResponseEntity.ok(itemDetailService.actualizarItemDetail(itemDetailRequestDTO,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        itemDetailService.eliminarItemDetail(id);
        return ResponseEntity.noContent().build();
    }
}
