package cap_stone_project.caregory_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cap_stone_project.caregory_service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	Optional<Category> findByCategoryName(String categoryName);

    List<Category> findByIsActiveTrue();

}
