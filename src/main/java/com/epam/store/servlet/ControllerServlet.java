package com.epam.store.servlet;

import com.epam.store.action.Action;
import com.epam.store.action.ActionFactory;
import com.epam.store.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Controller", urlPatterns = "/controller/*")
public class ControllerServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ControllerServlet.class);
    private ActionFactory actionFactory;
    private Context context;

    @Override
    public void init() throws ServletException {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = new Context(req, resp);
        log.debug("Requested action: " + context.getRequestedAction());
        log.debug("current URI: " + context.getURI());
        log.debug("Referrer: " + req.getHeader("Referrer"));
        Action action = actionFactory.getAction(context);
        if (action == null) {
            log.debug("Action not found");
            context.forward("/ErrorHandler");
            return;
        }
        log.debug("Found action: " + action.getClass().getSimpleName());
        ActionResult result = action.execute(context);
        doForwardOrRedirect(result, req, resp);
    }

    private void doForwardOrRedirect(ActionResult result, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (result.isRedirect()) {
            String location = context.getContextPath() + result.getPageName();
            context.sendRedirect(location);
        } else {
            String path = String.format("/WEB-INF/jsp/" + result.getPageName() + ".jsp");
            log.debug("Requested path: " + path);
            context.forward(path);
        }
    }
}
