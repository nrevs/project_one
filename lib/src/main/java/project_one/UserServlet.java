package project_one;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    final Logger logger = LogManager.getLogger(UserServlet.class);
    
    private static final long serialVersionUID = 2L;
    private ResponseBuilder rb = ResponseBuilder.getInstance();


    private String url = "jdbc:postgresql://database-project-one.cjsdfjt5gj6o.us-east-1.rds.amazonaws.com:5432/projectone";
    private String uname = "nrevs";
    private String pwDB = "KTw6bEi8dy9vxGdRjfrM";

    private String userId = "#maincontent";
    private String userHtml = "" +
        "<div>" +
            "<h2>Welcome {USERNAME}</h3></br></br>" +
            "<h3>Your active sessions:</h3></br>" +
            "<div id='userSessions'>" +
            "</div>" +
            "<button type=\"button\" onclick=\"getNewSession()\">new session</button></br>" +
            "<button type=\"button\" onclick=\"logout()\">logout</button></br>" +
        "</div>";
    private String userSrc = "user.js";
    private String userCmpntId = "mainComponent";
    private String userData = "";

    protected void service(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        System.out.println("user servlet service called");
        userHtml.replaceAll("{USERNAME}",(String)session.getAttribute("username"));

        try {
            Connection connection = DriverManager.getConnection(url, uname, pwDB);
            UsrSessionDAO usrSeshDAO = new UsrSessionDAO(connection);
            ArrayList<UsrSession> usrSessions = (ArrayList<UsrSession>)usrSeshDAO.getUsrSessionsByUserId((int)session.getAttribute("id"));
            
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(usrSessions);
            
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("sessions",jsonArray);
            userData = jsonObj.toJSONString();
            
        
        
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        String rString = rb.buildResponseString(userCmpntId, userId, userHtml, userSrc, userData);


        res.setContentType("application/json");
        res.getWriter().println(rString);
    }
}
