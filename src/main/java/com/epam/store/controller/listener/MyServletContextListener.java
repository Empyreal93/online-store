package com.epam.store.controller.listener;

import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.service.Authenticator;
import com.epam.store.service.ProductService;
import com.epam.store.service.RegistrationService;
import com.epam.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(MyServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent arg) {
        ServletContext servletContext = arg.getServletContext();
        ConnectionPool connectionPool = new SqlConnectionPool();
        SqlPooledConnection connection = connectionPool.getConnection();
        DBMetadataManager dbMetadataManager = new DBMetadataManager(connection.getMetaData());
        SqlQueryGenerator sqlQueryGenerator = new SqlQueryGenerator(dbMetadataManager);
        DaoFactory daoFactory = new JdbcDaoFactory(connectionPool, sqlQueryGenerator);

        //set services to servlet context, the class name is used as a attribute name
        servletContext.setAttribute(ProductService.class.getSimpleName(), new ProductService(daoFactory, sqlQueryGenerator));
        servletContext.setAttribute(UserService.class.getSimpleName(), new UserService(daoFactory));
        servletContext.setAttribute(RegistrationService.class.getSimpleName(), new RegistrationService(daoFactory));
        servletContext.setAttribute(Authenticator.class.getSimpleName(), new Authenticator(daoFactory));
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
        log.info("contextDestroyed...");
    }
}