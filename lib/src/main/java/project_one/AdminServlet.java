package project_one;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet("/admin")
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
                "<button type=\"button\" onclick=\"logout(event)\">logout</button>" +
            "</div>" +
        "</div>";
    private String adminSrc = "admin.js";
    private String adminCompntId = "mainComponent";

    


    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        System.out.println("AdminServlet -> doGet");
        HttpSession session = req.getSession();
        
        String rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
        logger.info("doGet -> response string: {}",rString);
    
        res.setContentType("application/json");
        res.getWriter().println(rString);
    }


    protected void service(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException
    {
        System.out.println("AdminServlet -> service called");

        HttpSession session = req.getSession();

        JSONObject jsonObj;
        jsonObj = JSONPartsHelper.getJSONParts(req.getParts());
        System.out.println(jsonObj.toJSONString());

        String rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
        logger.info("service -> response string: {}",rString);
    
        res.setContentType("application/json");
        res.getWriter().println(rString);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException
    {
        System.out.println("AdminServlet -> doPost called");

        HttpSession session = req.getSession();
        
        JSONObject jsonObj;
        jsonObj = JSONPartsHelper.getJSONParts(req.getParts());
        System.out.println(jsonObj.toJSONString());

        String rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
        logger.info("doPost -> response string: {}",rString);
    
        res.setContentType("application/json");
        res.getWriter().println(rString);
    }
    
}
