## Tabla de Contenidos
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Componentes Principales](#componentes-principales)
  - [Entidades (Entity)](#entidades-entity)
  - [DTOs (Data Transfer Objects)](#dtos-data-transfer-objects)
  - [Mappers](#mappers)
  - [Repositorios](#repositorios)
  - [Excepciones](#excepciones)
  - [Respuestas Estandarizadas](#respuestas-estandarizadas)
  - [Paginación](#paginación)
  - [Servicios](#servicios)
  - [Controladores](#controladores)
  - [Configuración](#configuración)
- [Flujo de Datos](#flujo-de-datos)
- [Diagrama de Arquitectura](#diagrama-de-arquitectura)

## Estructura del Proyecto

``` bash
src/main/java/com/tuempresa/proyecto
│── MainApplication.java  # Clase principal de Spring Boot
│── config/           # Configuraciones (CORS, seguridad, etc.)
│── controller/       # Controladores (APIs REST)
│── dto/              # Clases DTO (Data Transfer Object)
│── entity/           # Entidades JPA (Modelos de base de datos)
│── repository/       # Repositorios JPA (Interacción con BD)
│── service/          # Lógica de negocio (Servicios)
│├── impl/           # Implementaciones de los servicios
│── exception/        # Manejo de excepciones personalizadas
│── util/             # Clases utilitarias (helpers)
│── security/         # Configuración de seguridad (JWT, autenticación)
│── mapper/           # Mapstruct u otros mapeadores de DTOs
│── enums/            # Enumeradores (si es necesario)
│── response/         # Clases para respuestas estándar
│── pagination/       # Clases para manejar paginación
```

El proyecto sigue una arquitectura multicapa bien organizada que separa claramente las responsabilidades.
## Componentes Principales

### ENTITY)

Las entidades JPA representan los modelos de datos que se mapean directamente a tablas en la base de datos.
**Item Entity:**
``` bash
src/main/java/com/corporation/proyect
│── entity/           # Entidades JPA (Modelos de base de datos)
```


``` java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;

    @Column(name = "item_name", nullable = false, length = 100)
    private String name;  // VARCHAR(100) en BD

    @Column(name = "item_quantity", nullable = false)
    private Integer quantity;  // INT en BD

    @Column(name = "item_active", nullable = false)
    private Boolean active;  // BOOLEAN en BD

    @Column(name = "item_created", nullable = false)
    private LocalDateTime created;  // DATETIME en BD

    @Column(name = "item_uuid", nullable = true, unique = true)
    private UUID uuid = UUID.randomUUID();   // UUID en BD
}
```
### DTOs (Data Transfer Objects)

Los DTOs son utilizados para transferir datos entre capas, especialmente entre el controlador y el cliente.

**Request DTO:**

``` java
public record ItemRequestDTO(

        @Schema(description = "Name of the item", example = "Laptop")

        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name should not exceed 100 characters")
        String name,

        @Schema(description = "Quantity available", example = "10")
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity should be greater than or equal to 0")
        Integer quantity,

        @Schema(description = "Whether the item is active", example = "true")
        @NotNull(message = "Active status is required")
        Boolean active

){}

```
**Response DTO:**
``` java
public record ItemResponseDTO(
    @Schema(description = "ID del ítem", example = "1", required = true)
    Integer id,

    @Schema(description = "Nombre del ítem", example = "Item ejemplo", required = true)
    String name,

    @Schema(description = "Cantidad del ítem", example = "10", required = true)
    Integer quantity,

    @Schema(description = "Estado activo del ítem", example = "true", required = true)
    Boolean active,

    @Schema(description = "Fecha de creación del ítem", example = "2023-04-02T15:30:00", required = true)
    LocalDateTime created,

    @Schema(description = "UUID del ítem", example = "dcb41d8f-c467-4f89-a90f-9e687db3edc3", required = true)
    UUID uuid
) {}
```

### MAPPER

Los mappers facilitan la conversión entre entidades y DTOs:

``` java
@Mapper(componentModel = "spring")
public interface IItemMapper {

    ItemResponseDTO toItemResponseDTO(Item item);
    Item toItem(ItemResponseDTO itemResponseDTO);

    ItemRequestDTO toItemRequestDTO(Item item);
    Item toItem(ItemRequestDTO itemRequestDTO);
}
```
### REPOSITORY

Interfaces que extienden JpaRepository para operaciones CRUD básicas:
``` java
@Repository
public interface IItemRepository extends JpaRepository<Item, Integer> {
}
```
### EXCEPTION

Manejo centralizado de excepciones con clases específicas para diferentes tipos de errores:
``` java
public class ExAccesoNoAutorizadoException extends RuntimeException {
    // 401 Unauthorized 403 Forbidden
    public ExAccesoNoAutorizadoException(String message) {
        super(message);
    }
}
```
``` java
public class ExDatabaseErrorException extends RuntimeException {
    public ExDatabaseErrorException(String message) {
        super(message);
    }
}
```
``` java
public class ExDataNotFoundException extends RuntimeException {
    public ExDataNotFoundException(String message) {
        super(message);
    }
}
```
``` java
public class ExIncorrectJsonException extends RuntimeException {
    public ExIncorrectJsonException(String message) {
        super(message);
    }
}
```
``` java
public class ExInvalidDataException extends RuntimeException {
    //400 Bad Request
    public ExInvalidDataException(String message) {
        super(message);
    }
}
```

``` java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Constantes para keys de respuesta
    private static final String MESSAGE = "message";
    private static final String CODE = "code";
    private static final String ERRORS = "errors";

    // ---- Manejo de excepciones personalizadas ----
    @ExceptionHandler(ExAccesoNoAutorizadoException.class)
    public ResponseEntity<Map<String, Object>> handleAccesoNoAutorizado(ExAccesoNoAutorizadoException ex) {
        HttpStatus status = ex.getMessage().contains("Forbidden") ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        return buildResponse(ex.getMessage(), status);
    }

    @ExceptionHandler(ExDataNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDataNotFound(ExDataNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExInvalidDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidData(ExInvalidDataException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExIncorrectJsonException.class)
    public ResponseEntity<Map<String, Object>> handleIncorrectJson(ExIncorrectJsonException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExDatabaseErrorException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseError(ExDatabaseErrorException ex) {
        return buildResponse("Database error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ---- Manejo de excepciones de Spring ----
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        return buildResponse("Invalid JSON format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Invalid value"
                ));

        Map<String, Object> response = new HashMap<>();
        response.put(MESSAGE, "Validation error");
        response.put(CODE, HttpStatus.BAD_REQUEST.value());
        response.put(ERRORS, errors);
        return ResponseEntity.badRequest().body(response);
    }

    // ---- Último recurso (Error genérico) ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Metodo helper para respuestas consistentes
    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put(MESSAGE, message);
        response.put(CODE, status.value());
        return ResponseEntity.status(status).body(response);
    }
}
```

### RESPONSES

Formatos de respuesta consistentes:
``` java
public record ResponseDTO(
        String mensaje,
        Object data) {
}
```
``` java
@Getter
public enum ResponseMessage {
    SUCCESSFUL_ADDITION("Added successfully"),
    SUCCESSFUL_MODIFICATION("Modification completed successfully"),
    SUCCESSFUL_DELETION("Deletion completed successfully");
    
    private final String message;
    // Private constructor
    ResponseMessage(String message) {
        this.message = message;
    }
}
```


### PAGINATION

Soporte para resultados paginados:

``` java
public record PageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean isLast,
        boolean isFirst,
        int numberOfElements
) {
    public PageResponseDTO(Page<T> page) {
        this(page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst(),
                page.getNumberOfElements());
    }
}
```

### SERVICE

Implementación de la lógica de negocio:

``` java
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
```
### SERVICE/IMPL
``` bash
src/main/java/com/corporation/proyect
│── service/impl/     # Implementaciones de los servicios
```
``` java
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements IItemService {
    // ioc
    // repo
    private final IItemRepository itemRepository;
    // mapper
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

```
### CONTROLLER

Puntos de entrada REST:
``` java
@@Tag(name = "Items", description = "API para gestionar Items")
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @Operation(summary = "Listar todos los items")
    @GetMapping("/all")
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @Operation(summary = "Listar items con paginación")
    @GetMapping("/page")
    public ResponseEntity<PageResponseDTO<ItemResponseDTO>> pageItems(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(itemService.getAllItemsPageable(pageable));
    }

    @Operation(summary = "Obtener un item por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @Operation(summary = "Crear un nuevo item")
    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(@Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        return ResponseEntity.status(201).body(itemService.createItem(itemRequestDTO));
    }

    @Operation(summary = "Crear un nuevo item con respuesta personalizada")
    @PostMapping("/response")
    public ResponseEntity<ResponseDTO> createItemResponse(@Valid @RequestBody ItemRequestDTO itemRequestDTO) {
        return ResponseEntity.status(201).body(itemService.createItemResponse(itemRequestDTO));
    }

    @Operation(summary = "Actualizar un item existente")
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@Valid  @RequestBody ItemRequestDTO itemRequestDTO,@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.updateItem(itemRequestDTO, id));
    }

    @Operation(summary = "Actualizar un item con respuesta personalizada")
    @PutMapping("/response/{id}")
    public ResponseEntity<ResponseDTO> updateItemResponse(@Valid  @RequestBody ItemRequestDTO itemRequestDTO,@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.updateItemResponse(itemRequestDTO, id));
    }

    @Operation(summary = "Eliminar un item por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un item con respuesta personalizada")
    @DeleteMapping("/response/{id}")
    public ResponseEntity<ResponseDTO> deleteItemResponse(@PathVariable Integer id) {
        return ResponseEntity.ok(itemService.deleteItemResponse(id));
    }
}

```

### CONFIGURATION

Configuración OpenAPI/Swagger:

``` java
@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Items")
                        .version("1.0")
                        .description("Documentación de la API para la gestión de items"));
    }
}
```