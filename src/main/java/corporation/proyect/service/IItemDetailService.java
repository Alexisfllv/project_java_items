package corporation.proyect.service;

import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.dto.ItemRequestDTO;

public interface IItemDetailService {
    //request

    ItemDetailResponseDTO registrarItemDetail(ItemDetailRequestDTO itemDetailRequestDTO);

}
