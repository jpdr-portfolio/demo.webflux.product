package com.jpdr.apps.demo.webflux.product.service.mapper;

import com.jpdr.apps.demo.webflux.product.model.Product;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = java.util.Objects.class)
public interface ProductMapper {
  
  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
  
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "isActive", expression = "java(null)")
  @Mapping(target = "creationDate", expression = "java(null)")
  @Mapping(target = "deletionDate", expression = "java(null)")
  Product dtoToEntity(ProductDto dto);
  
  @Mapping(target = "categoryId", expression = "java(null)")
  @Mapping(target = "categoryName", expression = "java(null)")
  @Mapping(target = "retailerId", expression = "java(null)")
  @Mapping(target = "retailerName", expression = "java(null)")
  @Mapping(target = "creationDate", expression = "java(Objects.toString(entity.getCreationDate(),null))" )
  @Mapping(target = "deletionDate", expression = "java(Objects.toString(entity.getDeletionDate(),null))" )
  ProductDto entityToDto(Product entity);
  
}
