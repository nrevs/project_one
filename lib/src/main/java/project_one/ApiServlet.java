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

@WebServlet("/api")
public class ApiServlet extends HttpServlet {

    final Logger logger = LogManager.getLogger(ApiServlet.class);
    private static final long serialVersionUID = 3L;
    private ResponseBuilder rb = ResponseBuilder.getInstance();

    private String url = "jdbc:postgresql://database-project-one.cjsdfjt5gj6o.us-east-1.rds.amazonaws.com:5432/projectone";
    private String uname = "nrevs";
    private String pwDB = "KTw6bEi8dy9vxGdRjfrM";

    private String apiId = "#apicontent";
    private String apiHtml = "" +
        "<div>" +
            "<h4 id=\"tickertitle\"></h4></br>" +
            "<ul id=\"histdatalist\" style=\"list-style: none;\"></ul>" +
        "</div>";
    private String apiSrc = "empty";
    private String apiCmpntId = "histDataComponent";
    private String userData = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        System.out.println("ApiServlet doGet called");
        String code = req.getParameter("code");
        String sessionId = req.getParameter("sessionid");
        String username = req.getParameter("username");
        String ticker = req.getParameter("ticker");

        String histData;
        try {
            // connection and DAOs setup
            Connection connection = DriverManager.getConnection(url, uname, pwDB);
            UsrSessionDAO usrSeshDAO = new UsrSessionDAO(connection);
            TickerDataDAO tickerDataDAO = new TickerDataDAO(connection);

            // increment and check activesession reqcount
            usrSeshDAO.incrementRequestCount(sessionId);
            int reqCount = usrSeshDAO.getRequestCountBySessionId(sessionId);
            if(reqCount<10) {
                // mainComponent
                Payload payloadMain = new Payload(apiId, apiHtml, "empty", "empty");
                Component mainComponent = new Component("mainComponent", payloadMain);

                // histDataComponent
                JSONArray tickerDataArray = tickerDataDAO.getTickersDataByTicker(ticker);
                JSONObject jsonTickerData = new JSONObject();
                jsonTickerData.put("eoddata",tickerDataArray);
                jsonTickerData.put("tickersymbol",ticker);
                
                histData = jsonTickerData.toJSONString();

                Payload payloadHistData = new Payload("#histdatalist","empty","empty", histData);
                Component histDataComponent = new Component(apiCmpntId, payloadHistData);

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

                // build response
                ArrayList<Component> cmps = new ArrayList<Component>();
                cmps.add(mainComponent);
                cmps.add(histDataComponent);
                cmps.add(seshComponent);
                String rString = rb.buildResponseString(cmps);
                System.out.println("ApiServlet rString: "+rString);

                res.setContentType("application/json");
                res.getWriter().println(rString);
            }
        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

}
