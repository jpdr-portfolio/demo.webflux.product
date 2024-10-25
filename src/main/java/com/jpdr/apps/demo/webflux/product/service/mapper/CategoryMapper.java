package com.jpdr.apps.demo.webflux.product.service.mapper;

import com.jpdr.apps.demo.webflux.product.model.Category;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = java.util.Objects.class)
public interface CategoryMapper {
  
  CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
  
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "isActive", expression = "java(null)")
  @Mapping(target = "creationDate", expression = "java(null)")
  @Mapping(target = "deletionDate", expression = "java(null)")
  Category dtoToEntity(CategoryDto dto);
  
  @Mapping(target = "creationDate", expression = "java(Objects.toString(entity.getCreationDate(),null))" )
  @Mapping(target = "deletionDate", expression = "java(Objects.toString(entity.getDeletionDate(),null))" )
  CategoryDto entityToDto(Category entity);
  
}
