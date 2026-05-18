package cl.sda1085.envios.controller;

import cl.sda1085.envios.dto.EnvioRequestDTO;
import cl.sda1085.envios.dto.EnvioResponseDTO;
import cl.sda1085.envios.service.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/envios")
@CrossOrigin(origins = "*")  //Permite conexión con el FrontEnd
public class EnvioController {

    //Conexión con 'service'
    private final EnvioService envioService;


    //------------------------------
    //CRUD estándar
    //------------------------------

    //Obtener todos los envíos
    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(envioService.obtenerTodos());
    }

    //Obtener envío por ID
    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return envioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Crear (guardar) nuevo envío
    @PostMapping
    public ResponseEntity<EnvioResponseDTO> guardar(@Valid @RequestBody EnvioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.guardar(dto));
    }

    //Actualizar envío
    @PutMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EnvioRequestDTO dto) {
        return envioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Eliminar envío
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    //------------------------------
    //CRUD personalizado
    //------------------------------

    //Buscar por código de seguimiento
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<EnvioResponseDTO> buscarPorCodigo(@PathVariable String codigo) {
        return envioService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Buscar envío de una subasta específica
    @GetMapping("/subasta/{idSubasta}")
    public ResponseEntity<EnvioResponseDTO> buscarPorSubasta(@PathVariable Long idSubasta) {
        return envioService.buscarPorSubasta(idSubasta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Filtrar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EnvioResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(envioService.buscarPorEstado(estado));
    }

    //Verificar si existe el envío
    @GetMapping("/subasta/{idSubasta}/existe")
    public ResponseEntity<Boolean> existeEnvioParaSubasta(@PathVariable Long idSubasta) {
        return ResponseEntity.ok(envioService.existeEnvioParaSubasta(idSubasta));
    }

    //Búsqueda flexible por dirección
    @GetMapping("/busqueda/{direccion}")
    public ResponseEntity<List<EnvioResponseDTO>> buscarPorDireccion(@PathVariable String direccion) {
        return ResponseEntity.ok(envioService.buscarPorDireccion(direccion));
    }

    //Contar cuántos envíos hay en un estado particular
    @GetMapping("/estado/{estado}/conteo")
    public ResponseEntity<Long> contarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(envioService.contarPorEstado(estado));
    }
}
