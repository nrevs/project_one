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

@WebServlet("/api")
public class ApiServlet extends HttpServlet {
    private static final long serialVersionUID = 3L;
    private ResponseBuilder rb = ResponseBuilder.getInstance();

    final Logger logger = LogManager.getLogger(ApiServlet.class);

    private String apiId = "#apicontent";
    private String apiHtml = "" +
        "<div>" +
        "API CONTENT" +
        "</div>";
    private String apiSrc = "api.js";
    private String apiCmpntId = "mainComponent";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException
    {
        HttpSession session = req.getSession();

        String rString = rb.buildResponseString(apiCmpntId, apiId, apiHtml, apiSrc);

        res.setContentType("application/json");
        res.getWriter().println(rString);
    }

}
