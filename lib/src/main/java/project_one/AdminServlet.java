package project_one;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet(urlPatterns = "/admin")
@MultipartConfig(location="/tmp",
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 *1024 * 4,
    maxRequestSize = 1024 * 1024 * 5 * 5)
public class AdminServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = -6421597909242177978L;
    private ResponseBuilder rb = ResponseBuilder.getInstance();

    
    final Logger logger = LogManager.getLogger(AdminServlet.class);


    private String adminId = "#maincontent";
    private String adminHtml = "" +
        "<div>" +
            "<div>" +
                "<h3>Upload Stock Data File</h3></br>" +
                "<form id=\"fileUploadForm\" method=\"post\"></br>" +
                    "<input type=\"file\" name=\"stockUploader\"/></br>" +
                    "<input type=\"submit\" value=\"Upload File\" onclick=\"handleFileUploadForm(event)\" />" +
                "</form>" +
            "</div>" +
            "<div>" +
                "<button id=\"adminLogoutButton\" type=\"button\">logout</button>" +
            "</div>" +
        "</div>";
    private String adminSrc = "admin.js";
    private String adminCompntId = "mainComponent";

    


    // protected void doGet(HttpServletRequest req, HttpServletResponse res) 
    //     throws ServletException, IOException 
    // {
    //     System.out.println("AdminServlet -> doGet");
    //     HttpSession session = req.getSession();
        
    //     String rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
    //     logger.info("doGet -> response string: {}",rString);
    
    //     res.setContentType("application/json");
    //     res.getWriter().println(rString);
    // }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException
    {
        System.out.println("AdminServlet -> service called");

        HttpSession session = req.getSession();

        JSONObject jsonObj;

        String code = req.getParameter("code");
        if(code == "logout") return;
        String rString;
        System.out.println(code);
        if (code != null) {
            switch(code) {
                case "login":
                    rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
                    logger.info("service -> response string: {}",rString);
                
                    res.setContentType("application/json");
                    res.getWriter().println(rString);
                    res.getWriter().close();
                    break;

                case "logout":
                    System.out.println("AdminServlet switch code == logout");
                    //res.sendRedirect(req.getContextPath() + "/login");
                    
                    //return;
                    break;

                case "upload":
                    try {
                        List<Part> theParts = (List<Part>) req.getParts();
                    
                        jsonObj = JSONPartsHelper.getJSONParts(theParts);
                            
                        JSONObject stockUploader = jsonObj.getJSONObject("stockUploader");
                        JSONObject equityInfo = stockUploader.getJSONObject("equityinfo");
                        JSONArray eoddata = stockUploader.getJSONArray("eoddata");
                        JSONObject key = stockUploader.getJSONObject("key");
                
                        System.out.println("\nKEY:");
                        System.out.println(key.toJSONString());
                        System.out.println("\n");
                        System.out.println("\nEQUITYINFO:");
                        System.out.println(equityInfo.toJSONString());
                        System.out.println("\n");
                        System.out.println("\nEODDATA:");
                        System.out.println(eoddata.toJSONString());
                        System.out.println("\n");
                    } catch (ServletException sE) {
                        System.out.println("not multipart form");
                    }
                    rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
                    logger.info("service -> response string: {}",rString);
                
                    res.setContentType("application/json");
                    res.getWriter().println(rString);
                    res.getWriter().close();
                    break;

                }
            
        } else {
            rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
            logger.info("service -> response string: {}",rString);
        
            res.setContentType("application/json");
            res.getWriter().println(rString);
            res.getWriter().close();
        }
        
        
    }
    
}
