package com.jpdr.apps.demo.webflux.product.util;

import com.jpdr.apps.demo.webflux.product.model.Category;
import com.jpdr.apps.demo.webflux.product.model.Product;
import com.jpdr.apps.demo.webflux.product.model.Retailer;
import com.jpdr.apps.demo.webflux.product.model.SubCategory;
import com.jpdr.apps.demo.webflux.product.service.dto.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
import com.jpdr.apps.demo.webflux.product.service.mapper.CategoryMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.ProductMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.RetailerMapper;
import com.jpdr.apps.demo.webflux.product.service.mapper.SubCategoryMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestDataGenerator {
  
  private static final String PRODUCT_NAME = "Product Name";
  private static final String CATEGORY_NAME = "Category Name";
  private static final String SUB_CATEGORY_NAME = "Sub-Category Name";
  private static final String RETAILER_NAME = "Retailer Name";
  public static final String CREATION_DATE = "2024-10-14T10:39:45.732446-03:00";
  public static final String ADDRESS = "123 Street, City, State";
  public static final String EMAIL = "johnsmith@mail.com";
  public static final String CITY = "Great City";
  public static final String COUNTRY = "Good Country";
  
  public static ProductDto getNewProductDto(){
    ProductDto productDto = getProductDto();
    productDto.setCategoryId(1L);
    productDto.setCategoryName(null);
    productDto.setSubCategoryId(1L);
    productDto.setRetailerId(1L);
    productDto.setRetailerName(null);
    productDto.setCreationDate(null);
    productDto.setIsActive(null);
    return productDto;
  }
  
  public static List<ProductDto> getProductDtos(){
    return getList(TestDataGenerator::getProductDto);
  }
  
  public static ProductDto getProductDto(){
    return getProductDto(1L);
  }
  
  
  public static ProductDto getProductDto(long productId){
    ProductDto productDto = ProductMapper.INSTANCE.entityToDto(getProduct(productId));
    productDto.setCategoryName(CATEGORY_NAME);
    productDto.setSubCategoryName(SUB_CATEGORY_NAME);
    productDto.setRetailerName(RETAILER_NAME);
    return productDto;
  }
  
  public static List<Product> getProducts(){
    return getList(TestDataGenerator::getProduct);
  }
  
  public static Product getProduct(){
    return getProduct(1L);
  }
  
  public static Product getProduct(long productId){
    return Product.builder()
      .id(productId)
      .productName(PRODUCT_NAME)
      .categoryId(1L)
      .subCategoryId(1L)
      .retailerId(1L)
      .isActive(true)
      .creationDate(OffsetDateTime.parse(CREATION_DATE))
      .deletionDate(null)
      .build();
  }
  
  
  
  
  public static CategoryDto getNewCategoryDto(){
    return CategoryDto.builder()
      .id(null)
      .categoryName(CATEGORY_NAME)
      .isActive(null)
      .creationDate(null)
      .deletionDate(null)
      .build();
  }
  
    public static SubCategoryDto getNewSubCategoryDto(){
    return SubCategoryDto.builder()
      .id(null)
      .categoryId(1L)
      .subCategoryName(SUB_CATEGORY_NAME)
      .isActive(null)
      .creationDate(null)
      .deletionDate(null)
      .build();
  }

  public static RetailerDto getNewRetailerDto(){
    return RetailerDto.builder()
      .id(null)
      .retailerName(RETAILER_NAME)
      .retailerAddress(ADDRESS)
      .retailerEmail(EMAIL)
      .retailerCity(CITY)
      .retailerCountry(COUNTRY)
      .isActive(null)
      .creationDate(null)
      .deletionDate(null)
      .build();
  }
  
  
  public static List<CategoryDto> getCategoryDtos(){
    return getList(TestDataGenerator::getCategoryDto);
  }
  
  
  public static CategoryDto getCategoryDto(){
    return CategoryMapper.INSTANCE.entityToDto(getCategory(1L));
  }
  
  
  public static CategoryDto getCategoryDto(long categoryId){
    return CategoryMapper.INSTANCE.entityToDto(getCategory(categoryId));
  }
  
  public static List<Category> getCategories(){
    return getList(TestDataGenerator::getCategory);
  }
  
  public static Category getCategory(){
    return getCategory(1L);
  }
  
  public static Category getCategory(long categoryId){
    return Category.builder()
      .id(categoryId)
      .categoryName(CATEGORY_NAME)
      .isActive(true)
      .creationDate(OffsetDateTime.parse(CREATION_DATE))
      .deletionDate(null)
      .build();
  }
  
  
    public static List<SubCategoryDto> getSubCategoryDtos(){
    return getList(TestDataGenerator::getSubCategoryDto);
  }
  
  public static SubCategoryDto getSubCategoryDto(){
    return SubCategoryMapper.INSTANCE.entityToDto(getSubCategory(1L));
  }
  
  
  public static SubCategoryDto getSubCategoryDto(long subCategoryId){
    return SubCategoryMapper.INSTANCE.entityToDto(getSubCategory(subCategoryId));
  }
  
  
  
  
  
  
  public static List<SubCategory> getSubCategories(){
    return getList(TestDataGenerator::getSubCategory);
  }
  
  public static SubCategory getSubCategory(){
    return getSubCategory(1L);
  }
  
  public static SubCategory getSubCategory(long subCategoryId){
      return SubCategory.builder()
        .id(subCategoryId)
        .categoryId(1L)
        .subCategoryName(SUB_CATEGORY_NAME)
        .isActive(true)
        .creationDate(OffsetDateTime.parse(CREATION_DATE))
        .deletionDate(null)
        .build();
  }
  
  
  
    public static List<RetailerDto> getRetailerDtos(){
    return Stream.iterate(1, n -> n+1)
      .limit(3)
      .map(TestDataGenerator::getRetailerDto)
      .toList();
  }
  
  public static RetailerDto getRetailerDto(){
    return RetailerMapper.INSTANCE.entityToDto(getRetailer(1));
  }
  
  public static RetailerDto getRetailerDto(int i){
    return RetailerMapper.INSTANCE.entityToDto(getRetailer(i));
  }
  
  
  
  
  public static List<Retailer> getRetailers(){
    return Stream.iterate(1, n -> n+1)
      .limit(3)
      .map(TestDataGenerator::getRetailer)
      .toList();
  }
  
  public static Retailer getRetailer(){
    return getRetailer(1L);
  }
  
  public static Retailer getRetailer(long retailerId){
    return Retailer.builder()
      .id(retailerId)
      .retailerName(RETAILER_NAME)
      .retailerEmail(EMAIL)
      .retailerAddress(ADDRESS)
      .retailerCity(CITY)
      .retailerCountry(COUNTRY)
      .isActive(true)
      .creationDate(OffsetDateTime.parse(CREATION_DATE))
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
