package com.epam.store.service;

import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Attribute;
import com.epam.store.model.Category;
import com.epam.store.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductService {
    private DaoFactory daoFactory;
    private SqlQueryGenerator sqlQueryGenerator;
    private AttributeService attributeService;

    public ProductService(DaoFactory daoFactory, SqlQueryGenerator sqlQueryGenerator) {
        this.sqlQueryGenerator = sqlQueryGenerator;
        this.daoFactory = daoFactory;
        attributeService = new AttributeService(daoFactory, sqlQueryGenerator);
    }

    public List<Product> getProductsForCategory(String category) {
        List<Product> productsList = new ArrayList<>();
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Category> categoryDao = daoSession.getDao(Category.class);
            List<Category> categories = categoryDao.findByParameter("name", category);
            if(categories.size() == 1) {
                long categoryID = categories.get(0).getId();
                Dao<Product> productDao = daoSession.getDao(Product.class);
                productsList = productDao.findByParameter("Category_ID", categoryID);
                try {
                    for (Product product : productsList) {
                        List<Attribute> attributesForProduct = attributeService.getAttributesForProduct(product.getId());
                        product.setAttributes(attributesForProduct);
                    }
                } catch (SQLException e) {
                    throw new ServiceException(e);
                }
            }
        }
        return productsList;
    }

    public void addProduct(Product product) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Product> productDao = daoSession.getDao(Product.class);
            productDao.insert(product);
            attributeService.insertAttributes(product.getId(), product.getAttributes());
        }
    }
}
