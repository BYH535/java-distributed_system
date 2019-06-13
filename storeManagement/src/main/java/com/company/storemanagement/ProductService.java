
package com.company.storemanagement;

import com.company.storemanagement.models.Product;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Remote;

@Remote
@LocalBean
public interface ProductService {
    public List<Product> findAllProduct();
    public String performOrder(int productCode, int productQuantityOrdered);
    public String acquiringProduct(int productCode, int quantityAquired);
    public List<Integer> getProductsCode();
}
