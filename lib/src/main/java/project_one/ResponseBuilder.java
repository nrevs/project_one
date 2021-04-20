package project_one;

import com.alibaba.fastjson.JSON;

public class ResponseBuilder {
    
    private static ResponseBuilder instance = new ResponseBuilder();

    private ResponseBuilder(){}

    public static ResponseBuilder getInstance() {
        return instance;
    }

    public String buildResponseString(String componentId, String payloadId, String payloadHtml, String payloadSrc) {
        Payload payload = new Payload(payloadId, payloadHtml, payloadSrc);
        Component component = new Component(componentId, payload);
        
        RespObj rObj = new RespObj();
        rObj.addComponent(component);

        return JSON.toJSONString(rObj);
    }

}
