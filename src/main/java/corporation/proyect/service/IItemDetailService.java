package corporation.proyect.service;

import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.dto.ItemRequestDTO;

import java.util.List;

public interface IItemDetailService {
    //request
    List<ItemDetailResponseDTO> listarItemDetail();
    //listado
    ItemDetailResponseDTO buscarxid(Integer id);
    //getid

    //create
    ItemDetailResponseDTO registrarItemDetail(ItemDetailRequestDTO itemDetailRequestDTO);

    //update
    ItemDetailResponseDTO actualizarItemDetail(ItemDetailRequestDTO itemDetailRequestDTO, Integer id);

    //delete

    void eliminarItemDetail(Integer id);
}
