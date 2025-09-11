package com.reflux.store.services;
import com.reflux.store.dto.product.ProductDto;
import com.reflux.store.exception.ResourceNotFoundException;
import com.reflux.store.interfaces.ProductServiceInterface;
import com.reflux.store.models.Product;
import com.reflux.store.models.ProductCategory;
import com.reflux.store.repositories.ProductCategoryRepository;
import com.reflux.store.repositories.ProductRepository;
import com.reflux.store.response.product.ProductResponse;
import com.reflux.store.utility.FileUploader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ProductService implements ProductServiceInterface {

    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    @Value("${project.image.path}")
    private String path;

    public ProductService(
        ModelMapper modelMapper,
        ProductCategoryRepository productCategoryRepository,
        ProductRepository productRepository
    ) {
        this.modelMapper = modelMapper;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        ProductResponse  productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductDto createProduct(Product product, Long productCategoryId) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        product.setProductCategory(productCategory);
        double discount = (product.getDiscount() * 0.01) * product.getPrice();
        double specialPrice = Math.round(product.getPrice() - discount);
        product.setSpecialPrice(specialPrice);
        product.setImage("product.png");

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductResponse getProductsByCategory(Long productCategoryId) {
        ProductCategory category =  productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        List<Product> products = productRepository.findByProductCategory(category);
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        ProductResponse  productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByNameLikeIgnoreCase(keyword);
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        ProductResponse  productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setSpecialPrice(productDto.getSpecialPrice());
        product.setDiscount(productDto.getDiscount());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());

        productRepository.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        String filename = FileUploader.uploadImage(path, image);
        product.setImage(filename);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }


}
