package corporation.proyect.mapper;

import corporation.proyect.dto.ItemRequestDTO;
import corporation.proyect.dto.ItemResponseDTO;
import corporation.proyect.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IItemMapper {

    ItemResponseDTO toItemResponseDTO(Item item);
    Item toItem(ItemResponseDTO itemResponseDTO);

    ItemRequestDTO toItemRequestDTO(Item item);
    Item toItem(ItemRequestDTO itemRequestDTO);
}