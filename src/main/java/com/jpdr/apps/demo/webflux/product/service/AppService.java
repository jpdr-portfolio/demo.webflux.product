package com.jpdr.apps.demo.webflux.product.service;

import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppService {
  
  Mono<List<ProductDto>> findAllProducts(Integer categoryId, Integer retailerId);
  Mono<List<ProductDto>> findAllProducts();
  Mono<ProductDto> findProductById(Integer productId);
  Mono<List<ProductDto>> findProductsByCategoryId(Integer categoryId);
  Mono<List<ProductDto>> findProductsByRetailerId(Integer retailerId);
  Mono<ProductDto> createProduct(ProductDto productDto);
  
  Mono<List<CategoryDto>> findAllCategories();
  Mono<CategoryDto> findCategoryById(Integer categoryId);
  Mono<CategoryDto> createCategory(CategoryDto categoryDto);
  
}
