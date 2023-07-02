package io.server.registry;

import com.google.common.base.Joiner;
import io.common.constant.CommonOptions;
import io.common.protocol.RpcServiceInfo;
import io.common.util.ServiceUtils;
import io.common.zookeeper.CuratorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceRegistry {

    Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private List<String> pathList = new ArrayList<>();


    String registryServerAddress;

    String serverAddress;

    CuratorClient curatorClient;

    public ServiceRegistry(String registryServerAddress,String serverAddress) {
        this.registryServerAddress = registryServerAddress;
        this.serverAddress=serverAddress;
        this.curatorClient = new CuratorClient(registryServerAddress, 5000);
    }

    public void register(Map<String, Object> serviceMap) {
        List<RpcServiceInfo> serviceInfos = serviceMap.keySet().stream().map(RpcServiceInfo::new).collect(Collectors.toList());


        try {
            for (Map.Entry<String, Object> entry : serviceMap.entrySet()) {
                String key = entry.getKey();
                Object target = entry.getValue();


               String  basePath = this.curatorClient.createPathData(ServiceUtils.tryGetPath(key,serverAddress), new byte[0]);

                pathList.add(basePath);

            }
        } catch (Exception e) {
            logger.warn("Failed to register service");
        }
    }


}
