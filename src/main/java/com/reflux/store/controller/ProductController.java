package com.reflux.store.controller;
import com.reflux.store.dto.product.ProductDto;
import com.reflux.store.models.Product;
import com.reflux.store.response.product.ProductResponse;
import com.reflux.store.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/{productCategoryId}/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody Product product, @PathVariable Long productCategoryId) {
        ProductDto productDto = productService.createProduct(product, productCategoryId);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<ProductResponse> getProducts() {
        ProductResponse  productResponse = productService.getProducts();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{productCategoryId}/list")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long productCategoryId) {
        ProductResponse  productResponse = productService.getProductsByCategory(productCategoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
