package com.authorizer.port;

import com.authorizer.domain.model.Product;
import java.util.List;

/**
 * The interface is an inbound port provides the flow and the application functionality to the outside
 */
public interface ProductService {

    List<Product> getProducts();

    Product getProductById(Integer productId);

    Product addProduct(Product product);

    Product removeProduct(Integer productId);
}
