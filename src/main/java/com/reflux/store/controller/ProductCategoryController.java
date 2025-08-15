package com.reflux.store.controller;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.payload.ProductCategoryResponse;
import com.reflux.store.services.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/category")
public class ProductCategoryController {

    private final ProductCategoryService categoryService;

    public ProductCategoryController(ProductCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<ProductCategoryResponse> getCategoryList() {
        ProductCategoryResponse productCategoryResponse = categoryService.getCategoryList();
        return new ResponseEntity<>(productCategoryResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@Valid @RequestBody ProductCategory category) {
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category Created Successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        String status = categoryService.deleteCategory(id);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody ProductCategory category, @PathVariable Long id) {
        categoryService.updateCategory(category, id);
        return ResponseEntity.ok(HttpStatus.OK.name());
    }

}
