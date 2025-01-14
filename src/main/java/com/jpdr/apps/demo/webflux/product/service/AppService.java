package com.jpdr.apps.demo.webflux.product.service;

import com.jpdr.apps.demo.webflux.product.service.dto.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppService {
  
  Mono<List<ProductDto>> findAllProducts(Long categoryId, Long subCategoryId,Long retailerId);
  Mono<List<ProductDto>> findAllProducts();
  Mono<ProductDto> findProductById(Long productId);
  Mono<List<ProductDto>> findProductsByCategoryId(Long categoryId);
  Mono<List<ProductDto>> findProductsBySubCategoryId(Long subCategoryId);
  Mono<List<ProductDto>> findProductsByRetailerId(Long retailerId);
  Mono<ProductDto> createProduct(ProductDto productDto);
  
  Mono<List<CategoryDto>> findAllCategories();
  Mono<CategoryDto> findCategoryById(Long categoryId);
  Mono<CategoryDto> createCategory(CategoryDto categoryDto);
  
  Mono<List<SubCategoryDto>> findAllSubCategories();
  Mono<SubCategoryDto> findSubCategoryById(Long subCategoryId);
  Mono<SubCategoryDto> createSubCategory(SubCategoryDto subCategoryDto);
  
  Mono<List<RetailerDto>> findAllRetailers();
  Mono<RetailerDto> findRetailerById(Long retailerId);
  Mono<RetailerDto> createRetailer(RetailerDto retailerDto);
  
}
