package mhmmdnaufall.productservice.service;

import mhmmdnaufall.productservice.dto.ProductRequest;
import mhmmdnaufall.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    void createProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

}
