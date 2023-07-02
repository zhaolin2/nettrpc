package io.server;

import io.common.constant.CommonOptions;
import io.server.constant.ServerOptions;
import io.server.core.NettyServer;
import io.server.impl.HelloServiceImpl;

public class RpcServerApplication {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer(ServerOptions.serverAddress, CommonOptions.registryAddress);
        nettyServer.addService("HelloService",new HelloServiceImpl());
        nettyServer.start();
    }
}