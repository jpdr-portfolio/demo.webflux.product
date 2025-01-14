package com.jpdr.apps.demo.webflux.product.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@Table("product")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
  
  @Id
  @Column("id")
  Long id;
  @Column("name")
  String name;
  @Column("category_id")
  Long categoryId;
  @Column("sub_category_id")
  Long subCategoryId;
  @Column("retailer_id")
  Long retailerId;
  @Column("is_active")
  Boolean isActive;
  @Column("creation_date")
  OffsetDateTime creationDate;
  @Column("deletion_date")
  OffsetDateTime deletionDate;
  
}
