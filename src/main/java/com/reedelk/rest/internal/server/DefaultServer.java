package com.reedelk.rest.internal.server;


import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.component.RestListener1Configuration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.internal.server.configurer.ServerConfigurer;
import com.reedelk.rest.internal.server.configurer.ServerSecurityConfigurer;
import com.reedelk.runtime.api.commons.StringUtils;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.tcp.TcpServer;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DefaultServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServer.class);

    private HttpServerRoutes routes;
    private DisposableServer server;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    private final RestListener1Configuration configuration;

    DefaultServer(RestListener1Configuration configuration) {
        this.configuration = configuration;

        this.routes = new DefaultServerRoutes();
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();

        TcpServer bootstrap = createTcpServer(configuration);
        HttpServer httpServer = HttpServer.from(bootstrap).handle(routes);
        this.server = ServerConfigurer.configure(httpServer, configuration).bindNow();
    }

    @Override
    public void addRoute(String path, RestMethod method, Response response, ErrorResponse errorResponse, OperationObject operationObject, HttpRequestHandler httpHandler) {
        requireNonNull(httpHandler, "httpHandler");
        requireNonNull(method, "method");

        String realPath = getRealPath(path);

        method.addRoute(routes, realPath, httpHandler);
    }

    @Override
    public void removeRoute(String path, RestMethod method) {
        requireNonNull(method, "method");

        String realPath = getRealPath(path);

        routes.remove(HttpMethod.valueOf(method.name()), realPath);
    }

    @Override
    public String getBasePath() {
        return configuration.getBasePath();
    }

    @Override
    public void stop() {
        shutdownGracefully(bossGroup);
        shutdownGracefully(workerGroup);
        shutdownSilently(server);
    }

    @Override
    public boolean hasEmptyRoutes() {
        return routes.handlers().isEmpty();
    }

    @Override
    public List<HttpRouteHandler> handlers() {
        return routes.handlers();
    }

    @Override
    public String toString() {
        return "Server{" +
                "host=" + server.host() +
                ", port=" + server.port() +
                '}';
    }

    private TcpServer createTcpServer(RestListener1Configuration configuration) {
        TcpServer bootstrap = TcpServer.create();
        bootstrap = ServerSecurityConfigurer.configure(RestListener1Configuration.class, bootstrap, configuration);
        bootstrap = bootstrap.bootstrap(serverBootstrap -> {
            ServerConfigurer.configure(serverBootstrap, configuration);
            return serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(bossGroup, workerGroup);
        }).doOnConnection(ServerConfigurer.onConnection(configuration));
        return bootstrap;
    }

    private static void shutdownGracefully(EventExecutorGroup executionGroup) {
        try {
            executionGroup.shutdownGracefully(0, 3, SECONDS).sync();
        } catch (InterruptedException e) {

            logger.warn("Error while shutting down event group", e);

            Thread.currentThread().interrupt();
        }
    }

    private static void shutdownSilently(DisposableServer server) {
        if (server != null) {
            try {
                server.disposeNow();
            } catch (Exception e) {
                logger.warn("Error while disposing Http server", e);
            }
        }
    }

    /**
     * Returns the real path, with the base path prefixed to the given
     * path if it is not blank.
     *
     * @param path the original path.
     * @return the base path + original path if the base path is not blank,
     * otherwise the original path is returned.
     */
    private String getRealPath(String path) {
        String thePath = StringUtils.isBlank(path) ? StringUtils.EMPTY : path;
        if (StringUtils.isNotBlank(configuration.getBasePath())) {
            return configuration.getBasePath() + thePath;
        } else {
            return StringUtils.isBlank(thePath) ? "/" : thePath;
        }
    }
}
