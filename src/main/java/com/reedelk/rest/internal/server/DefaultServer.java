package com.reedelk.rest.internal.server;


import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.internal.commons.RealPath;
import com.reedelk.rest.internal.server.configurer.ServerConfigurer;
import com.reedelk.rest.internal.server.configurer.ServerSecurityConfigurer;
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

    private final RESTListenerConfiguration configuration;

    DefaultServer(RESTListenerConfiguration configuration) {
        this.configuration = configuration;

        this.routes = new DefaultServerRoutes();
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();

        TcpServer bootstrap = createTcpServer(configuration);
        HttpServer httpServer = HttpServer.from(bootstrap).handle(routes);
        this.server = ServerConfigurer.configure(httpServer, configuration).bindNow();
    }

    @Override
    public void addRoute(RouteDefinition routeDefinition, HttpRequestHandler httpHandler) {
        requireNonNull(httpHandler, "httpHandler");
        requireNonNull(routeDefinition.getMethod(), "method");

        String realPath = RealPath.from(configuration, routeDefinition.getPath());

        routeDefinition.getMethod().addRoute(routes, realPath, httpHandler);
    }

    @Override
    public void removeRoute(RouteDefinition routeDefinition) {
        requireNonNull(routeDefinition.getMethod(), "method");

        String realPath = RealPath.from(configuration, routeDefinition.getPath());

        routes.remove(HttpMethod.valueOf(routeDefinition.getMethod().name()), realPath);
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

    private TcpServer createTcpServer(RESTListenerConfiguration configuration) {
        TcpServer bootstrap = TcpServer.create();
        bootstrap = ServerSecurityConfigurer.configure(RESTListenerConfiguration.class, bootstrap, configuration);
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
}
