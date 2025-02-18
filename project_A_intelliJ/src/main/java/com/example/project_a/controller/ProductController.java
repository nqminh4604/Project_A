package com.example.project_a.controller;

import com.example.project_a.model.Category;
import com.example.project_a.model.Media;
import com.example.project_a.model.Product;
import com.example.project_a.service.CategoryService;
import com.example.project_a.service.MediaService;
import com.example.project_a.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MediaService mediaService;

    @GetMapping("/admin/product/list")
    public String ShowPageAdminProduct(Model model) {
        List<Product> products = service.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Product");
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
    public String ShowPageAdminProductDetails(@RequestParam String productId, Model model) {
        Product product = service.findProductById(Integer.parseInt(productId));
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Product Details");
        return "admin/product-details";
    }

    @GetMapping("/admin/product/edit")
    public String editProduct(@RequestParam String productId, Model model) {
        Product product = service.findProductById(Integer.parseInt(productId));
        List<Category> categories = categoryService.getAllCategories();
        categories.remove(product.getCategory());
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Product Edit");
        return "admin/product-edit";
    }

    @PostMapping("/admin/product/save")
    public String saveProduct(Product product, @RequestParam String categoryId,
                              @RequestParam("thumbnailName") String thumbnailName,
                              @RequestParam("mediaAlt") String mediaAlt,
                              @RequestParam("mediaAlt") String mediaName,
                              RedirectAttributes ra) {

        Media media = new Media();
        media.setName(mediaName);
        media.setAlt(mediaAlt);
        media.setImageURL(mediaService.getFileUrl(thumbnailName));
        mediaService.save(media);

        Category category = categoryService.findCategoryById(Integer.parseInt(categoryId));
        product.setCategory(category);
        product.setThumbnail(media);
        service.save(product);

        ra.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/admin/product/list";
    }

    @PostMapping("/admin/product/delete")
    public String deleteProduct(@RequestParam String productId, RedirectAttributes ra) {
        service.deleteProductById(Integer.parseInt(productId));
        return "redirect:/admin/product/list";
    }

    @PostMapping("/admin/product/edit")
    public String editProduct(@RequestParam Integer categoryId, Product product, RedirectAttributes ra) {
        Category category = categoryService.findCategoryById(categoryId);
        product.setCategory(category);
        service.updateProduct(product);
        ra.addFlashAttribute("message", "The product has been edited successfully.");
        return "redirect:/admin/product/list";
    }

    @PostMapping("/admin/product/status/change")
    public String changeStatus(@RequestParam String productId, RedirectAttributes ra) {
        Product product = service.findProductById(Integer.parseInt(productId));
        product.setStatus(product.getStatus().equals("Active") ? "Inactive" : "Active");
        service.save(product);
        ra.addFlashAttribute("message", "The product has been changed successfully.");
        return "redirect:/admin/product/list";
    }

    @GetMapping("/shop")
    public String showProductPage(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Product> products = service.getAllProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shop/shop-list";
    }

    @GetMapping("/filter-products")
    public String filterProducts(
            @RequestParam(value = "categories", required = false) List<Long> categoryIds,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sort", required = false) String sort,
            Model model) {

        List<Product> filteredProducts = service.getAllProducts();

        if (categoryIds != null && !categoryIds.isEmpty()) {
            filteredProducts = service.getProductsByCategoryIds(categoryIds);
        }

        if (minPrice != null && maxPrice != null) {
            double finalMinPrice = minPrice;
            double finalMaxPrice = maxPrice;
            filteredProducts = filteredProducts.stream()
                    .filter(p -> p.getPrice() >= finalMinPrice && p.getPrice() <= finalMaxPrice)
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            switch (sort) {
                case "nameAsc":
                    filteredProducts.sort(Comparator.comparing(Product::getName));
                    break;
                case "nameDesc":
                    filteredProducts.sort(Comparator.comparing(Product::getName).reversed());
                    break;
                case "priceAsc":
                    filteredProducts.sort(Comparator.comparing(Product::getPrice));
                    break;
                case "priceDesc":
                    filteredProducts.sort(Comparator.comparing(Product::getPrice).reversed());
                    break;
            }
        }

        model.addAttribute("products", filteredProducts);
        model.addAttribute("productsCount", filteredProducts.size());

        return "shop/shop-list :: productList";
    }


    @GetMapping("/home")
    public String showIndexPage(Model model) {
        List<Product> products = service.getAllProducts();
        List<Product> limit4Products = products.size() > 4 ? products.subList(0, 4) : products;
        List<Product> next4Products = products.size() > 8 ? products.subList(4, 8) :
                products.size() > 4 ? products.subList(4, products.size()) : Collections.emptyList();
        List<Product> limit8Products = products.size() > 8 ? products.subList(0, 8) :
                products.size() > 8 ? products.subList(0, products.size()) : Collections.emptyList();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("products", products);
        model.addAttribute("limit4Products", limit4Products);
        model.addAttribute("next4Products", next4Products);
        model.addAttribute("limit8Products", limit8Products);
        model.addAttribute("categories", categories);

        return "shop/index-2";
    }
}
