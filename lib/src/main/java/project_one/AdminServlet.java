package project_one;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = -6421597909242177978L;


    
    final Logger logger = LogManager.getLogger(AdminServlet.class);


    private String adminId = "#maincontent";
    private String adminHtml = "" +
        "<div>" +
        "ADMIN CONTENT" +
        "</div>";
    private String adminSrc = "admin.js";
    private String adminCompntId = "mainComponent";

    
    protected void service(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        System.out.println("called");
        HttpSession session = req.getSession();
        
        Payload payload = new Payload(adminId, adminHtml, adminSrc);
        Component adminCmp = new Component(adminCompntId, payload);

        RespObj rObj = new RespObj();
        rObj.addComponent(adminCmp);
        

        String rString = JSON.toJSONString(rObj);
        logger.info("doPost -> response string: {}",rString);
    
        res.setContentType("text/html");
        res.getWriter().println(rString);
    }
    
}
