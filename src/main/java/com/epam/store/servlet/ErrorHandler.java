package com.epam.store.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/ErrorHandler")
public class ErrorHandler extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);
    private static final String ERROR_PAGE = "WEB-INF/jsp/error.jsp";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context(req, resp);
        Throwable throwable = (Throwable) context.getAttribute("javax.servlet.error.exception", Scope.REQUEST);
        Integer statusCode = (Integer) context.getAttribute("javax.servlet.error.status_code", Scope.REQUEST);
        String requestUri = (String) context.getAttribute("javax.servlet.error.request_uri", Scope.REQUEST);
        if(requestUri != null) {
            log.warn("Error when accessing: " + requestUri);
        }
        if(statusCode != null) {
            log.warn("Error status code:" + statusCode);
            context.setAttribute("statusCode", statusCode, Scope.REQUEST);
        }
        if(throwable != null) {
            log.warn("Handled exception:", throwable);
        }
        context.forward(ERROR_PAGE);
    }
}
