package com.jpdr.apps.demo.webflux.product.service.impl;

import com.jpdr.apps.demo.webflux.commons.caching.CacheHelper;
import com.jpdr.apps.demo.webflux.commons.validation.InputValidator;
import com.jpdr.apps.demo.webflux.product.exception.CategoryNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.ProductNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.RetailerNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.SubCategoryNotFoundException;
import com.jpdr.apps.demo.webflux.product.model.Category;
import com.jpdr.apps.demo.webflux.product.model.Product;
import com.jpdr.apps.demo.webflux.product.model.Retailer;
import com.jpdr.apps.demo.webflux.product.model.SubCategory;
import com.jpdr.apps.demo.webflux.product.repository.CategoryRepository;
import com.jpdr.apps.demo.webflux.product.repository.ProductRepository;
import com.jpdr.apps.demo.webflux.product.repository.RetailerRepository;
import com.jpdr.apps.demo.webflux.product.repository.SubCategoryRepository;
import com.jpdr.apps.demo.webflux.product.service.AppService;
import com.jpdr.apps.demo.webflux.product.service.dto.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
import com.jpdr.apps.demo.webflux.product.service.mapper.CategoryMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.ProductMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.RetailerMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.SubCategoryMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.OffsetDateTime;
import java.util.List;

import static com.jpdr.apps.demo.webflux.commons.validation.InputValidator.isValidName;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
  
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final SubCategoryRepository subCategoryRepository;
  private final RetailerRepository retailerRepository;
  private final CacheHelper cacheHelper;
  
  @Override
  public Mono<List<ProductDto>> findAllProducts(Long categoryId, Long subCategoryId,
    Long retailerId) {
    if(categoryId != null){
      return this.findProductsByCategoryId(categoryId);
    }else{
      if (subCategoryId != null){
        return this.findProductsBySubCategoryId(subCategoryId);
      }else{
        if (retailerId != null){
          return this.findProductsByRetailerId(retailerId);
        }else{
          return this.findAllProducts();
        }
      }
    }
  }
  
  @Override
  public Mono<List<ProductDto>> findAllProducts() {
    log.debug("findAllProducts");
    return this.productRepository.findAllByIsActiveTrueOrderById()
      .doOnNext(product -> log.debug(product.toString()))
      .flatMapSequential(product ->
        Mono.zip(Mono.just(product),
          Mono.from(getCategory(product.getCategoryId())
            .switchIfEmpty(getEmptyCategory(product.getCategoryId()))),
          Mono.from(getSubCategory(product.getSubCategoryId())
              .switchIfEmpty(getEmptySubCategory(product.getSubCategoryId()))),
          Mono.from(getRetailer(product.getRetailerId())
            .switchIfEmpty(getEmptyRetailer(product.getRetailerId())))))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        productDto.setCategoryId(tuple.getT2().getId());
        productDto.setCategoryName(tuple.getT2().getName());
        productDto.setSubCategoryId(tuple.getT3().getId());
        productDto.setSubCategoryName(tuple.getT3().getName());
        productDto.setRetailerId(tuple.getT4().getId());
        productDto.setRetailerName(tuple.getT4().getName());
        return productDto;
        })
      .doOnNext(productDto -> log.debug(productDto.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#productId", value = "products", sync = true)
  public Mono<ProductDto> findProductById(Long productId) {
    log.debug("findProductById");
    return this.productRepository
      .findByIdAndIsActiveTrue(productId)
        .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
      .doOnNext(product -> log.debug(product.toString()))
      .flatMap(product ->
        Mono.zip(
          Mono.just(product),
          Mono.from(getCategory(product.getCategoryId())
            .switchIfEmpty(getEmptyCategory(product.getCategoryId()))),
          Mono.from(getSubCategory(product.getSubCategoryId())
            .switchIfEmpty(getEmptySubCategory(product.getSubCategoryId()))),
          Mono.from(getRetailer(product.getRetailerId())
            .switchIfEmpty(getEmptyRetailer(product.getRetailerId())))
          ))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        productDto.setCategoryId(tuple.getT2().getId());
        productDto.setCategoryName(tuple.getT2().getName());
        productDto.setSubCategoryId(tuple.getT3().getId());
        productDto.setSubCategoryName(tuple.getT3().getName());
        productDto.setRetailerId(tuple.getT4().getId());
        productDto.setRetailerName(tuple.getT4().getName());
        return productDto;
      })
      .doOnNext(productDto -> log.debug(productDto.toString()));
    
  }
  
  @Override
  public Mono<List<ProductDto>> findProductsByCategoryId(Long categoryId) {
    log.debug("findProductsByCategoryId");
    return Mono.from(getCategory(categoryId))
        .switchIfEmpty(Mono.error(new CategoryNotFoundException(categoryId)))
      .flatMapMany(category ->
        Flux.zip(
          Mono.from(this.productRepository
            .findByCategoryIdAndIsActiveTrueOrderById(category.getId())),
          Mono.just(category).repeat()))
      .flatMapSequential(tuple ->
        Flux.zip(
          Mono.just(tuple.getT1()).repeat(),
          Mono.just(tuple.getT2()).repeat(),
          Mono.from(getSubCategory(tuple.getT1().getSubCategoryId())),
          Mono.from(getRetailer(tuple.getT1().getRetailerId()))))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        productDto.setCategoryId(tuple.getT2().getId());
        productDto.setCategoryName(tuple.getT2().getName());
        productDto.setSubCategoryId(tuple.getT3().getId());
        productDto.setSubCategoryName(tuple.getT3().getName());
        productDto.setRetailerId(tuple.getT4().getId());
        productDto.setRetailerName(tuple.getT4().getName());
        return productDto;
        })
        .doOnNext(productDto -> log.debug(productDto.toString()))
        .collectList();
  }
  
  @Override
  public Mono<List<ProductDto>> findProductsBySubCategoryId(Long subCategoryId) {
    log.debug("findProductsBySubCategoryId");
    return Mono.from(getSubCategory(subCategoryId))
        .switchIfEmpty(Mono.error(new SubCategoryNotFoundException(subCategoryId)))
      .flatMapMany(subCategory ->
        Flux.zip(
          Mono.from(this.productRepository
            .findBySubCategoryIdAndIsActiveTrueOrderById(subCategory.getId())),
          Mono.just(subCategory).repeat()))
      .flatMapSequential(tuple ->
        Flux.zip(
          Mono.just(tuple.getT1()).repeat(),
          Mono.from(getCategory(tuple.getT1().getCategoryId())),
          Mono.just(tuple.getT2()).repeat(),
          Mono.from(getRetailer(tuple.getT1().getRetailerId()))))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        productDto.setCategoryId(tuple.getT2().getId());
        productDto.setCategoryName(tuple.getT2().getName());
        productDto.setSubCategoryId(tuple.getT3().getId());
        productDto.setSubCategoryName(tuple.getT3().getName());
        productDto.setRetailerId(tuple.getT4().getId());
        productDto.setRetailerName(tuple.getT4().getName());
        return productDto;
        })
        .doOnNext(productDto -> log.debug(productDto.toString()))
        .collectList();
  }
  
  @Override
  public Mono<List<ProductDto>> findProductsByRetailerId(Long retailerId) {
    log.debug("findProductsByRetailerId");
    return Mono.from(getRetailer(retailerId))
      .switchIfEmpty(Mono.error(new RetailerNotFoundException(retailerId)))
      .flatMapMany(retailerDto ->
        Flux.zip(
          Mono.from(this.productRepository
            .findByRetailerIdAndIsActiveTrueOrderById(retailerId)),
        Mono.just(retailerDto).repeat()))
      .flatMapSequential(tuple ->
        Flux.zip(
          Mono.just(tuple.getT1()).repeat(),
          Mono.from(getCategory(tuple.getT1().getCategoryId())),
          Mono.from(getSubCategory(tuple.getT1().getSubCategoryId())),
          Mono.just(tuple.getT2()).repeat()))
      .map(tuple -> {
        ProductDto productDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        productDto.setCategoryId(tuple.getT2().getId());
        productDto.setCategoryName(tuple.getT2().getName());
        productDto.setSubCategoryId(tuple.getT3().getId());
        productDto.setSubCategoryName(tuple.getT3().getName());
        productDto.setRetailerId(tuple.getT4().getId());
        productDto.setRetailerName(tuple.getT4().getName());
        return productDto;
        })
      .doOnNext(productDto -> log.debug(productDto.toString()))
      .collectList();
  }
  
  @Override
  @Transactional
  public Mono<ProductDto> createProduct(ProductDto productDto) {
    log.debug("createProduct");
    return Mono.from(validateProduct(productDto))
      .flatMap(validProduct ->
        Mono.zip(
          Mono.just(validProduct),
          Mono.from(getCategory(validProduct.getCategoryId())
            .switchIfEmpty(
              Mono.error(new CategoryNotFoundException(validProduct.getCategoryId())))),
          Mono.from(getSubCategory(validProduct.getSubCategoryId())
            .switchIfEmpty(
              Mono.error(new SubCategoryNotFoundException(validProduct.getSubCategoryId())))),
          Mono.from(getRetailer(validProduct.getRetailerId())
            .switchIfEmpty(
              Mono.error(new RetailerNotFoundException(validProduct.getRetailerId()))))))
      .map(tuple -> {
        Product product = ProductMapper.INSTANCE.dtoToEntity(tuple.getT1());
        product.setCreationDate(OffsetDateTime.now());
        product.setIsActive(true);
        return Tuples.of(product, tuple.getT2(), tuple.getT3(), tuple.getT4());
      })
      .flatMap(tuple ->
        Mono.zip(
          Mono.from(this.productRepository.save(tuple.getT1())),
          Mono.just(tuple.getT2()),
          Mono.just(tuple.getT3()),
          Mono.just(tuple.getT4())))
      .map(tuple -> {
        ProductDto savedProductDto = ProductMapper.INSTANCE.entityToDto(tuple.getT1());
        savedProductDto.setCategoryName(tuple.getT2().getName());
        savedProductDto.setSubCategoryName(tuple.getT3().getName());
        savedProductDto.setRetailerName(tuple.getT4().getName());
        return savedProductDto;
      })
      .doOnNext(savedProductDto ->
        log.debug(savedProductDto.toString()))
      .doOnNext(savedProductDto ->
        this.cacheHelper.put("products", savedProductDto.getId(), savedProductDto));
  }
  
  @Override
  public Mono<List<CategoryDto>> findAllCategories() {
    log.debug("findAllCategories");
    return this.categoryRepository.findAllByIsActiveTrueOrderById()
      .map(CategoryMapper.INSTANCE::entityToDto)
      .doOnNext(category -> log.debug(category.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#categoryId", value = "categories", sync = true)
  public Mono<CategoryDto> findCategoryById(Long categoryId) {
    log.debug("findCategoryById");
    return this.categoryRepository.findByIdAndIsActiveTrue(categoryId)
      .switchIfEmpty(Mono.error(new CategoryNotFoundException(categoryId)))
      .map(CategoryMapper.INSTANCE::entityToDto)
      .doOnNext(category -> log.debug(category.toString()));
  }
  
  @Override
  @Transactional
  public Mono<CategoryDto> createCategory(CategoryDto categoryDto) {
    log.debug("createCategory");
    return Mono.from(validateCategory(categoryDto)).map(validCategory -> {
      Category category = CategoryMapper.INSTANCE.dtoToEntity(validCategory);
      category.setCreationDate(OffsetDateTime.now());
      category.setIsActive(true);
      return category;
    }).flatMap(categoryRepository::save).map(CategoryMapper.INSTANCE::entityToDto).doOnNext(resultCategory -> log.debug(resultCategory.toString())).doOnNext(resultCategory -> this.cacheHelper.put("categories", resultCategory.getId(), resultCategory));
  }
  
  @Override
  public Mono<List<SubCategoryDto>> findAllSubCategories() {
    log.debug("findAllSubCategories");
    return this.subCategoryRepository
      .findAllByIsActiveTrueOrderById()
      .flatMapSequential(subCategory ->
        Mono.zip(
          Mono.just(subCategory),
          Mono.from(getCategory(subCategory.getCategoryId()))
            .switchIfEmpty(getEmptyCategory(subCategory.getCategoryId()))
        ))
      .map(tuple -> {
        SubCategoryDto subCategoryDto = SubCategoryMapper.INSTANCE.entityToDto(tuple.getT1());
        subCategoryDto.setCategoryName(tuple.getT2().getName());
        return subCategoryDto;
        })
      .doOnNext(subCategory -> log.debug(subCategory.toString()))
      .collectList();
  }
  
  @Override
  @Cacheable(key = "#subCategoryId", value = "subcategories", sync = true)
  public Mono<SubCategoryDto> findSubCategoryById(Long subCategoryId) {
    log.debug("findSubCategoryById");
    return this.subCategoryRepository
      .findByIdAndIsActiveTrue(subCategoryId)
      .switchIfEmpty(Mono.error(new SubCategoryNotFoundException(subCategoryId)))
      .flatMap(subCategory ->
        Mono.zip(
          Mono.just(subCategory),
          Mono.from(getCategory(subCategory.getCategoryId()))
            .switchIfEmpty(getEmptyCategory(subCategory.getCategoryId()))
        ))
      .map(tuple -> {
        SubCategoryDto subCategoryDto = SubCategoryMapper.INSTANCE.entityToDto(tuple.getT1());
        subCategoryDto.setCategoryName(tuple.getT2().getName());
        return subCategoryDto;
        })
      .doOnNext(subCategory -> log.debug(subCategory.toString()));
  }
  
  @Override
  public Mono<SubCategoryDto> createSubCategory(SubCategoryDto subCategoryDto) {
    log.debug("createSubCategory");
    return Mono.from(validateSubCategory(subCategoryDto))
      .flatMap( subCategory ->
        Mono.zip(
          Mono.just(subCategory),
          Mono.from(getCategory(subCategory.getCategoryId()))
            .switchIfEmpty(Mono.error(new CategoryNotFoundException(
              subCategoryDto.getCategoryId())))
        ))
      .map(tuple -> {
        SubCategory subCategory = SubCategoryMapper.INSTANCE.dtoToEntity(tuple.getT1());
        subCategory.setCreationDate(OffsetDateTime.now());
        subCategory.setIsActive(true);
        return subCategory;
    })
      .flatMap(subCategoryRepository::save)
      .map(SubCategoryMapper.INSTANCE::entityToDto)
      .doOnNext(resultSubCategory ->
        log.debug(resultSubCategory.toString()))
      .doOnNext(resultSubCategory ->
        this.cacheHelper.put("subcategories", resultSubCategory.getId(),
          resultSubCategory));
  }
  
  @Override
  public Mono<List<RetailerDto>> findAllRetailers() {
    log.debug("findAllRetailers");
    return this.retailerRepository.findAllByIsActiveIsTrueOrderById()
      .map(RetailerMapper.INSTANCE::entityToDto)
      .doOnNext(retailer -> log.debug(retailer.toString())).collectList();
  }
  
  @Override
  @Cacheable(key = "#retailerId", value = "retailers", sync = true)
  public Mono<RetailerDto> findRetailerById(Long retailerId) {
    log.debug("findRetailerById");
    return this.retailerRepository.findByIdAndIsActiveIsTrue(retailerId)
      .switchIfEmpty(Mono.error(new RetailerNotFoundException(retailerId)))
      .map(RetailerMapper.INSTANCE::entityToDto)
      .doOnNext(retailer -> log.debug(retailer.toString()));
  }
  
  @Override
  @Transactional
  public Mono<RetailerDto> createRetailer(RetailerDto retailerDto) {
    log.debug("createRetailer");
    return Mono.from(validateRetailer(retailerDto))
      .map(validRetailer -> {
      Retailer retailerToSave = RetailerMapper.INSTANCE.dtoToEntity(validRetailer);
      retailerToSave.setCreationDate(OffsetDateTime.now());
      retailerToSave.setIsActive(true);
      return retailerToSave;
    })
      .flatMap(this.retailerRepository::save)
      .doOnNext(retailer -> log.debug(retailer.toString()))
      .map(RetailerMapper.INSTANCE::entityToDto)
      .doOnNext(savedRetailer ->
        this.cacheHelper.put("retailers", savedRetailer.getId(), savedRetailer));
  }
  
  private Mono<Category> getCategory(Long categoryId) {
    return this.categoryRepository
      .findByIdAndIsActiveTrue(categoryId)
      .doOnNext(category -> log.debug(category.toString()));
  }
  
  private Mono<SubCategory> getSubCategory(Long subCategoryId) {
    return this.subCategoryRepository
      .findByIdAndIsActiveTrue(subCategoryId)
      .doOnNext(subCategory -> log.debug(subCategory.toString()));
  }
  
  private Mono<Retailer> getRetailer(Long retailerId) {
    return this.retailerRepository
      .findByIdAndIsActiveIsTrue(retailerId)
      .doOnNext(retailer -> log.debug(retailer.toString()));
  }
  
  private Mono<ProductDto> validateProduct(ProductDto productDto) {
    return Mono.just(productDto).filter(product -> isValidName(product.getName())).switchIfEmpty(Mono.error(new ValidationException("The product name is not valid")));
  }
  
  private Mono<CategoryDto> validateCategory(CategoryDto categoryDto) {
    return Mono.just(categoryDto).filter(category -> isValidName(category.getName())).switchIfEmpty(Mono.error(new ValidationException("The category name is not valid")));
  }
  
  private Mono<SubCategoryDto> validateSubCategory(SubCategoryDto subCategoryDto){
    return Mono.just(subCategoryDto)
      .filter(subCategory -> isValidName(subCategory.getName()))
      .switchIfEmpty(Mono.error(new ValidationException("The category name is not valid")));
  }
  
  private Mono<RetailerDto> validateRetailer(RetailerDto retailerDto){
    return Mono.just(retailerDto)
      .filter(retailer -> (retailer != null &&
        InputValidator.isValidName(retailer.getName()) &&
        InputValidator.isValidEmail(retailer.getEmail()) &&
        InputValidator.isValidName(retailer.getAddress())
      ))
      .switchIfEmpty(Mono.error(new ValidationException("Invalid retailer property.")));
  }
  
  private Mono<Category> getEmptyCategory(Long categoryId){
    return Mono.just(Category.builder()
      .id(categoryId)
      .name(StringUtils.EMPTY)
      .build());
  }
  
  private Mono<SubCategory> getEmptySubCategory(Long subCategoryId){
    return Mono.just(SubCategory.builder()
      .id(subCategoryId)
      .categoryId(null)
      .name(StringUtils.EMPTY)
      .build());
  }
  
  private Mono<Retailer> getEmptyRetailer(Long retailerId){
    return Mono.just(Retailer.builder()
      .id(retailerId)
      .name(StringUtils.EMPTY)
      .build());
  }
  
}
