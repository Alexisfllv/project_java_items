package corporation.proyect.service;

import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.dto.ItemRequestDTO;

import java.util.List;

public interface IItemDetailService {
    List<ItemDetailResponseDTO> listarItemDetail();
    ItemDetailResponseDTO buscarxid(Integer id);
    ItemDetailResponseDTO registrarItemDetail(ItemDetailRequestDTO itemDetailRequestDTO);
    ItemDetailResponseDTO actualizarItemDetail(ItemDetailRequestDTO itemDetailRequestDTO, Integer id);
    void eliminarItemDetail(Integer id);
}
