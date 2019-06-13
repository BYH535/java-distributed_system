package com.company.storemanagement;

import com.company.storemanagement.models.Product;
import java.util.Arrays;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@Stateless(name = "ProductService", mappedName = "ejb/ProductService")
@LocalBean
public class ProductServiceImpl implements ProductService {

    public Configuration initConfiguration() {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        return configuration;
    }

    public Session buildSession() {
        SessionFactory sf = this.initConfiguration().buildSessionFactory();
        return sf.openSession();
    }

    @Override
    public List<Product> findAllProduct() {
        try {
            Session s = this.buildSession();

            @SuppressWarnings("unchecked")
            List<Product> products = s.createQuery("from Products").list();

            s.close();

            return products;
        } catch (RuntimeException re) {
            List<Product> products = Arrays.asList(new Product());
            return products;
        }

    }

    @Override
    public String performOrder(int productCode, int quantityOrdered) {
        String res = "";
        Session s = this.buildSession();

        @SuppressWarnings("unchecked")
        List<Product> products
                = s.createQuery("from Product where productCode = " + productCode).list();

        if (!products.isEmpty()) {
            Product product = products.get(0);
            if (product.getProductQuantity() >= quantityOrdered) {
                product.setProductQuantity(product.getProductQuantity() - quantityOrdered);
                Transaction tr = s.beginTransaction();
                s.save(product);
                tr.commit();
                res = "success";
            } else if (product.getProductQuantity() < quantityOrdered) {
                res = "The ordered quantity is greater than the quantity in store !";
            }
        } else {
            res = "There is no product with the id " + productCode;
        }

        s.close();

        return res;
    }

    @Override
    public String acquiringProduct(int productCode, int quantityAquired) {
        String res = "";
        Session s = this.buildSession();

        @SuppressWarnings("unchecked")
        List<Product> products
                = s.createQuery("from Product where productCode = " + productCode).list();

        if (!products.isEmpty()) {
            Product product = products.get(0);
            product.setProductQuantity(product.getProductQuantity() + quantityAquired);
            Transaction tr = s.beginTransaction();
            s.save(product);
            tr.commit();
            res = "success";
        } else {
            res = "There is no product with the id " + productCode;
        }

        s.close();

        return res;
    }

    @Override
    public List<Integer> getProductsCode() {
        Session s = this.buildSession();

        @SuppressWarnings("unchecked")
        List<Integer> productsCode = s.createQuery("select productCode from Product").list();

        return productsCode;
    }

}
