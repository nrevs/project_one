package project_one;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class ResponseBuilder {
    
    private static ResponseBuilder instance = new ResponseBuilder();

    private ResponseBuilder(){}

    public static ResponseBuilder getInstance() {
        return instance;
    }


    public String buildResponseString(List<Component> components) {
        RespObj rObj = new RespObj();
        for(Component cmp: components) {
            rObj.addComponent(cmp);
        }

        return JSON.toJSONString(rObj);
    }

    public String buildResponseString(String componentId, String payloadId, String payloadHtml, String payloadSrc) {
        return initSingleComponent(componentId, payloadId, payloadHtml, payloadSrc, "empty");
    }

    public String buildResponseString(String componentId, String payloadId, String payloadHtml, String payloadSrc, String payloadData) {
        return initSingleComponent(componentId, payloadId, payloadHtml, payloadSrc, payloadData);
    }

    private String initSingleComponent(String componentId, String payloadId, String payloadHtml, String payloadSrc, String payloadData) {
        Payload payload = new Payload(payloadId, payloadHtml, payloadSrc, payloadData);
        Component component = new Component(componentId, payload);
        
        RespObj rObj = new RespObj();
        rObj.addComponent(component);

        return JSON.toJSONString(rObj);
    }
}
