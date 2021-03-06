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

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {

    final Logger logger = LogManager.getLogger(UserServlet.class);
    
    private static final long serialVersionUID = 2L;
    private ResponseBuilder rb = ResponseBuilder.getInstance();

    private String url = "jdbc:postgresql://database-project-one.cjsdfjt5gj6o.us-east-1.rds.amazonaws.com:5432/projectone";
    private String uname = "nrevs";
    private String pwDB = "KTw6bEi8dy9vxGdRjfrM";

    private String userIdTag = "#maincontent";
    private String userHtml = "" +
        "<div>" +
            "<div>" +
                "<h2>Welcome {USERNAME}</h3></br></br>" +
                "<button id=\"newSessionButton\" type=\"button\" onclick=\"getNewSession(event)\">new session</button>" +
                "<button id=\"userLogoutButton\" type=\"button\" onclick=\"logoutUser(event)\" >logout</button></br>" +
                "<h3>Your active sessions:</h3></br>" +
                "<form id=\"sessionsForm\" method=\"get\"></br>" +
                    "<table id=sessionsTable class=\"table table-bordered table-striped\">" +
                        "<thead>" +
                            "<tr>" +
                                "<th>SessionID</th>" +
                                "<th>Expiration</th>" +
                                "<th>Req Count</th>" +
                                "<th>Select</th>" +
                            "</tr>" +
                        "</thead>" +
                        "<tbody id=sessionsTableBody>" +
                        "</tbody>" +
                    "</table>" +
                    "<input type=\"hidden\" name=\"username\" value=\"{USERNAME}\"/>" +
                "</form>" +
                "<label for=\"tickers\">Choose a Ticker:</label>" +
                "<select id=\"tickers\" name=\"ticker\" form=\"sessionsForm\">" +
                "</select></br></br>" + 
                "<button id=\"getApiDataButton\" type=\"button\" onclick=\"getApiData(event)\">GET DATA</button>" +
            "</div>" + 
            "<div id=\"apicontent\">" +
            "</div>" +
        "</div>";
    private String userSrc = "user.js";
    private String userCmpntId = "mainComponent";
    private String userData = "";
    private String tickersData = "";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException
    {
        HttpSession session = req.getSession();

        String code = req.getParameter("code");
        String username = (String)session.getAttribute("username");

        System.out.println("user servlet service called");
        userHtml = userHtml.replaceAll("\\{USERNAME\\}", username);
        System.out.println(userHtml);
        try {
            // connection and DAOs setup
            Connection connection = DriverManager.getConnection(url, uname, pwDB);
            UsrSessionDAO usrSeshDAO = new UsrSessionDAO(connection);
            TickerDataDAO tickerDataDAO = new TickerDataDAO(connection);

            // mainComponent
            Payload payload1 = new Payload(userIdTag, userHtml, userSrc, "empty");
            Component mainComponent = new Component(userCmpntId, payload1);

            // seshComponent
            ArrayList<UsrSession> usrSessions;

            usrSessions = (ArrayList<UsrSession>)usrSeshDAO.getUsrSessionsByUserId((int)session.getAttribute("id"));

            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(usrSessions);
            
            JSONObject jsonUsrData = new JSONObject();
            jsonUsrData.put("sessions",jsonArray);
            userData = jsonUsrData.toJSONString();
     
            Payload payload2 = new Payload("#sessionsTableBody","empty","empty",userData);
            Component seshComponent = new Component("seshComponent", payload2);

            // tickerComponent
            JSONArray tickersArray = tickerDataDAO.getTickersArray();
            
            JSONObject jsonTickerData = new JSONObject();
            jsonTickerData.put("tickers", tickersArray);
            tickersData = jsonTickerData.toJSONString();
            System.out.println(tickersData);
                       
            Payload payload3 = new Payload("#tickers","empty", "empty", tickersData);
            Component tickersComponent = new Component("tickersComponent", payload3);
            
            // build response
            ArrayList<Component> cmps = new ArrayList<Component>();
            cmps.add(mainComponent);
            cmps.add(seshComponent);
            cmps.add(tickersComponent);
            String rString = rb.buildResponseString(cmps);
            System.out.println("UserServlet rString: "+rString);

            res.setContentType("application/json");
            res.getWriter().println(rString);
        
        
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }



    }
}
