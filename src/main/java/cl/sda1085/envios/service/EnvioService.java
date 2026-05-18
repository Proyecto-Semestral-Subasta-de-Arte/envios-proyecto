package cl.sda1085.envios.service;

import cl.sda1085.envios.dto.EnvioRequestDTO;
import cl.sda1085.envios.dto.EnvioResponseDTO;
import cl.sda1085.envios.model.Envio;
import cl.sda1085.envios.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  //Por defecto, todos los métodos son solo de lectura
public class EnvioService {

    //Conexion con 'repository'
    private final EnvioRepository envioRepository;

    //Método de apoyo para encapsulamiento de datos
    private EnvioResponseDTO mapToResponseDTO(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .idSubasta(envio.getIdSubasta())
                .direccion(envio.getDireccion())
                .estadoEnvio(envio.getEstadoEnvio())
                .codigoSeguimiento(envio.getCodigoSeguimiento())
                .build();
    }


    //------------------------------
    //CRUD estándar
    //------------------------------

    //Obtener todos los envíos
    public List<EnvioResponseDTO> obtenerTodos() {
        log.info("Consultando todos los envíos registrados");
        return envioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Obtener envío por ID
    public Optional<EnvioResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando envío con ID: {}", id);
        return envioRepository.findById(id).map(this::mapToResponseDTO);
    }

    //Crear (guardar) nuevo envío
    @Transactional  //Permite escritura en la BD
    public EnvioResponseDTO guardar(EnvioRequestDTO dto) {
        if (envioRepository.existsByIdSubasta(dto.getIdSubasta())) {
            log.error("Error: Ya existe un envío para la subasta ID: {}", dto.getIdSubasta());
            throw new RuntimeException("Ya existe un envío programado para esta subasta.");
        }

        Envio envio = new Envio();
        envio.setIdSubasta(dto.getIdSubasta());
        envio.setDireccion(dto.getDireccion());
        envio.setEstadoEnvio("PENDIENTE");

        String codigoGenerado = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        envio.setCodigoSeguimiento(codigoGenerado);

        Envio envioGuardado = envioRepository.save(envio);
        log.info("Nuevo envío creado exitosamente - ID: {}, Seguimiento: {}", envioGuardado.getId(), codigoGenerado);

        return mapToResponseDTO(envioGuardado);
    }

    //Actualizar envío existente
    @Transactional
    public Optional<EnvioResponseDTO> actualizar(Long id, EnvioRequestDTO dto) {
        log.info("Actualizando envío ID: {}", id);
        return envioRepository.findById(id).map(envioExistente -> {
            envioExistente.setIdSubasta(dto.getIdSubasta());
            envioExistente.setDireccion(dto.getDireccion());
            envioExistente.setEstadoEnvio(dto.getEstadoEnvio());
            return mapToResponseDTO(envioRepository.save(envioExistente));
        });
    }

    //Eliminar envío
    @Transactional
    public void eliminar(Long id){
        log.warn("Eliminando envío ID: {}", id);
        envioRepository.deleteById(id);
    }


    //------------------------------
    //CRUD personalizado
    //------------------------------

    //Buscar por código de seguimiento
    public Optional<EnvioResponseDTO> buscarPorCodigo(String codigo){
        log.info("Buscando envío por código de seguimiento: {}", codigo);
        return envioRepository.findByCodigoSeguimiento(codigo)
                .map(this::mapToResponseDTO);
    }

    //Buscar envío de una subasta específica
    public Optional<EnvioResponseDTO> buscarPorSubasta(Long idSubasta){
        return envioRepository.findByIdSubasta(idSubasta)
                .map(this::mapToResponseDTO);
    }

    //Filtrar por estado
    public List<EnvioResponseDTO> buscarPorEstado(String estado){
        log.info("Filtrando envíos por estado: {}", estado);
        return envioRepository.findByEstadoEnvio(estado).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Verificar si existe el envío
    public boolean existeEnvioParaSubasta(Long idSubasta){
        return envioRepository.existsByIdSubasta(idSubasta);
    }

    //Búsqueda flexible por dirección
    public List<EnvioResponseDTO> buscarPorDireccion(String direccion){
        return envioRepository.findByDireccionContainingIgnoreCase(direccion)
                .stream().map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //Contar cuántos envíos hay en un estado particular
    public long contarPorEstado(String estado) {
        log.info("Contando envíos con estado: {}", estado);
        return envioRepository.countByEstadoEnvio(estado);
    }
}
