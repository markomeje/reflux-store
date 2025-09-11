package com.reflux.store.interfaces;
import com.reflux.store.dto.product.ProductDto;
import com.reflux.store.models.Product;
import com.reflux.store.response.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductServiceInterface {
    ProductResponse getProducts();
    ProductDto createProduct(Product product, Long productCategoryId);
    ProductResponse getProductsByCategory(Long productCategoryId);
    ProductResponse searchProductsByKeyword(String keyword);
    ProductDto updateProduct(Long productId, ProductDto productDto);
    ProductDto deleteProduct(Long productId);
    ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException;
}
