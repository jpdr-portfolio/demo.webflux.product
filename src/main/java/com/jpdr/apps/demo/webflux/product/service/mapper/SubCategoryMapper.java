package com.jpdr.apps.demo.webflux.product.service.mapper;

import com.jpdr.apps.demo.webflux.product.model.SubCategory;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = java.util.Objects.class)
public interface SubCategoryMapper {
  
  SubCategoryMapper INSTANCE = Mappers.getMapper(SubCategoryMapper.class);
  
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "isActive", expression = "java(null)")
  @Mapping(target = "creationDate", expression = "java(null)")
  @Mapping(target = "deletionDate", expression = "java(null)")
  SubCategory dtoToEntity(SubCategoryDto dto);
  
  @Mapping(target = "categoryName", expression = "java(null)")
  @Mapping(target = "creationDate", expression = "java(Objects.toString(entity.getCreationDate(),null))" )
  @Mapping(target = "deletionDate", expression = "java(Objects.toString(entity.getDeletionDate(),null))" )
  SubCategoryDto entityToDto(SubCategory entity);
  
}
