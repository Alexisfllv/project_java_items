package corporation.proyect.controller;


import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.service.IItemDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/details")
@RequiredArgsConstructor
public class ItemDetailController {


    //service
    private final IItemDetailService itemDetailService;

    @PostMapping
    public ResponseEntity<ItemDetailResponseDTO> createItemDetail(@RequestBody ItemDetailRequestDTO itemDetailRequestDTO) {
        return ResponseEntity.status(200).body(itemDetailService.registrarItemDetail(itemDetailRequestDTO));
    }



}
