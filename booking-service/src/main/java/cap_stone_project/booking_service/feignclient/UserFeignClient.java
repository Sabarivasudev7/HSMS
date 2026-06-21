//package cap_stone_project.booking_service.feignclient;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import cap_stone_project.booking_service.DTO.UserResponseDTO;
//import cap_stone_project.booking_service.model.CustomerResponseDTO;
//
//
//
//@FeignClient(name = "USER-SERVICE")
//public interface UserFeignClient {
//
//    @GetMapping("/api/users/{userId}")
//    UserResponseDTO getUserById(@PathVariable Long userId);
//    
//}