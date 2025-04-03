package corporation.proyect.service;

import corporation.proyect.dto.ItemRequestDTO;
import corporation.proyect.dto.ItemResponseDTO;
import corporation.proyect.pagination.PageResponseDTO;
import corporation.proyect.response.ResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IItemService {

    // List all
    List<ItemResponseDTO> getAllItems();

    // List all Pageable
    PageResponseDTO<ItemResponseDTO> getAllItemsPageable(Pageable pageable);

    // Get by ID
    ItemResponseDTO getItemById(Integer id);

    // Create
    ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO);

    // Create Response
    ResponseDTO createItemResponse(ItemRequestDTO itemRequestDTO);

    // Update
    ItemResponseDTO updateItem(ItemRequestDTO itemRequestDTO, Integer id);

    // Update Response
    ResponseDTO updateItemResponse(ItemRequestDTO itemRequestDTO, Integer id);

    // Delete
    void deleteItem(Integer id);

    // Delete Response
    ResponseDTO deleteItemResponse(Integer id);
}
