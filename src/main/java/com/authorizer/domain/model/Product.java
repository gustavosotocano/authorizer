package com.authorizer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * The class defines the domain object model
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    private Integer productId;
    private String type;
    private String description;

}
