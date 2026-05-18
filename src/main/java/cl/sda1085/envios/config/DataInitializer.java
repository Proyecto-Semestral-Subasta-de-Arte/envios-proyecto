package cl.sda1085.envios.config;

import cl.sda1085.envios.model.Envio;
import cl.sda1085.envios.repository.EnvioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    //Conexión con 'repository'
    private final EnvioRepository envioRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (envioRepository.count() > 0) {
            log.info("Base de datos de envíos ya contiene datos. Omitiendo inicialización.");
            return;
        }
        log.info("Iniciando la creación de envíos de prueba para el sistema.");

        //Envio 1: Vasija Precolombina
        Envio e1 = new Envio(
                null,
                1L,
                "Av. Libertad 123, Viña del Mar",
                "ENTREGADO",
                generarCodigoSeguimiento()
        );

        //Envio 2: Espada Medieval
        Envio e2 = new Envio(
                null,
                2L,
                "Calle Los Leones 456, Providencia, Santiago",
                "EN CAMINO",
                generarCodigoSeguimiento()
        );

        //Envio 3: Armadura Samurai
        Envio e3 = new Envio(
                null,
                3L,
                "Av. Apoquindo 789, Las Condes, Santiago",
                "PENDIENTE",
                generarCodigoSeguimiento()
        );

        //Envio 4: Óleo del Siglo XVIII
        Envio e4 = new Envio(
                null,
                5L,
                "Maipú 342, San Felipe de Aconcagua",
                "ENTREGADO",
                generarCodigoSeguimiento());

        //Envio 5: Monedas de Oro Antiguas
        Envio e5 = new Envio(
                null,
                8L,
                "Errázuriz 1020, Valparaíso",
                "DEVUELTO",
                generarCodigoSeguimiento());

        //Envio 6: Reloj de Bolsillo Vintage
        Envio e6 = new Envio(
                null,
                6L,
                "Diego Portales 55, Quilpué",
                "EN CAMINO",
                generarCodigoSeguimiento());

        //Envio 7: Mapa Cartográfico Original
        Envio e7 = new Envio(
                null,
                7L,
                "Paseo Ahumada 11, Santiago Centro",
                "PENDIENTE",
                generarCodigoSeguimiento());

        //Guardado de los 7 registros obligatorios
        envioRepository.saveAll(List.of(e1, e2, e3, e4, e5, e6, e7));

        log.info("Se han registrado exitosamente 7 envíos de prueba con seguimiento dinámico.");
    }

    //Generación codigo único de seguimiento
    private String generarCodigoSeguimiento() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
