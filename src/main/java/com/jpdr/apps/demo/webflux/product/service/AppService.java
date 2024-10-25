package com.jpdr.apps.demo.webflux.product.service;

import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppService {
  
  Flux<ProductDto> findAllProducts(Integer categoryId, Integer retailerId);
  Flux<ProductDto> findAllProducts();
  Mono<ProductDto> findProductById(Integer productId);
  Flux<ProductDto> findProductsByCategoryId(Integer categoryId);
  Flux<ProductDto> findProductsByRetailerId(Integer retailerId);
  Mono<ProductDto> createProduct(ProductDto productDto);
  
  Flux<CategoryDto> findAllCategories();
  Mono<CategoryDto> findCategoryById(Integer categoryId);
  Mono<CategoryDto> createCategory(CategoryDto categoryDto);
  
}
