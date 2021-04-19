package project_one;

public class Payload {

    private String id;
    private String html;
    private String src;


    public Payload(String id, String html, String src) {
        setId(id);
        setHtml(html);
        setSrc(src);
    }

    public String getId() {
        return this.id;
    }
    
    public String getHtml() {
        return this.html;
    }

    public String getSrc() {
        return this.src;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
