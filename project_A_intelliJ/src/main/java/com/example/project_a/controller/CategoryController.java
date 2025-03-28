package com.example.project_a.controller;

import com.example.project_a.model.Category;
import com.example.project_a.model.CategoryDTO;
import com.example.project_a.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("/admin/category/list")
    public String listAllCategories(Model model) {
        List<CategoryDTO> categories = service.getAllCategoriesWithProductCount();
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Category");

        if (model.containsAttribute("message")) {
            System.out.println("Flash message: " + model.getAttribute("message"));
        }

        return "admin/category-list";
    }

    @GetMapping("/admin/category/add")
    public String showCategoryForm(Model model) {
        model.addAttribute("pageTitle", "Category add");
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @PostMapping("/admin/category/save")
    public String saveCategory(Category category, RedirectAttributes ra) {
        service.save(category);
        ra.addFlashAttribute("message",  "The category has been saved successfully.");
        return "redirect:/admin/category/list";
    }

    @GetMapping("/admin/category/delete")
    public String deleteCategory(@RequestParam("id") String id , RedirectAttributes ra) {
        service.deleteCategoryById(Integer.parseInt(id));
        ra.addFlashAttribute("message", "The category has been deleted successfully.");
        return "redirect:/admin/category/list";
    }

    @GetMapping("/admin/category/edit")
    public String showEditForm(@RequestParam(value = "id", required = false) String id, Model model, RedirectAttributes ra) {
        if (id != null) {
            Category category = service.findCategoryById(Integer.parseInt(id));
            model.addAttribute("category", category);
        }else {
            model.addAttribute("category", new Category());
        }
        model.addAttribute("pageTitle", "Category Edit");
        return "admin/category-edit";
    }

    // Method to handle the update of the category
    @PostMapping("/admin/category/update/{id}")
    public String updateCategory(@PathVariable("id") String id,
                                 @ModelAttribute Category category,
                                 RedirectAttributes ra) {
        try {
            service.updateCategory(id, category);
            ra.addFlashAttribute("message", "Category updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("massage", "Failed to update category: " + e.getMessage());
        }
        return "redirect:/admin/category/list";
    }

    @GetMapping("admin/category/status/change")
    public String changeCategoryStatus(@RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        try {
            // Find the category by ID
            Category category = service.findCategoryById(Integer.parseInt(id));
            category.setStatus(category.getStatus().equals("Active") ? "Inactive" : "Active");
            service.save(category);

            redirectAttributes.addFlashAttribute("message", "Category status changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error changing category status: " + e.getMessage());
        }
        return "redirect:/admin/category/list";
    }
}
