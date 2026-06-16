package cap_stone_project.assignment_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cap_stone_project.assignment_service.model.TechnicianResponseDTO;


@FeignClient(name="USER-SERVICE")
public interface TechnicianFeignClient {

    @GetMapping("/api/technician/{technicianId}")
    TechnicianResponseDTO getTechnicianById(@PathVariable Long technicianId);
}