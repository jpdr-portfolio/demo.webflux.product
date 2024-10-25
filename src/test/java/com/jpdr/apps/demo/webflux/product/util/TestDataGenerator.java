package com.jpdr.apps.demo.webflux.product.util;

import com.jpdr.apps.demo.webflux.product.model.Category;
import com.jpdr.apps.demo.webflux.product.model.Product;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.mapper.CategoryMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.ProductMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestDataGenerator {
  
  private static final String PRODUCT_NAME = "Product Name";
  private static final String CATEGORY_NAME = "Category Name";
  private static final String RETAILER_NAME = "Retailer Name";
  public static final String CREATION_DATE = "2024-10-14T10:39:45.732446-03:00";
  public static final String ADDRESS = "123 Street, City, State";
  public static final String SECTOR_NAME = "Sector Name";
  public static final String EMAIL = "johnsmith@mail.com";
  
  public static ProductDto getNewProductDto(){
    ProductDto productDto = getProductDto();
    productDto.setCategoryId(1);
    productDto.setCategoryName(null);
    productDto.setRetailerId(1);
    productDto.setRetailerName(null);
    productDto.setCreationDate(null);
    productDto.setIsActive(null);
    return productDto;
  }
  
  public static List<ProductDto> getProductDtos(){
    return getList(TestDataGenerator::getProductDto);
  }
  
  public static ProductDto getProductDto(){
    return getProductDto(1);
  }
  
  
  public static ProductDto getProductDto(int productId){
    ProductDto productDto = ProductMapper.INSTANCE.entityToDto(getProduct(productId));
    productDto.setCategoryName(CATEGORY_NAME);
    productDto.setRetailerName(RETAILER_NAME);
    return productDto;
  }
  
  public static List<Product> getProducts(){
    return getList(TestDataGenerator::getProduct);
  }
  
  public static Product getProduct(){
    return getProduct(1);
  }
  
  public static Product getProduct(int productId){
    return Product.builder()
      .id(productId)
      .name(PRODUCT_NAME)
      .categoryId(1)
      .retailerId(1)
      .isActive(true)
      .creationDate(OffsetDateTime.parse(CREATION_DATE))
      .deletionDate(null)
      .build();
  }
  
  
  
  
  public static CategoryDto getNewCategoryDto(){
    return CategoryDto.builder()
      .id(null)
      .name(CATEGORY_NAME)
      .isActive(null)
      .creationDate(null)
      .deletionDate(null)
      .build();
  }
  
  
  public static List<CategoryDto> getCategoryDtos(){
    return getList(TestDataGenerator::getCategoryDto);
  }
  
  
  public static CategoryDto getCategoryDto(){
    return CategoryMapper.INSTANCE.entityToDto(getCategory(1));
  }
  
  
  public static CategoryDto getCategoryDto(int categoryId){
    return CategoryMapper.INSTANCE.entityToDto(getCategory(categoryId));
  }
  
  public static List<Category> getCategories(){
    return getList(TestDataGenerator::getCategory);
  }
  
  public static Category getCategory(){
    return getCategory(1);
  }
  
  public static Category getCategory(int categoryId){
    return Category.builder()
      .id(categoryId)
      .name(CATEGORY_NAME)
      .isActive(true)
      .creationDate(OffsetDateTime.parse(CREATION_DATE))
      .deletionDate(null)
      .build();
  }
  
  public static RetailerDto getRetailer(){
    return getRetailer(1);
  }
  
  public static RetailerDto getRetailer(int retailerId){
    return RetailerDto.builder()
      .id(retailerId)
      .name(RETAILER_NAME)
      .email(EMAIL)
      .address(ADDRESS)
      .sectorId(1)
      .sectorName(SECTOR_NAME)
      .isActive(true)
      .creationDate(CREATION_DATE)
      .deletionDate(null)
      .build();
  }
  
  private static <R> List<R> getList(Function<Integer,R> function){
    return Stream.iterate(1, n -> n + 1)
      .limit(3)
      .map(function)
      .toList();
  }
  
}
