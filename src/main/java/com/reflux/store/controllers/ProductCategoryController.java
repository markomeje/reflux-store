package com.reflux.store.controllers;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.services.ProductCategoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/category")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public List<ProductCategory> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @PostMapping("/create")
    public String createCategory(@Valid @RequestBody ProductCategory category) {
        categoryService.createCategory(category);
        return "Category Created Successfully";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            String status = categoryService.deleteCategory(id);
            return ResponseEntity.ok(status);
        }catch (ResponseStatusException e) {
            return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@RequestBody ProductCategory category, @PathVariable Long id) {
        try {
            categoryService.updateCategory(category, id);
            return ResponseEntity.ok(HttpStatus.OK.name());
        }catch (ResponseStatusException e) {
            return new ResponseEntity<String>(e.getReason(), e.getStatusCode());
        }
    }

}
