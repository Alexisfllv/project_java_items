package corporation.proyect.service.impl;

import corporation.proyect.dto.ItemDetailRequestDTO;
import corporation.proyect.dto.ItemDetailResponseDTO;
import corporation.proyect.entity.Item;
import corporation.proyect.entity.ItemDetail;
import corporation.proyect.exception.Errors.ExDataNotFoundException;
import corporation.proyect.mapper.IItemDetailMapper;
import corporation.proyect.repository.IItemDetailRepository;
import corporation.proyect.repository.IItemRepository;
import corporation.proyect.service.IItemDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemDetailServiceImpl implements IItemDetailService {

    //repos
    private final IItemDetailRepository itemDetailRepository;
    private final IItemRepository itemRepository;

    //mapper
    private final IItemDetailMapper iItemDetailMapper;


    @Override
    public ItemDetailResponseDTO registrarItemDetail(ItemDetailRequestDTO itemDetailRequestDTO) {

        //
        Item item = itemRepository.findById(itemDetailRequestDTO.itemId())
                .orElseThrow(() -> new ExDataNotFoundException(" id de detail no encontado :" + itemDetailRequestDTO.itemId()));


        log.debug("mapeo de los datos request al modelo");
        ItemDetail itemDetail = iItemDetailMapper.toItemDetail(itemDetailRequestDTO);

        itemDetail.setDescription(itemDetailRequestDTO.description());
        itemDetail.setQuantity(itemDetailRequestDTO.quantity());
        itemDetail.setItem(item);
        log.debug("setear datos a itemDetailRequestDTO");
        //
        itemDetailRepository.save(itemDetail);
        return iItemDetailMapper.toItemDetailResponseDTO(itemDetail);

    }
}
