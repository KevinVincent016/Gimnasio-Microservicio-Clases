package co.analisys.gimnasio.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "gimnasio-miembros", url = "http://localhost:8083")
public interface MiembroClient {
    
}