package io.server.core;

import com.google.common.collect.Maps;
import io.common.util.ServiceUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.server.core.handler.RpcServerInit;
import io.server.registry.ServiceRegistry;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NettyServer {

    Logger logger = LoggerFactory.getLogger(NettyServer.class);

    String serverAddress;

    Map<String,Object> serviceMap=new HashMap<String,Object>();

    ServiceRegistry serviceRegistry;

    Executor executor = Executors.newSingleThreadExecutor();

    public NettyServer(String serverAddress,String registryAddress){
        this.serverAddress=serverAddress;
        this.serviceRegistry =new ServiceRegistry(registryAddress,serverAddress);
    }

    public void start()  {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RpcServerInit(serviceMap,executor))
                    .option(ChannelOption.SO_BACKLOG,2048)
                    .option(ChannelOption.SO_KEEPALIVE,true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("netty启动成功");

            if (MapUtils.isNotEmpty(this.serviceMap)){
                serviceRegistry.register(this.serviceMap);
            }


            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.warn("启动netty失败",e);
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            boss.shutdownGracefully();
        }


    }

    public void addService(String interfaceName,Object bean){
        String serviceKey = ServiceUtils.buildServiceKey(interfaceName);
        serviceMap.put(serviceKey, bean);
    }

    public void stop(){
    }
}
