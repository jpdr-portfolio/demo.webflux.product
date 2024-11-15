package com.jpdr.apps.demo.webflux.product.controller;

import com.jpdr.apps.demo.webflux.product.service.AppService;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  
  @GetMapping("/products")
  public Mono<ResponseEntity<List<ProductDto>>>findAllProducts(
    @RequestParam(name="categoryId",required = false) Integer categoryId,
    @RequestParam(name="retailerId",required = false) Integer retailerId){
    return this.appService.findAllProducts(categoryId, retailerId)
      .map(products -> new ResponseEntity<>(products, HttpStatus.OK));
  }
  
  @GetMapping("/products/{productId}")
  public Mono<ResponseEntity<ProductDto>> findProductById(@PathVariable(name="productId") Integer productId){
    return this.appService.findProductById(productId)
      .map(product -> new ResponseEntity<>(product, HttpStatus.OK));
  }
  
  @PostMapping("/products")
  public Mono<ResponseEntity<ProductDto>> createProduct(@RequestBody ProductDto productDto){
    return this.appService.createProduct(productDto)
      .map(product -> new ResponseEntity<>(product, HttpStatus.CREATED));
  }
  
  
  @GetMapping("/categories")
  public Mono<ResponseEntity<List<CategoryDto>>> findAllCategories(){
    return this.appService.findAllCategories()
      .map(categories -> new ResponseEntity<>(categories, HttpStatus.OK));
  }
  
  @GetMapping("/categories/{categoryId}")
  public Mono<ResponseEntity<CategoryDto>> findCategoryById(
    @PathVariable(name="categoryId") Integer categoryId){
    return this.appService.findCategoryById(categoryId)
      .map(category -> new ResponseEntity<>(category, HttpStatus.OK));
  }
  
  @PostMapping("/categories")
  public Mono<ResponseEntity<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto){
    return this.appService.createCategory(categoryDto)
      .map(category -> new ResponseEntity<>(category, HttpStatus.CREATED));
  }
  
}
