package com.jpdr.apps.demo.webflux.product.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import com.jpdr.apps.demo.webflux.product.util.DtoSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@EnableCaching
@Configuration
public class CacheConfig {
  
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper){
    
    ObjectMapper mapper = objectMapper.copy()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    mapper.findAndRegisterModules();
    
    DtoSerializer<ProductDto> productDtoDtoSerializer = new DtoSerializer<>(mapper, ProductDto.class);
    DtoSerializer<CategoryDto> categoryDtoDtoSerializer = new DtoSerializer<>(mapper, CategoryDto.class);
    DtoSerializer<RetailerDto> retailerDtoDtoSerializer = new DtoSerializer<>(mapper, RetailerDto.class);
    
    RedisSerializationContext.SerializationPair<ProductDto> productDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(productDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<CategoryDto> categoryDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(categoryDtoDtoSerializer);
    RedisSerializationContext.SerializationPair<RetailerDto> retailerDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(retailerDtoDtoSerializer);
    
    RedisCacheConfiguration productCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(productDtoSerializationPair);
    RedisCacheConfiguration categoryCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(categoryDtoSerializationPair);
    RedisCacheConfiguration retailerCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(retailerDtoSerializationPair);
    
    return RedisCacheManager.builder(redisConnectionFactory)
      .withCacheConfiguration("products",productCacheConfiguration)
      .withCacheConfiguration("categories", categoryCacheConfiguration)
      .withCacheConfiguration("retailers", retailerCacheConfiguration)
      .build();
  }

}
