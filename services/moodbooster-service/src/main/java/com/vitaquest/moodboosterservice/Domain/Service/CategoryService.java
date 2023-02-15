package com.vitaquest.moodboosterservice.Domain.Service;

import com.vitaquest.moodboosterservice.Database.Repository.ICategoryRepository;
import com.vitaquest.moodboosterservice.Domain.DTO.AddCategoryDTO;
import com.vitaquest.moodboosterservice.Domain.DTO.UpdateCategoryDTO;
import com.vitaquest.moodboosterservice.Domain.Models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final List<Category> categories = new ArrayList<>();
    private final ICategoryRepository repository;

    @Autowired
    public CategoryService(ICategoryRepository repository) {
        this.repository = repository;
    }

    public Category addCategory(AddCategoryDTO DTO) {
        Category category = Category.builder()
                .name(DTO.getName())
                .build();

        category = repository.save(category);
        categories.add(category);
        return category;
    }


    public Category updateCategory(UpdateCategoryDTO DTO) {
        // get the id of the existing category
        Optional<Category> existingCategory = repository.findById(DTO.getId());
        // get the existing object details
        Category updatedCategory = existingCategory.stream().findFirst().orElse(null);
        // update details
        updatedCategory.setName(DTO.getName());
        // save record
        repository.save(updatedCategory);
        return updatedCategory;
    }

    public void deleteCategory(String id) {
        // get the id of the existing category
        Optional<Category> existingCategory = repository.findById(id);
        // get the existing object with that id
        Category updatedCategory = existingCategory.stream().findFirst().orElse(null);
        // delete record
        repository.delete(updatedCategory);
    }

    public Category getCategoryById(String id) {
        // look for the selected category by id
        Optional<Category> findCategory = repository.findById(id);
        // assign the correct category otherwise return null
        Category receivedCategory = findCategory.stream().findFirst().orElse(null);
        // return
        return receivedCategory;
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }
}
