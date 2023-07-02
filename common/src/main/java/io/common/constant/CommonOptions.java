package io.common.constant;

public interface CommonOptions {

    String registryAddress="127.0.0.1:2181";

    int ZK_SESSION_TIMEOUT = 5000;
    int ZK_CONNECTION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    String ZK_SERVICE_PATH = ZK_REGISTRY_PATH + "/service";


    String ZK_NAMESPACE = "netty-rpc";
}
