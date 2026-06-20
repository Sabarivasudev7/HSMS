package cap_stone_project.caregory_service.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import cap_stone_project.caregory_service.entity.Category;



@DataJpaTest
public class CategoryRepositoryTest {
	
	@Autowired
	private CategoryRepository cateRepo;
	
	
	@Test
	void testFindByCategoryName() {
		Category cate = new Category(null, "Plumbing", "Piperelated services", 500, true);
		
		cateRepo.save(cate);
		Optional<Category> result = cateRepo.findByCategoryName("Plumbing");
		
		assertTrue(result.isPresent());
		assertEquals("Plumbing", result.get().getCategoryName());
	}
	
	
	
	@Test
	void testFindByIsActiveTrue() {
		Category c1 = new Category(null, "Plumbing", "Pipe service", 500, true);
		Category c2 = new Category(null, "Electrical", "Electrical service", 700, false);
		cateRepo.save(c1);
		cateRepo.save(c2);
		
		List<Category> cateList = cateRepo.findByIsActiveTrue();
		
		assertEquals(1, cateList.size());
		assertEquals("Plumbing", cateList.get(0).getCategoryName());
	}
	
	
	
	@Test
	void testFindById() {
		Category cate = new Category(null, "Cleaning", "Cleaning Service", 300, true);
		Category savedCate = cateRepo.save(cate);
		
		Optional<Category> result = cateRepo.findById(savedCate.getCategoryId());
		
		assertTrue(result.isPresent());
		assertEquals("Cleaning", result.get().getCategoryName());
	}
	

}
