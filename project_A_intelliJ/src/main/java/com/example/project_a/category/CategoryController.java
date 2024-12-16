package com.example.project_a.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/admin/category/list")
    public String list(Model model) {
        List<Category> categories = service.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/category-list";
    }

    @GetMapping("/admin/category/add")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @PostMapping("/category/save")
    public String saveCategory(Category category, RedirectAttributes ra) {

        service.save(category);
        ra.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/admin/category/add";
    }
    @GetMapping("/admin/category/delete/{id}")
       public String deleteCategory(@PathVariable("id") String id , RedirectAttributes ra) {
                service.deleteUserById(Integer.parseInt(id));
                ra.addFlashAttribute("message", "The category has been deleted successfully.");
                return "redirect:/admin/category/list";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        // Fetch the category by its ID
        Category category = service.findCategoryById(Integer.parseInt(id));

        // Add the category object to the model
        model.addAttribute("updatedCategory", category);

        // Return the view for the edit form
        return "admin/category-edit";
    }

    // Method to handle the update of the category
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") String id, Category category, RedirectAttributes redirectAttributes) {
        service.updateCategory(id, category);
        redirectAttributes.addFlashAttribute("message", "Category updated successfully!");
        return "redirect:/admin/category/list";
    }


        @GetMapping("/admin/category/edit")
    public String ShowPageAdminCategoryEdit( Model model) {
            model.addAttribute("updatedCategory",new Category());
        return "admin/category-edit";
    }

}