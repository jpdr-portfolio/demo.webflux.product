package com.jpdr.apps.demo.webflux.product.service;

import com.jpdr.apps.demo.webflux.product.exception.product.CategoryNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.retailer.RetailerNotFoundException;
import com.jpdr.apps.demo.webflux.product.model.Category;
import com.jpdr.apps.demo.webflux.product.model.Product;
import com.jpdr.apps.demo.webflux.product.repository.product.CategoryRepository;
import com.jpdr.apps.demo.webflux.product.repository.product.ProductRepository;
import com.jpdr.apps.demo.webflux.product.repository.retailer.RetailerRepository;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.impl.AppServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getCategories;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getCategory;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewCategoryDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewProductDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getProduct;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getProducts;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getRetailer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AppServiceTest {
  
  @InjectMocks
  private AppServiceImpl appService;
  
  @Mock
  private ProductRepository productRepository;
  
  @Mock
  private CategoryRepository categoryRepository;
  
  @Mock
  private RetailerRepository retailerRepository;
  
  private Category category;
  
  private RetailerDto retailer;
  
  @BeforeEach
  void setupEach(){
    category = getCategory();
    retailer = getRetailer();
    when(categoryRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.just(category));
    when(retailerRepository.getRetailerById(anyInt()))
      .thenReturn(Mono.just(retailer));
  }
  
  
  @Test
  @DisplayName("OK - Find All Products")
  void givenProductsWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Integer, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrue())
      .thenReturn(Flux.fromIterable(expectedProducts));
    
    StepVerifier.create(appService.findAllProducts(null,null))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, category.getName(), retailer.getName()))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, category.getName(), retailer.getName()))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, category.getName(), retailer.getName()))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find All Products - Category Not found")
  void givenCategoryNotFoundWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Integer, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrue())
      .thenReturn(Flux.fromIterable(expectedProducts));
    when(categoryRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(null,null))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, StringUtils.EMPTY, retailer.getName()))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, StringUtils.EMPTY, retailer.getName()))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, StringUtils.EMPTY, retailer.getName()))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find All Products - Retailer Not Found")
  void givenRetailerNotFoundWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Integer, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrue())
      .thenReturn(Flux.fromIterable(expectedProducts));
    when(retailerRepository.getRetailerById(anyInt()))
      .thenReturn(Mono.error(new RetailerNotFoundException(1, new RuntimeException())));
    
    StepVerifier.create(appService.findAllProducts(null,null))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, category.getName(), StringUtils.EMPTY))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, category.getName(), StringUtils.EMPTY))
      .assertNext(receivedProduct -> assertProduct(expectedProductsMap.get(receivedProduct.getId()),
        receivedProduct, category.getName(), StringUtils.EMPTY))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Product By Id")
  void givenIdWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.just(expectedProduct));
    
    StepVerifier.create(appService.findProductById(1))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Product By Id - Category not found")
  void givenCategoryNotFoundWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.just(expectedProduct));
    when(categoryRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findProductById(1))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, StringUtils.EMPTY, retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Find Product By Id - Retailer not found")
  void givenRetailerNotFoundWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.just(expectedProduct));
    when(retailerRepository.getRetailerById(anyInt()))
      .thenReturn(Mono.error(new RetailerNotFoundException(1, new RuntimeException())));
    
    StepVerifier.create(appService.findProductById(1))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), StringUtils.EMPTY))
      .expectComplete()
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Find Product By Category Id")
  void givenCategoryIdWhenFindProductByCategoryIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByCategoryIdAndIsActiveTrue(anyInt()))
      .thenReturn(Flux.just(expectedProduct));
    
    StepVerifier.create(appService.findProductsByCategoryId(1))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Product By Category Id - Category Not Found")
  void givenCategoryNotFoundWhenFindProductByCategoryIdThenReturnError(){
    
    when(categoryRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findProductsByCategoryId(1))
      .expectError(CategoryNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Find Product By Retailer Id")
  void givenRetailerIdWhenFindProductByRetailerIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByRetailerIdAndIsActiveTrue(anyInt()))
      .thenReturn(Flux.just(expectedProduct));
    
    StepVerifier.create(appService.findProductsByRetailerId(1))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Product By Retailer Id - Retailer Not Found")
  void givenRetailerNotFoundWhenFindProductByRetailerIdThenReturnError(){
    
    when(retailerRepository.getRetailerById(anyInt()))
      .thenReturn(Mono.error(new RetailerNotFoundException(1, new RuntimeException())));
    
    StepVerifier.create(appService.findProductsByRetailerId(1))
      .expectError(RetailerNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Create Product")
  void givenProductWhenCreateProductThenReturnProduct(){
    
    ProductDto requestProduct = getNewProductDto();
    Product expectedProduct = getProduct();
    
    when(productRepository.save(any(Product.class)))
      .thenAnswer(i -> {
        Product savedProduct = i.getArgument(0);
        savedProduct.setId(1);
        return Mono.just(savedProduct);
      });
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  
  @Test
  @DisplayName("Error - Create Product - Category Not Found")
  void givenCategoryNotFoundWhenCreateProductThenReturnError(){
    
    ProductDto requestProduct = getNewProductDto();
    when(categoryRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .expectError(CategoryNotFoundException.class)
      .verify();
    
  }
  
  
  
  @Test
  @DisplayName("Error - Create Product - Retailer Not Found")
  void givenRetailerNotFoundWhenCreateProductThenReturnError(){
    
    ProductDto requestProduct = getNewProductDto();
    
    when(retailerRepository.getRetailerById(anyInt()))
      .thenReturn(Mono.error(new RetailerNotFoundException(1, new RuntimeException())));
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .expectError(RetailerNotFoundException.class)
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Find All Categories")
  void givenCategoriesWhenFindAllCategoriesThenReturnCategories(){
    List<Category> expectedCategories = getCategories();
    
    Map<Integer, Category> expectedCategoriesMap = expectedCategories.stream()
      .collect(Collectors.toMap(Category::getId, Function.identity()));
    
    when(categoryRepository.findAllByIsActiveTrue())
      .thenReturn(Flux.fromIterable(expectedCategories));
    
    StepVerifier.create(appService.findAllCategories())
      .assertNext(receivedCategory -> assertCategory(expectedCategoriesMap.get(receivedCategory.getId()),
        receivedCategory))
      .assertNext(receivedCategory -> assertCategory(expectedCategoriesMap.get(receivedCategory.getId()),
          receivedCategory))
      .assertNext(receivedCategory -> assertCategory(expectedCategoriesMap.get(receivedCategory.getId()),
            receivedCategory))
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("OK - Find Category By Id")
  void givenCategoryWhenFindCategoryByIdThenReturnCategory(){
    Category expectedCategory = getCategory();
    
    when(categoryRepository.findByIdAndIsActiveTrue(anyInt()))
      .thenReturn(Mono.just(expectedCategory));
    
    StepVerifier.create(appService.findCategoryById(1))
      .assertNext(receivedCategory -> assertCategory(category,
        receivedCategory))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Category")
  void givenCategoryWhenCreateCategoryThenReturnCategory(){
    
    CategoryDto requestCategory = getNewCategoryDto();
    Category expectedCategory = getCategory();
    
    when(categoryRepository.save(any(Category.class)))
      .thenAnswer(i -> {
        Category savedCategory = i.getArgument(0);
        savedCategory.setId(1);
        return Mono.just(savedCategory);
      });
    
    StepVerifier.create(appService.createCategory(requestCategory))
      .assertNext(receivedCategory -> assertCategory(expectedCategory,
          receivedCategory))
      .expectComplete()
      .verify();
    
  }
  
  
  
  private static void assertProduct(Product entity, ProductDto dto, String categoryName,
    String retailerName){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(categoryName, dto.getCategoryName());
    assertEquals(retailerName, dto.getRetailerName());
    assertNotNull(dto.getCreationDate());
    assertTrue(dto.getIsActive());
    assertNull(dto.getDeletionDate());
  }
  
  private static void assertCategory(Category entity, CategoryDto dto){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertNotNull(dto.getCreationDate());
    assertTrue(dto.getIsActive());
    assertNull(dto.getDeletionDate());
  }

}
