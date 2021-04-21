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

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;
    private ResponseBuilder rb = ResponseBuilder.getInstance();

    final Logger logger = LogManager.getLogger(UserServlet.class);

    private String userId = "#maincontent";
    private String userHtml = "" +
        "<div>" +
        "USER CONTENT" +
        "</div>";
    private String userSrc = "user.js";
    private String userCmpntId = "mainComponent";

    protected void service(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        System.out.println("user servlet service called");
        String rString = rb.buildResponseString(userCmpntId, userId, userHtml, userSrc);

        res.setContentType("application/json");
        res.getWriter().println(rString);
    }
}
