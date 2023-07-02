package io.zl.connect;

import com.google.common.base.Splitter;
import io.common.codec.RpcRequest;
import io.common.codec.RpcResponse;
import io.common.constant.CommonOptions;
import io.common.util.ServiceUtils;
import io.common.zookeeper.CuratorClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.*;
import io.zl.handler.ClientInit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConnectManager {

    public static DefaultPromise<RpcResponse> promise= null;

    NioEventLoopGroup eventLoopGroup=new NioEventLoopGroup(4);
    CuratorClient curatorClient=new CuratorClient(CommonOptions.registryAddress);

    EventExecutor eventExecutor= new DefaultEventExecutor();

    ChannelFuture channelFuture;

    private ConnectManager() {
    }

    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        String targetInfo = getTargetInfo(request);
        List<String> list = Splitter.on(":").splitToList(targetInfo);
        ChannelFuture future = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientInit())
                .connect(list.get(0), Integer.parseInt(list.get(1)))
                .sync();
        this.channelFuture=future;

        future.channel().writeAndFlush(request);

        DefaultPromise<RpcResponse> promise = new DefaultPromise<>(eventExecutor);
        ConnectManager.promise =promise;


        return promise.get();
    }

    private String getTargetInfo(RpcRequest request) throws Exception {
        String className = request.getClassName();
        String serviceKey = ServiceUtils.buildServiceKey(className);

        List<String> services = curatorClient.getChildren(CommonOptions.ZK_SERVICE_PATH);
        String firstService = services.get(0);

        List<String> targetInfoPaths = curatorClient.getChildren(CommonOptions.ZK_SERVICE_PATH + "/" + firstService);

        List<String> targetHosts = ServiceUtils.tryGetTargetInfo(targetInfoPaths);

        return targetHosts.get(0);
    }

    private static class SingletonHolder {
        private static final ConnectManager instance = new ConnectManager();
    }

    public static ConnectManager getInstance() {
        return SingletonHolder.instance;
    }


    public void close() throws InterruptedException {
        this.channelFuture.channel().close().sync();
        this.eventLoopGroup.shutdownGracefully();
        eventExecutor.shutdownGracefully();
        curatorClient.close();
    }

}
