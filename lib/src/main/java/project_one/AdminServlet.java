package project_one;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@WebServlet("/admin")
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
        "ADMIN CONTENT" +
        "</div>";
    private String adminSrc = "admin.js";
    private String adminCompntId = "mainComponent";

    
    protected void service(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException 
    {
        System.out.println("called");
        HttpSession session = req.getSession();
        
        String rString = rb.buildResponseString(adminCompntId, adminId, adminHtml, adminSrc);
        logger.info("doPost -> response string: {}",rString);
    
        res.setContentType("application/json");
        res.getWriter().println(rString);
    }
    
}
