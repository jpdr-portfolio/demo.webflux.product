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
@Table("category")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
  
  @Id
  @Column("id")
  Integer id;
  @Column("name")
  String name;
  @Column("is_active")
  Boolean isActive;
  @Column("creation_date")
  OffsetDateTime creationDate;
  @Column("deletion_date")
  OffsetDateTime deletionDate;
  
}
