package io.common.codec;

import com.google.common.base.Objects;

public class RpcRequest {
    String requestId;
    String className;
    String methodName;

    private Class<?>[] parameterTypes;
    private Object[] parameters;

    public static RpcRequest BEAT=new RpcRequest(Beat.BEAT_ID);

    public RpcRequest(){
    }

    RpcRequest(String requestId){
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcRequest that = (RpcRequest) o;
        return Objects.equal(requestId, that.requestId) && Objects.equal(className, that.className) && Objects.equal(methodName, that.methodName) && Objects.equal(parameterTypes, that.parameterTypes) && Objects.equal(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestId, className, methodName, parameterTypes, parameters);
    }
}
