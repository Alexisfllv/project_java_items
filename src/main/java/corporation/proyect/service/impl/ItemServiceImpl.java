package corporation.proyect.service.impl;

import corporation.proyect.dto.ItemRequestDTO;
import corporation.proyect.dto.ItemResponseDTO;
import corporation.proyect.entity.Item;
import corporation.proyect.exception.Errors.ExDataNotFoundException;
import corporation.proyect.mapper.IItemMapper;
import corporation.proyect.pagination.PageResponseDTO;
import corporation.proyect.repository.IItemRepository;
import corporation.proyect.response.ResponseDTO;
import corporation.proyect.response.ResponseMessage;
import corporation.proyect.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements IItemService {


    //ioc
    //repo
    private final IItemRepository itemRepository;
    //mapper
    private final IItemMapper itemMapper;


    @Override
    public List<ItemResponseDTO> getAllItems() {
        List<Item> items= itemRepository.findAll();
        return items.stream()
                .map(item -> itemMapper.toItemResponseDTO(item))
                .toList(); //inmutable
    }

    @Override
    public PageResponseDTO<ItemResponseDTO> getAllItemsPageable(Pageable pageable) {
        Page<ItemResponseDTO> paged = itemRepository.findAll(pageable)
                .map(item -> itemMapper.toItemResponseDTO(item));
        return new PageResponseDTO<>(paged);
    }

    @Override
    public ItemResponseDTO getItemById(Integer id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));
        return itemMapper.toItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO) {
        Item item = itemMapper.toItem(itemRequestDTO);

        item.setName(itemRequestDTO.name());
        item.setQuantity(itemRequestDTO.quantity());
        item.setActive(itemRequestDTO.active());
        item.setCreated(LocalDateTime.now());

        itemRepository.save(item);
        return itemMapper.toItemResponseDTO(item);
    }

    @Override
    public ResponseDTO createItemResponse(ItemRequestDTO itemRequestDTO) {
        Item item = itemMapper.toItem(itemRequestDTO);

        item.setName(itemRequestDTO.name());
        item.setQuantity(itemRequestDTO.quantity());
        item.setActive(itemRequestDTO.active());
        item.setCreated(LocalDateTime.now());

        itemRepository.save(item);

        ItemResponseDTO itemResponseDTO =  itemMapper.toItemResponseDTO(item);
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_ADDITION.getMessage(), itemResponseDTO);
    }

    @Override
    public ItemResponseDTO updateItem(ItemRequestDTO itemRequestDTO, Integer id) {
        Item itemRecovered = itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));

        itemRecovered.setName(itemRequestDTO.name());
        itemRecovered.setQuantity(itemRequestDTO.quantity());
        itemRecovered.setActive(itemRequestDTO.active());

        itemRepository.save(itemRecovered);
        return itemMapper.toItemResponseDTO(itemRecovered);
    }

    @Override
    public ResponseDTO updateItemResponse(ItemRequestDTO itemRequestDTO, Integer id) {
        Item itemRecovered = itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));

        itemRecovered.setName(itemRequestDTO.name());
        itemRecovered.setQuantity(itemRequestDTO.quantity());
        itemRecovered.setActive(itemRequestDTO.active());

        itemRepository.save(itemRecovered);
        ItemResponseDTO itemResponseDTO =  itemMapper.toItemResponseDTO(itemRecovered);
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_MODIFICATION.getMessage(), itemResponseDTO);
    }

    @Override
    public void deleteItem(Integer id) {
        itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));
        itemRepository.deleteById(id);
    }

    @Override
    public ResponseDTO deleteItemResponse(Integer id) {
        itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));
        itemRepository.deleteById(id);

        return new ResponseDTO(ResponseMessage.SUCCESSFUL_DELETION.getMessage(), "Item deleted is id :"+id);
    }
}
