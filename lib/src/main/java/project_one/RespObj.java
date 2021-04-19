package project_one;

import java.util.ArrayList;
import java.util.List;

public class RespObj {

    private List<Component> components;

    public RespObj() {
        this.components = new ArrayList<Component>();
    }

    public RespObj(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return this.components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }
    
}
