package com.example.project_a.category;
import com.example.project_a.user.User;
import com.example.project_a.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service

public class CategoryService {
    @Autowired private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public Category findCategoryById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    public void deleteUserById(Integer id) {
        if (findCategoryById(id) == null) {
            return;
        }
        categoryRepository.deleteById(id);
    }

    public void updateCategory(String id, Category updatedCategory) {
        // Fetch the existing category
        int categoryId = Integer.parseInt(id);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));

        // Update the fields of the existing category
        category.setCategoryname(updatedCategory.getCategoryname());
        category.setQuantity(updatedCategory.getQuantity());
        category.setImage(updatedCategory.getImage());
        category.setStatus(updatedCategory.getStatus());

        // Save the updated category
        categoryRepository.save(category);
    }

}