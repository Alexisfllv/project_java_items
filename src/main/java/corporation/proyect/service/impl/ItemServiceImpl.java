package corporation.proyect.service.impl;

import corporation.proyect.dto.ItemRequestDTO;
import corporation.proyect.dto.ItemResponseDTO;
import corporation.proyect.entity.Item;
import corporation.proyect.exception.Errors.ExDataNotFoundException;
import corporation.proyect.exception.Errors.ExInvalidDataException;
import corporation.proyect.mapper.IItemMapper;
import corporation.proyect.pagination.PageResponseDTO;
import corporation.proyect.repository.IItemRepository;
import corporation.proyect.response.ResponseDTO;
import corporation.proyect.response.ResponseMessage;
import corporation.proyect.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements IItemService {


    //ioc
    //repo
    private final IItemRepository itemRepository;
    //mapper
    private final IItemMapper itemMapper;


    @Override
    public List<ItemResponseDTO> getAllItems() {
        log.info("Iniciando la obtención de todos los items.");
        List<Item> items= itemRepository.findAll();
        log.debug("Se obtuvieron {} items desde la base de datos.", items.size());

        List<ItemResponseDTO> respose = items
                .stream()
                .map(item -> itemMapper.toItemResponseDTO(item))
                .toList(); //inmutable
        log.debug("Se transformaron los items a DTOs correctamente.");
        log.info("Finalizó la obtención y transformación de items.");
        return respose;
    }

    @Override
    public PageResponseDTO<ItemResponseDTO> getAllItemsPageable(Pageable pageable) {
        log.info("Iniciar obtencion de los items en Page paged.");
        Page<ItemResponseDTO> paged = itemRepository.findAll(pageable)
                .map(item -> itemMapper.toItemResponseDTO(item));
        log.debug("Trasnformacion de listado a Pageable.");
        log.debug("Mapeo de cursos");

        log.debug("Nuevo obj de Respuesta dentro de Pageable.");
        log.info("Finalizando el listado de Items en Paginados.");
        return new PageResponseDTO<>(paged);
    }

    @Override
    public ItemResponseDTO getItemById(Integer id) {

        log.info("Iniciar busqueda y validacion de id.");
        Item item = itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));

        log.debug("Recuperar el id : {} buscado y validado.", item.getId());
        log.debug("Mapear modelo a itemResponseDTO.");
        log.info("Terminar con mapeo y retorno.");
        return itemMapper.toItemResponseDTO(item);
    }

    @Override
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO) {

        log.info("Iniciar Registro de Item RequestDTO con Response.");
        log.debug("Maper el request a modelo Item.");
        Item item = itemMapper.toItem(itemRequestDTO);

        log.debug("Enviar datos al modelo item los valores recuperados de itemRequestDTO.");
        item.setName(itemRequestDTO.name());
        item.setQuantity(itemRequestDTO.quantity());
        item.setActive(itemRequestDTO.active());
        item.setCreated(LocalDateTime.now());

        log.debug("Guardar datos seteados.");
        itemRepository.save(item);
        log.debug("Mapear del modelo a un itemResponseDTO.");
        log.info("Terminar con mapeo y retorno.");
        return itemMapper.toItemResponseDTO(item);
    }

    @Override
    public ResponseDTO createItemResponse(ItemRequestDTO itemRequestDTO) {
        log.info("Iniciar Registro de Item RequestDTO.");
        log.debug("Maper el request a modelo Item.");
        Item item = itemMapper.toItem(itemRequestDTO);

        log.debug("Enviar datos al modelo item los valores recuperados de itemRequestDTO.");
        item.setName(itemRequestDTO.name());
        item.setQuantity(itemRequestDTO.quantity());
        item.setActive(itemRequestDTO.active());
        item.setCreated(LocalDateTime.now());

        log.debug("Guardar datos seteados.");
        itemRepository.save(item);

        log.debug("Mapear del modelo a un itemResponseDTO.");
        ItemResponseDTO itemResponseDTO =  itemMapper.toItemResponseDTO(item);
        log.debug("Envolver la itemResponseDTO en un ResponseDTO.");
        log.info("Terminar con retorno con ResponseDTO y Mensaje Registrado.");
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_ADDITION.getMessage(), itemResponseDTO);
    }

    @Override
    public ItemResponseDTO updateItem(ItemRequestDTO itemRequestDTO, Integer id) {

        log.info("Iniciar Modificacion de Item RequestDTO con Response.");
        log.debug("Recupear el item y validar por id.");
        Item itemRecovered = itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));

        log.debug("Enviar datos de itemRequestDTO al modelo itemRecovered .");
        itemRecovered.setName(itemRequestDTO.name());
        itemRecovered.setQuantity(itemRequestDTO.quantity());
        itemRecovered.setActive(itemRequestDTO.active());

        log.debug("Guardar datos seteados.");
        itemRepository.save(itemRecovered);
        log.debug("Mapear del modelo a un itemResponseDTO.");
        log.info("Temino la modificacion del itemRequestDTO.");
        return itemMapper.toItemResponseDTO(itemRecovered);
    }

    @Override
    public ResponseDTO updateItemResponse(ItemRequestDTO itemRequestDTO, Integer id) {

        log.info("Iniciar Modificacion de Item RequestDTO.");
        log.debug("Recupear el item y validar por id.");
        Item itemRecovered = itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));

        log.debug("Enviar datos de itemRequestDTO al modelo itemRecovered .");
        itemRecovered.setName(itemRequestDTO.name());
        itemRecovered.setQuantity(itemRequestDTO.quantity());
        itemRecovered.setActive(itemRequestDTO.active());

        log.debug("Guardar datos seteados.");
        itemRepository.save(itemRecovered);
        log.debug("Mapear del modelo a un itemResponseDTO.");
        ItemResponseDTO itemResponseDTO =  itemMapper.toItemResponseDTO(itemRecovered);
        log.debug("Envolver la itemResponseDTO en un ResponseDTO.");
        log.info("Terminar con retorno con ResponseDTO y Mensaje Modificado.");
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_MODIFICATION.getMessage(), itemResponseDTO);
    }

    @Override
    public void deleteItem(Integer id) {

        log.info("Iniciar Eliminacion de Modelo Item.");
        log.debug("Validar id existente.");
        itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));
        log.debug("Eliminar el item por id.");
        log.info("Termina con Eliminacion de Modelo Item.");
        itemRepository.deleteById(id);
    }

    @Override
    public ResponseDTO deleteItemResponse(Integer id) {
        log.info("Iniciar Eliminacion de Modelo Item con Response.");
        log.debug("Validar id existente.");
        itemRepository.findById(id)
                .orElseThrow(()-> new ExDataNotFoundException("id item not found: "+id));
        log.debug("Eliminar el item por id.");
        itemRepository.deleteById(id);

        log.debug("Envolver en ResponseDTO y Mensaje Eliminacion de Modelo Item.");
        log.info("Terminar con Eliminacion de Modelo Item con Response.");
        return new ResponseDTO(ResponseMessage.SUCCESSFUL_DELETION.getMessage(), "Item deleted is id :"+id);
    }
}
