package main;

import api.GetServlet;
import api.PutServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Application {

    private static final int DEFAULT_PORT = 8000;
    private static final String PORT = "PORT";
    private static final String DELIMITER = "/";
    private static final String PUT_API = "/put";
    private static final String GET_API = "/get";

    private static int getPort() {
        final String port = System.getenv().get(PORT);
        if (port == null) return DEFAULT_PORT;
        return Integer.parseInt(port);
    }

    public static void main(final String[] args) throws Exception {
        final Server server = new Server(getPort());

        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath(DELIMITER);
        servletContextHandler.addServlet(new ServletHolder(new PutServlet()), PUT_API);
        servletContextHandler.addServlet(new ServletHolder(new GetServlet()), GET_API);

        final GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);
        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { servletContextHandler, new DefaultHandler() });
        gzip.setHandler(handlers);

        System.out.println("Server starting at " + getPort());
        server.start();
        server.join();
    }
}

