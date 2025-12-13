package com.reflux.store.controllers;
import com.reflux.store.config.PaginationConstants;
import com.reflux.store.dto.product.ProductDto;
import com.reflux.store.response.product.ProductResponse;
import com.reflux.store.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin/product")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/{productCategoryId}/create")
    public ResponseEntity<ProductDto> createProduct(
        @Valid
        @RequestBody ProductDto productDto,
        @PathVariable Long productCategoryId
    ) {
        ProductDto newProduct = productService.createProduct(productDto, productCategoryId);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<ProductResponse> getProducts(
        @RequestParam(name = "page", defaultValue = PaginationConstants.PAGE_NUMBER, required = false) Integer page,
        @RequestParam(name = "limit", defaultValue = PaginationConstants.PAGE_SIZE, required = false) Integer limit,
        @RequestParam(name = "sortOrder", defaultValue = PaginationConstants.SORT_ORDER, required = false) String sortOrder,
        @RequestParam(name = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy
    ) {
        ProductResponse  productResponse = productService.getProducts(page, limit, sortOrder, sortBy);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{productCategoryId}/list")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long productCategoryId) {
        ProductResponse  productResponse = productService.getProductsByCategory(productCategoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ProductResponse> searchProductsByKeyword(@PathVariable String keyword) {
        ProductResponse  productResponse = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
        @Valid
        @PathVariable Long productId,
        @RequestBody ProductDto productDto
    ) {
        ProductDto updatedProduct = productService.updateProduct(productId, productDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId) {
        ProductDto deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update-image")
    public ResponseEntity<ProductDto> updateProductImage(
        @Valid
        @PathVariable Long productId,
        @RequestParam("image") MultipartFile image
    ) throws IOException {
        ProductDto updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
