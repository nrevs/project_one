package project_one;

public class Payload {

    private String id;
    private String html;
    private String src;
    private String data;


    public Payload(String id, String html, String src, String data) {
        init(id, html, src, data);
    }

    private Payload init(String id, String html, String src, String data) {
        setId(id);
        setHtml(html);
        setSrc(src);
        setData(data);
        return this;
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

    public String getData() {
        return this.data;
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

    public void setData(String data) {
        this.data = data;
    }
}
