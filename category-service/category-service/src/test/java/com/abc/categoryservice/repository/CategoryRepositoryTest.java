package com.abc.categoryservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.abc.categoryservice.entity.ServiceCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    void testFindByCategoryName() {
        String name = "Plumbing_" + System.nanoTime();
        ServiceCategory category = new ServiceCategory(null, name, "Repairs for pipes", 120.0, true);
        repository.save(category);

        Optional<ServiceCategory> found = repository.findByCategoryName(name);
        assertTrue(found.isPresent());
        assertEquals(name, found.get().getCategoryName());
        assertEquals("Repairs for pipes", found.get().getDescription());
        assertEquals(120.0, found.get().getBasePrice());
    }

    @Test
    void testExistsByCategoryName() {
        String electricalName = "Electrical_" + System.nanoTime();
        String cleaningName = "Cleaning_" + System.nanoTime();
        ServiceCategory category = new ServiceCategory(null, electricalName, "Wiring work", 150.0, true);
        repository.save(category);

        assertTrue(repository.existsByCategoryName(electricalName));
        assertFalse(repository.existsByCategoryName(cleaningName));
    }

    @Test
    void testFindByActiveTrue() {
        String cleaningName = "Cleaning_" + System.nanoTime();
        String pestControlName = "PestControl_" + System.nanoTime();
        ServiceCategory activeCat = new ServiceCategory(null, cleaningName, "Home cleaning services", 80.0, true);
        ServiceCategory inactiveCat = new ServiceCategory(null, pestControlName, "Fumigation services", 90.0, false);
        repository.save(activeCat);
        repository.save(inactiveCat);

        List<ServiceCategory> activeCategories = repository.findByActiveTrue();
        assertFalse(activeCategories.isEmpty());
        assertTrue(activeCategories.stream().anyMatch(cat -> cleaningName.equals(cat.getCategoryName())));
        assertTrue(activeCategories.stream().noneMatch(cat -> pestControlName.equals(cat.getCategoryName())));
    }

    @Test
    void testFindByCategoryIdAndActiveTrue() {
        String applianceName = "Appliance_" + System.nanoTime();
        String paintingName = "Painting_" + System.nanoTime();
        ServiceCategory activeCat = new ServiceCategory(null, applianceName, "Microwave/Fridge repair", 110.0, true);
        ServiceCategory inactiveCat = new ServiceCategory(null, paintingName, "Interior painting", 130.0, false);
        
        ServiceCategory savedActive = repository.save(activeCat);
        ServiceCategory savedInactive = repository.save(inactiveCat);

        Optional<ServiceCategory> foundActive = repository.findByCategoryIdAndActiveTrue(savedActive.getCategoryId());
        assertTrue(foundActive.isPresent());
        assertEquals(applianceName, foundActive.get().getCategoryName());

        Optional<ServiceCategory> foundInactive = repository.findByCategoryIdAndActiveTrue(savedInactive.getCategoryId());
        assertFalse(foundInactive.isPresent());
    }
}

