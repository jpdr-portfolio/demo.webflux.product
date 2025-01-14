package com.jpdr.apps.demo.webflux.product.service;

import com.jpdr.apps.demo.webflux.commons.caching.CacheHelper;
import com.jpdr.apps.demo.webflux.product.exception.CategoryNotFoundException;
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
import com.jpdr.apps.demo.webflux.product.service.dto.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
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
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewRetailerDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewSubCategoryDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getProduct;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getProducts;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getRetailer;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getRetailers;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getSubCategories;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getSubCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
  private SubCategoryRepository subCategoryRepository;
  @Mock
  private RetailerRepository retailerRepository;
  @Mock
  private CacheHelper cacheHelper;
  private Category category;
  private SubCategory subCategory;
  private Retailer retailer;
  
  @BeforeEach
  void setupEach(){
    category = getCategory();
    subCategory = getSubCategory();
    retailer = getRetailer();
    when(categoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(category));
    when(subCategoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(subCategory));
    when(retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.just(retailer));
  }
  
  
  @Test
  @DisplayName("OK - Find All Products")
  void givenProductsWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Long, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedProducts));
    
    StepVerifier.create(appService.findAllProducts(null,null, null))
      .assertNext(receivedProducts ->{
        for(ProductDto receivedProduct : receivedProducts){
          assertProduct(expectedProductsMap.get(receivedProduct.getId()),
            receivedProduct, category.getName(), subCategory.getName(), retailer.getName());
        }
      })
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find All Products - Category Not found")
  void givenCategoryNotFoundWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Long, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedProducts));
    when(categoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(null,null, null))
      .assertNext(receivedProducts ->{
        for(ProductDto receivedProduct : receivedProducts){
          assertProduct(expectedProductsMap.get(receivedProduct.getId()),
            receivedProduct, StringUtils.EMPTY, subCategory.getName(), retailer.getName());
        }
      })
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find All Products - Sub-Category Not found")
  void givenSubCategoryNotFoundWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Long, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedProducts));
    when(subCategoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(null,null, null))
      .assertNext(receivedProducts ->{
        for(ProductDto receivedProduct : receivedProducts){
          assertProduct(expectedProductsMap.get(receivedProduct.getId()),
            receivedProduct, category.getName(), StringUtils.EMPTY, retailer.getName());
        }
      })
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find All Products - Retailer Not Found")
  void givenRetailerNotFoundWhenFindAllProductsThenReturnProducts(){
    
    List<Product> expectedProducts = getProducts();
    Map<Long, Product> expectedProductsMap = expectedProducts.stream()
      .collect(Collectors.toMap(Product::getId, Function.identity()));
    
    when(productRepository.findAllByIsActiveTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedProducts));
    when(retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(null,null, null))
      .assertNext(receivedProducts ->{
        for(ProductDto receivedProduct : receivedProducts){
          assertProduct(expectedProductsMap.get(receivedProduct.getId()),
            receivedProduct, category.getName(), subCategory.getName(), StringUtils.EMPTY);
        }
      })
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Product By Id")
  void givenIdWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(expectedProduct));
    
    StepVerifier.create(appService.findProductById(1L))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), subCategory.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Product By Id - Category not found")
  void givenCategoryNotFoundWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(expectedProduct));
    when(categoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findProductById(1L))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, StringUtils.EMPTY, subCategory.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Product By Id - Sub-Category not found")
  void givenSubCategoryNotFoundWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(expectedProduct));
    when(subCategoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findProductById(1L))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), StringUtils.EMPTY, retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Find Product By Id - Retailer not found")
  void givenRetailerNotFoundWhenFindProductByIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(expectedProduct));
    when(retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findProductById(1L))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), subCategory.getName(), StringUtils.EMPTY))
      .expectComplete()
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Find Product By Category Id")
  void givenCategoryIdWhenFindProductByCategoryIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByCategoryIdAndIsActiveTrueOrderById(anyLong()))
      .thenReturn(Flux.just(expectedProduct));
    
    StepVerifier.create(appService.findAllProducts(1L, null, null))
      .assertNext(receivedProducts -> assertProduct(expectedProduct,
        receivedProducts.getFirst(), category.getName(), subCategory.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Product By Category Id - Category Not Found")
  void givenCategoryNotFoundWhenFindProductByCategoryIdThenReturnError(){
    
    when(categoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(1L, null, null))
      .expectError(CategoryNotFoundException.class)
      .verify();
    
  }
  
 @Test
  @DisplayName("OK - Find Product By Sub Category Id")
  void givenSubCategoryIdWhenFindProductBySubCategoryIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findBySubCategoryIdAndIsActiveTrueOrderById(anyLong()))
      .thenReturn(Flux.just(expectedProduct));
    
    StepVerifier.create(appService.findAllProducts(null,1L, null))
      .assertNext(receivedProducts -> assertProduct(expectedProduct,
        receivedProducts.getFirst(), category.getName(), subCategory.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Product By Sub Category Id - Sub Category Not Found")
  void givenSubCategoryNotFoundWhenFindProductByCategoryIdThenReturnError(){
    
    when(subCategoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(null, 1L, null))
      .expectError(SubCategoryNotFoundException.class)
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Find Product By Retailer Id")
  void givenRetailerIdWhenFindProductByRetailerIdThenReturnProduct(){
    
    Product expectedProduct = getProduct();
    
    when(productRepository.findByRetailerIdAndIsActiveTrueOrderById(anyLong()))
      .thenReturn(Flux.just(expectedProduct));
    
    StepVerifier.create(appService.findAllProducts(null, null, 1L))
      .assertNext(receivedProducts -> assertProduct(expectedProduct,
        receivedProducts.getFirst(), category.getName(), subCategory.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Find Product By Retailer Id - Retailer Not Found")
  void givenRetailerNotFoundWhenFindProductByRetailerIdThenReturnError(){
    
    when(retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findAllProducts(null, null, 1L))
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
        savedProduct.setId(1L);
        return Mono.just(savedProduct);
      });
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .assertNext(receivedProduct -> assertProduct(expectedProduct,
        receivedProduct, category.getName(), subCategory.getName(), retailer.getName()))
      .expectComplete()
      .verify();
    
  }
  
  
  @Test
  @DisplayName("Error - Create Product - Category Not Found")
  void givenCategoryNotFoundWhenCreateProductThenReturnError(){
    
    ProductDto requestProduct = getNewProductDto();
    when(categoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .expectError(CategoryNotFoundException.class)
      .verify();
    
  }
  
  @Test
  @DisplayName("Error - Create Product - Sub-Category Not Found")
  void givenSubCategoryNotFoundWhenCreateProductThenReturnError(){
    
    ProductDto requestProduct = getNewProductDto();
    when(subCategoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .expectError(SubCategoryNotFoundException.class)
      .verify();
    
  }
  
  
  
  @Test
  @DisplayName("Error - Create Product - Retailer Not Found")
  void givenRetailerNotFoundWhenCreateProductThenReturnError(){
    
    ProductDto requestProduct = getNewProductDto();
    
    when(retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.createProduct(requestProduct))
      .expectError(RetailerNotFoundException.class)
      .verify();
    
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Find All Categories")
  void givenCategoriesWhenFindAllCategoriesThenReturnCategories(){
    List<Category> expectedCategories = getCategories();
    
    Map<Long, Category> expectedCategoriesMap = expectedCategories.stream()
      .collect(Collectors.toMap(Category::getId, Function.identity()));
    
    when(categoryRepository.findAllByIsActiveTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedCategories));
    
    StepVerifier.create(appService.findAllCategories())
      .assertNext(receivedCategories ->{
        for(CategoryDto receivedCategory : receivedCategories){
          assertCategory(expectedCategoriesMap.get(receivedCategory.getId()),
            receivedCategory);
        }
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("OK - Find Category By Id")
  void givenCategoryWhenFindCategoryByIdThenReturnCategory(){
    Category expectedCategory = getCategory();
    
    when(categoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(expectedCategory));
    
    StepVerifier.create(appService.findCategoryById(1L))
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
        savedCategory.setId(1L);
        return Mono.just(savedCategory);
      });
    
    StepVerifier.create(appService.createCategory(requestCategory))
      .assertNext(receivedCategory -> assertCategory(expectedCategory,
          receivedCategory))
      .expectComplete()
      .verify();
    
  }
  
  
  
  
  
   @Test
  @DisplayName("OK - Find All Sub-Categories")
  void givenSubCategoriesWhenFindAllSubCategoriesThenReturnSubCategories(){
    List<SubCategory> expectedSubCategories = getSubCategories();
    
    Map<Long, SubCategory> expectedCategoriesMap = expectedSubCategories.stream()
      .collect(Collectors.toMap(SubCategory::getId, Function.identity()));
    
    when(subCategoryRepository.findAllByIsActiveTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedSubCategories));
    
    StepVerifier.create(appService.findAllSubCategories())
      .assertNext(receivedSubCategories ->{
        for(SubCategoryDto receivedSubCategory : receivedSubCategories){
          assertSubCategory(expectedCategoriesMap.get(receivedSubCategory.getId()),
            receivedSubCategory);
        }
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("OK - Find Sub-Category By Id")
  void givenSubCategoryWhenFindSubCategoryByIdThenReturnSubCategory(){
    SubCategory expectedSubCategory = getSubCategory();
    
    when(subCategoryRepository.findByIdAndIsActiveTrue(anyLong()))
      .thenReturn(Mono.just(expectedSubCategory));
    
    StepVerifier.create(appService.findSubCategoryById(1L))
      .assertNext(receivedSubCategory -> assertSubCategory(subCategory,
        receivedSubCategory))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Sub-Category")
  void givenSubCategoryWhenCreateSubCategoryThenReturnSubCategory(){
    
    SubCategoryDto requestSubCategory = getNewSubCategoryDto();
    SubCategory expectedSubCategory = getSubCategory();
    
    when(subCategoryRepository.save(any(SubCategory.class)))
      .thenAnswer(i -> {
        SubCategory savedSubCategory = i.getArgument(0);
        savedSubCategory.setId(1L);
        return Mono.just(savedSubCategory);
      });
    
    StepVerifier.create(appService.createSubCategory(requestSubCategory))
      .assertNext(receivedSubCategory -> assertSubCategory(expectedSubCategory,
          receivedSubCategory))
      .expectComplete()
      .verify();
    
  }
  
  
  
  @Test
  @DisplayName("OK - Find All Retailers")
  void givenRetailersWhenFindRetailersThenReturnRetailers(){
    
    List<Retailer> expectedRetailers = getRetailers();
    Map<Long, Retailer> expectedRetailersMap = expectedRetailers.stream()
      .collect(Collectors.toMap(Retailer::getId, Function.identity()));
    
    when(retailerRepository.findAllByIsActiveIsTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedRetailers));
    
    StepVerifier.create(appService.findAllRetailers())
      .assertNext(receivedRetailers -> {
        for(RetailerDto receivedRetailer : receivedRetailers){
          assertRetailer(expectedRetailersMap.get(receivedRetailer.getId()),
            receivedRetailer);
        }
      })
      .expectComplete()
      .verify();
    
  }
  
  
  @Test
  @DisplayName("OK - Find Retailer By Id")
  void givenIdWhenFindRetailerByIdThenReturnRetailer(){
    
    Retailer expectedRetailer = getRetailer();
    
    when(this.retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.just(expectedRetailer));
    
    StepVerifier.create(appService.findRetailerById(1L))
      .assertNext(receivedRetailer -> assertRetailer(expectedRetailer, receivedRetailer))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("Error - Find By Retailer Id - Not Found")
  void givenNotFoundIdWhenFindRetailerByIdThenReturnError(){
    
    when(this.retailerRepository.findByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.findRetailerById(1L))
      .expectError(RetailerNotFoundException.class)
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Create Retailer")
  void givenRetailerWhenCreateRetailerThenReturnRetailer(){
    
    RetailerDto requestRetailer = getNewRetailerDto();
    Retailer expectedRetailer = getRetailer();
    
    when(this.retailerRepository.save(any(Retailer.class)))
      .thenReturn(Mono.just(expectedRetailer));
    
    StepVerifier.create(appService.createRetailer(requestRetailer))
      .assertNext(receivedRetailer -> {
        assertNotNull(receivedRetailer.getId());
        assertEquals(expectedRetailer.getName(), receivedRetailer.getName());
        assertEquals(expectedRetailer.getEmail(), receivedRetailer.getEmail());
        assertEquals(expectedRetailer.getAddress(), receivedRetailer.getAddress());
        assertTrue(receivedRetailer.getIsActive());
        assertNotNull(receivedRetailer.getCreationDate());
        assertTrue(StringUtils.isBlank(receivedRetailer.getDeletionDate()));
      })
      .expectComplete()
      .verify();
    
  }
  
  
  
  
  
  private static void assertProduct(Product entity, ProductDto dto, String categoryName,
    String subCategoryName, String retailerName){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(categoryName, dto.getCategoryName());
    assertEquals(subCategoryName, dto.getSubCategoryName());
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
  
  private static void assertSubCategory(SubCategory entity, SubCategoryDto dto){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(entity.getCategoryId(), dto.getCategoryId());
    assertNotNull(dto.getCreationDate());
    assertTrue(dto.getIsActive());
    assertNull(dto.getDeletionDate());
  }
  
  private static void assertRetailer(Retailer entity, RetailerDto dto){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertNotNull(dto.getCreationDate());
    assertTrue(dto.getIsActive());
    assertNull(dto.getDeletionDate());
  }

}
