package com.epam.store;

import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.Attribute;
import com.epam.store.model.Price;
import com.epam.store.model.Product;
import com.epam.store.model.StringAttribute;
import com.epam.store.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws SQLException {
        ConnectionPool connectionPool = new SqlConnectionPool();
        SqlPooledConnection connection = connectionPool.getConnection();
        DBMetadataManager dbMetadataManager = new DBMetadataManager(connection.getMetaData());
        SqlQueryManager sqlQueryManager = new SqlQueryManager(dbMetadataManager);

        DaoFactory daoFactory = new JdbcDaoFactory(connectionPool, sqlQueryManager);
        DaoSession daoSession = daoFactory.getDaoSession();

        Product bread = new Product();
        bread.setName("Pitt's bread");
        bread.setCategory("Food");
        bread.setPrice(new Price(new BigDecimal("43")));

        List<Attribute> attributeList = new ArrayList<>();
        StringAttribute stringAttribute = new StringAttribute("high");
        stringAttribute.setName("damage resistance");
        attributeList.add(stringAttribute);

        bread.setAttributes(attributeList);

        ProductService productService = new ProductService(daoFactory, sqlQueryManager);
        productService.addProduct(bread);

        List<Product> food = productService.getProductsForCategory("food");
        for (Product product : food) {
            System.out.println(product);
        }
    }
}
