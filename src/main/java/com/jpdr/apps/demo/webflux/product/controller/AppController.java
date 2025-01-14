package com.jpdr.apps.demo.webflux.product.controller;

import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.product.service.AppService;
import com.jpdr.apps.demo.webflux.product.service.dto.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
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
  private final EventLogger eventLogger;
  
  @GetMapping("/products")
  public Mono<ResponseEntity<List<ProductDto>>> findAllProducts(
    @RequestParam(name="categoryId",required = false) Long categoryId,
    @RequestParam(name="subCategoryId",required = false) Long subCategoryId,
    @RequestParam(name="retailerId",required = false) Long retailerId){
    return this.appService.findAllProducts(categoryId, subCategoryId, retailerId)
      .doOnNext(list -> this.eventLogger.logEvent("findAllProducts", list))
      .map(products -> new ResponseEntity<>(products, HttpStatus.OK));
  }
  
  @GetMapping("/products/{productId}")
  public Mono<ResponseEntity<ProductDto>> findProductById(@PathVariable(name="productId") Long productId){
    return this.appService.findProductById(productId)
      .doOnNext(product -> this.eventLogger.logEvent("findProductById", product))
      .map(product -> new ResponseEntity<>(product, HttpStatus.OK));
  }
  
  @PostMapping("/products")
  public Mono<ResponseEntity<ProductDto>> createProduct(@RequestBody ProductDto productDto){
    return this.appService.createProduct(productDto)
      .doOnNext(product -> this.eventLogger.logEvent("createProduct", product))
      .map(product -> new ResponseEntity<>(product, HttpStatus.CREATED));
  }
  
  
  @GetMapping("/categories")
  public Mono<ResponseEntity<List<CategoryDto>>> findAllCategories(){
    return this.appService.findAllCategories()
      .doOnNext(categories -> this.eventLogger
        .logEvent("findAllCategories", categories))
      .map(categories -> new ResponseEntity<>(categories, HttpStatus.OK));
  }
  
  @GetMapping("/categories/{categoryId}")
  public Mono<ResponseEntity<CategoryDto>> findCategoryById(
    @PathVariable(name="categoryId") Long categoryId){
    return this.appService.findCategoryById(categoryId)
      .doOnNext(category -> this.eventLogger
        .logEvent("findCategoryById", category))
      .map(category -> new ResponseEntity<>(category, HttpStatus.OK));
  }
  
  @PostMapping("/categories")
  public Mono<ResponseEntity<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto){
    return this.appService.createCategory(categoryDto)
      .doOnNext(category -> this.eventLogger.logEvent("createCategory", category))
      .map(category -> new ResponseEntity<>(category, HttpStatus.CREATED));
  }
  
  @GetMapping("/subcategories")
  public Mono<ResponseEntity<List<SubCategoryDto>>> findAllSubCategories(){
    return this.appService.findAllSubCategories()
      .doOnNext(subCategories -> this.eventLogger
        .logEvent("findAllSubCategories", subCategories))
      .map(subCategories -> new ResponseEntity<>(subCategories, HttpStatus.OK));
  }
  
  @GetMapping("/subcategories/{subCategoryId}")
  public Mono<ResponseEntity<SubCategoryDto>> findSubCategoryById(
    @PathVariable(name="subCategoryId") Long subCategoryId){
    return this.appService.findSubCategoryById(subCategoryId)
      .doOnNext(subCategory -> this.eventLogger
        .logEvent("findSubCategoryById", subCategory))
      .map(subCategory -> new ResponseEntity<>(subCategory, HttpStatus.OK));
  }
  
  @PostMapping("/subcategories")
  public Mono<ResponseEntity<SubCategoryDto>> createSubCategory(@RequestBody SubCategoryDto subCategoryDto){
    return this.appService.createSubCategory(subCategoryDto)
      .doOnNext(subCategory -> this.eventLogger.logEvent("createSubCategory", subCategory))
      .map(subCategory -> new ResponseEntity<>(subCategory, HttpStatus.CREATED));
  }
  
  @GetMapping("/retailers")
  public Mono<ResponseEntity<List<RetailerDto>>> findAllRetailers(){
    return this.appService.findAllRetailers()
      .doOnNext(list -> this.eventLogger.logEvent("findAllRetailers", list))
      .map(retailers -> new ResponseEntity<>(retailers, HttpStatus.OK));
  }
  
  @GetMapping("/retailers/{retailerId}")
  public Mono<ResponseEntity<RetailerDto>> findRetailerById(
    @PathVariable(name="retailerId") Long retailerId){
    return this.appService.findRetailerById(retailerId)
      .doOnNext(retailer -> this.eventLogger.logEvent("findRetailerById", retailer))
      .map(retailer -> new ResponseEntity<>(retailer, HttpStatus.OK));
  }
  
  @PostMapping("/retailers")
  public Mono<ResponseEntity<RetailerDto>> createRetailer(@RequestBody RetailerDto retailerDto){
    return this.appService.createRetailer(retailerDto)
      .doOnNext(retailer -> this.eventLogger.logEvent("createRetailer", retailer))
      .map(retailer -> new ResponseEntity<>(retailer, HttpStatus.CREATED));
  }
  
}
