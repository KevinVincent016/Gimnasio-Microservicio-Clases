package co.analisys.gimnasio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import co.analisys.gimnasio.dto.EntrenadorDTO;

@FeignClient(name = "gimnasio-entrenadores", url = "http://localhost:8081")
public interface EntrenadorClient {
    @GetMapping("/api/trainers/{id}")
    EntrenadorDTO getEntrenadorById(@PathVariable("id") Long id);
}