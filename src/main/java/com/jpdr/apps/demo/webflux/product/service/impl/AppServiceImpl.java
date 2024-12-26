package com.jpdr.apps.demo.webflux.product.service.impl;

import com.jpdr.apps.demo.webflux.product.exception.product.CategoryNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.product.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.retailer.RetailerNotFoundException;
import com.jpdr.apps.demo.webflux.product.model.Category;
import com.jpdr.apps.demo.webflux.product.model.Product;
import com.jpdr.apps.demo.webflux.product.repository.product.CategoryRepository;
import com.jpdr.apps.demo.webflux.product.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.product.repository.retailer.RetailerRepository;
import com.jpdr.apps.demo.webflux.product.service.AppService;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.mapper.CategoryMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.ProductMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.OffsetDateTime;
import java.util.List;

import static com.jpdr.apps.demo.webflux.product.util.InputValidator.isValidName;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
  
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final RetailerRepository retailerRepository;
  
  @Override
  public Mono<List<ProductDto>> findAllProducts(Integer categoryId, Integer retailerId) {
    return Mono.from(Mono.justOrEmpty(categoryId)
        .flatMap(this::findProductsByCategoryId))
      .switchIfEmpty(Mono.from(Mono.justOrEmpty(retailerId)
        .flatMap(this::findProductsByRetailerId)))
      .switchIfEmpty(findAllProducts());
  }
  
  @Override
  public Mono<List<ProductDto>> findAllProducts(){
    log.debug("findAllProducts");
    return this.productRepository.findAllByIsActiveTrue()
      .doOnNext(product -> log.debug(product.toString()))
      .flatMapSequential(product ->
        Mono.zip(
          Mono.just(product),
          Mono.from(getCategory(product.getCategoryId())
            .switchIfEmpty(getEmptyCategory(product))),
          Mono.from(getRetailerDto(product.getRetailerId())
            .onErrorResume(ex -> {
                log.warn(ExceptionUtils.getStackTrace(ex));
                return getEmptyRetailerDto(product);
              }))))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        productDto.setCategoryId(tuple.getT2().getId());
        productDto.setCategoryName(tuple.getT2().getName());
        productDto.setRetailerId(tuple.getT3().getId());
        productDto.setRetailerName(tuple.getT3().getName());
        return productDto;
      })
      .doOnNext(productDto -> log.debug(productDto.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#productId", value = "products", sync = true)
  public Mono<ProductDto> findProductById(Integer productId) {
    log.debug("findProductById");
    return this.productRepository.findByIdAndIsActiveTrue(productId)
      .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
      .doOnNext(product -> log.debug(product.toString()))
      .flatMap(product ->
        Mono.zip(
          Mono.just(product),
          Mono.from(getCategory(product.getCategoryId())
            .switchIfEmpty(getEmptyCategory(product))),
          Mono.from(getRetailerDto(product.getRetailerId())
            .onErrorResume(ex -> {
              log.warn(ExceptionUtils.getStackTrace(ex));
              return getEmptyRetailerDto(product);
            }))))
      .map(tuple -> {
          ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
          productDto.setCategoryId(tuple.getT2().getId());
          productDto.setCategoryName(tuple.getT2().getName());
          productDto.setRetailerId(tuple.getT3().getId());
          productDto.setRetailerName(tuple.getT3().getName());
          return productDto;
      })
      .doOnNext(productDto -> log.debug(productDto.toString()));
      
  }
  
  @Override
  public Mono<List<ProductDto>> findProductsByCategoryId(Integer categoryId) {
    log.debug("findProductsByCategoryId");
    return Mono.from(getCategory(categoryId))
      .switchIfEmpty(Mono.error(new CategoryNotFoundException(categoryId)))
      .flatMapMany(category ->
        Flux.zip(
          Mono.just(category).repeat(),
          this.productRepository.findByCategoryIdAndIsActiveTrue(category.getId())))
      .flatMapSequential(tuple ->
        Flux.zip(
          Mono.just(tuple.getT1()).repeat(),
          Mono.just(tuple.getT2()).repeat(),
          Mono.from(getRetailerDto(tuple.getT2().getRetailerId()))))
      .map(tuple-> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT2());
        productDto.setCategoryId(tuple.getT1().getId());
        productDto.setCategoryName(tuple.getT1().getName());
        productDto.setRetailerId(tuple.getT3().getId());
        productDto.setRetailerName(tuple.getT3().getName());
        return productDto;
        })
      .doOnNext(productDto -> log.debug(productDto.toString()))
      .collectList();
  }
  
  @Override
  public Mono<List<ProductDto>> findProductsByRetailerId(Integer retailerId) {
    log.debug("findProductsByRetailerId");
    return Mono.from(getRetailerDto(retailerId))
      .flatMapMany(retailerDto ->
        Flux.zip(
          Mono.just(retailerDto).repeat(),
          Mono.from(this.productRepository.findByRetailerIdAndIsActiveTrue(retailerId))))
      .flatMapSequential(tuple ->
        Flux.zip(
          Mono.just(tuple.getT1()).repeat(),
          Mono.just(tuple.getT2()).repeat(),
          Mono.from(getCategory(tuple.getT2().getCategoryId()))))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT2());
        productDto.setCategoryId(tuple.getT3().getId());
        productDto.setCategoryName(tuple.getT3().getName());
        productDto.setRetailerId(tuple.getT1().getId());
        productDto.setRetailerName(tuple.getT1().getName());
        return productDto;
      })
      .doOnNext(productDto -> log.debug(productDto.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#productId", value = "products", sync = true)
  @Transactional
  public Mono<ProductDto> createProduct(ProductDto productDto) {
    log.debug("createProduct");
    return Mono.from(validateProduct(productDto))
      .flatMap(validProduct ->
        Mono.zip(
          Mono.just(validProduct),
          Mono.from(getCategory(validProduct.getCategoryId())
            .switchIfEmpty(Mono.error(new CategoryNotFoundException(validProduct.getCategoryId())))),
          Mono.from(getRetailerDto(validProduct.getRetailerId())
            .onErrorResume(ex ->Mono.error(new RetailerNotFoundException(validProduct.getRetailerId(),ex))))))
      .map(tuple -> {
        Product product = ProductMapper.INSTANCE.dtoToEntity(tuple.getT1());
        product.setCreationDate(OffsetDateTime.now());
        product.setIsActive(true);
        return Tuples.of(product, tuple.getT2(), tuple.getT3());
      })
     .flatMap(tuple -> Mono.zip(
       Mono.from(this.productRepository.save(tuple.getT1())),
       Mono.just(tuple.getT2()),
       Mono.just(tuple.getT3())))
      .map(tuple -> {
        ProductDto savedProductDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        savedProductDto.setCategoryName(tuple.getT2().getName());
        savedProductDto.setRetailerName(tuple.getT3().getName());
        return savedProductDto;
      })
    .doOnNext(savedProductDto -> log.debug(savedProductDto.toString()));
  }
  
  @Override
  public Mono<List<CategoryDto>> findAllCategories() {
    log.debug("findAllCategories");
    return this.categoryRepository.findAllByIsActiveTrue()
      .map(CategoryMapper.INSTANCE::entityToDto)
      .doOnNext(category -> log.debug(category.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#categoryId", value = "categories", sync = true)
  public Mono<CategoryDto> findCategoryById(Integer categoryId) {
    log.debug("findCategoryById");
    return this.categoryRepository.findByIdAndIsActiveTrue(categoryId)
      .switchIfEmpty(Mono.error(new CategoryNotFoundException(categoryId)))
      .map(CategoryMapper.INSTANCE::entityToDto)
      .doOnNext(category -> log.debug(category.toString()));
  }
  
  @Override
  @Cacheable(key = "#categoryId", value = "categories", sync = true)
  @Transactional
  public Mono<CategoryDto> createCategory(CategoryDto categoryDto) {
    log.debug("createCategory");
    return Mono.from(validateCategory(categoryDto))
      .map(validCategory -> {
        Category category =  CategoryMapper.INSTANCE.dtoToEntity(validCategory);
        category.setCreationDate(OffsetDateTime.now());
        category.setIsActive(true);
        return category;
      })
      .flatMap(categoryRepository::save)
      .map(CategoryMapper.INSTANCE::entityToDto)
      .doOnNext(resultCategory -> log.debug(resultCategory.toString()));
  }
  
  private Mono<Category> getCategory(Integer categoryId){
    return this.categoryRepository.findByIdAndIsActiveTrue(categoryId)
      .doOnNext(category -> log.debug(category.toString()));
  }
  
  private Mono<RetailerDto> getRetailerDto(Integer retailerId){
    return this.retailerRepository.getRetailerById(retailerId)
      .doOnNext(retailerDto -> log.debug(retailerDto.toString()));
  }
  
  private Mono<ProductDto> validateProduct(ProductDto productDto){
    return Mono.just(productDto)
      .filter(product -> isValidName(product.getName()))
      .switchIfEmpty(Mono.error(new ValidationException("The product name is not valid")));
  }
  
  private Mono<CategoryDto> validateCategory(CategoryDto categoryDto){
    return Mono.just(categoryDto)
      .filter(category -> isValidName(category.getName()))
      .switchIfEmpty(Mono.error(new ValidationException("The category name is not valid")));
  }
  
  private Mono<Category> getEmptyCategory(Product product){
    return Mono.just(Category.builder()
      .id(product.getCategoryId())
      .name(StringUtils.EMPTY)
      .build());
  }
  
  private Mono<RetailerDto> getEmptyRetailerDto(Product product){
    return Mono.just(RetailerDto.builder()
      .id(product.getRetailerId())
      .name(StringUtils.EMPTY)
      .build());
  }
  
}
