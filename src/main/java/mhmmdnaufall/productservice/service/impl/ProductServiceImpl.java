package mhmmdnaufall.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mhmmdnaufall.productservice.document.Product;
import mhmmdnaufall.productservice.dto.ProductRequest;
import mhmmdnaufall.productservice.dto.ProductResponse;
import mhmmdnaufall.productservice.repository.ProductRepository;
import mhmmdnaufall.productservice.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductRequest request) {
        final var product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        final var products = productRepository.findAll();
        return products.stream().map(this::toProductResponse).toList();
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

}
