package corporation.proyect.mapper;

import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.entity.ItemDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IItemDetailMapper {

    ItemDetailRequestDTO toItemDetailRequestDTO(ItemDetail itemDetail);

    @Mapping(target = "item.id", source = "itemId")
    ItemDetail toItemDetail(ItemDetailRequestDTO itemDetailRequestDTO);

    // response
    @Mapping(target = "responseDTO", source = "item")
    ItemDetailResponseDTO toItemDetailResponseDTO(ItemDetail itemDetail);

    ItemDetail toItemDetail(ItemDetail itemDetail);

}
