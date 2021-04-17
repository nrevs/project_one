package project_one;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import javax.servlet.http.Part;


import com.alibaba.fastjson.JSONObject;

public class JSONPartsHelper {

    static Reader reader;
    static Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
    static StringBuilder strBldr = new StringBuilder();

    protected static JSONObject getJSONParts(Collection<Part> parts) throws IOException {
        if (parts == null)
            return null;


        
        JSONObject obj = new JSONObject();

        for (Part p : parts) {
            strBldr.setLength(0);

            try (Reader reader = new BufferedReader(
                    new InputStreamReader(
                            p.getInputStream(), 
                            charset
                        )
                    )
                ) {
                int c = 0;
                while ((c=reader.read()) != -1) {
                    strBldr.append((char) c);
                }
            }
            obj.put(p.getName(), strBldr.toString());
        }
        return obj;
    }

}

