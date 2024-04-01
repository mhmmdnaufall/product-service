package mhmmdnaufall.productservice.controller;

import lombok.RequiredArgsConstructor;
import mhmmdnaufall.productservice.dto.ProductRequest;
import mhmmdnaufall.productservice.dto.ProductResponse;
import mhmmdnaufall.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest request) {
        productService.createProduct(request);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

}
