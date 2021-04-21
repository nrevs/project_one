package project_one;

import com.alibaba.fastjson.JSON;

public class ResponseBuilder {
    
    private static ResponseBuilder instance = new ResponseBuilder();

    private ResponseBuilder(){}

    public static ResponseBuilder getInstance() {
        return instance;
    }


    public String buildResponseString(String componentId, String payloadId, String payloadHtml, String payloadSrc) {
        return init(componentId, payloadId, payloadHtml, payloadSrc, "empty");
    }

    public String buildResponseString(String componentId, String payloadId, String payloadHtml, String payloadSrc, String payloadData) {
        return init(componentId, payloadId, payloadHtml, payloadSrc, payloadData);
    }

    private String init(String componentId, String payloadId, String payloadHtml, String payloadSrc, String payloadData) {
        Payload payload = new Payload(payloadId, payloadHtml, payloadSrc, payloadData);
        Component component = new Component(componentId, payload);
        
        RespObj rObj = new RespObj();
        rObj.addComponent(component);

        return JSON.toJSONString(rObj);
    }
}
