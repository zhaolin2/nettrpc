package io.common.protocol;

public class RpcServiceInfo {

    private String serviceName;

    public RpcServiceInfo(String serviceName) {
        this.serviceName=serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
