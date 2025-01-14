package com.example.project_a.controller;

import com.example.project_a.model.Category;
import com.example.project_a.model.Product;
import com.example.project_a.service.CategoryService;
import com.example.project_a.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/product/list")
    public String ShowPageAdminProduct(Model model) {
        model.addAttribute("pageTitle", "Product" );
        return "admin/product-list";
    }

    @GetMapping("/admin/product/add")
    public String showProductForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        return "admin/product-add";
    }

    @GetMapping("/admin/product/details")
    public String ShowPageAdminProductDetails(Model model) {
        model.addAttribute("pageTitle", "Product Details" );
        return "admin/product-details";}

    @GetMapping("/admin/product/edit")
    public String ShowPageAdminProductEdit(Model model) {
        model.addAttribute("pageTitle", "Product Edit" );
        return "admin/product-edit";}

    @PostMapping("/product/save")
    public String saveCategory(Product product, RedirectAttributes ra) {

        service.save(product);
        ra.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/admin";
    }

}
