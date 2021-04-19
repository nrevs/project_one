package project_one;

public class Component {
    private String id;
    private Payload payload;

    public Component(String id, Payload payload) {
        setId(id);
        setPayload(payload);
    }

    public String getId() {
        return this.id;
    }

    public Payload getPayload() {
        return this.payload;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

}
