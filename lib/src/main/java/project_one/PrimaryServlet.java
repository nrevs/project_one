/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package project_one;


import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




@WebServlet("/login")
@MultipartConfig(location="/tmp",
    fileSizeThreshold = 1024,
    maxFileSize = 1024,
    maxRequestSize = 1024)
public class PrimaryServlet extends HttpServlet{
    /**
     *
     */
    private static final long serialVersionUID = -6668597909242177978L;

    private String url = "jdbc:postgresql://database-project-one.cjsdfjt5gj6o.us-east-1.rds.amazonaws.com:5432/projectone";
    private String uname = "nrevs";
    private String pwDB = "KTw6bEi8dy9vxGdRjfrM";

    private DBManager dbManager = new DBManager();
    final Logger logger = LogManager.getLogger(PrimaryServlet.class);

    private ResponseBuilder rb = ResponseBuilder.getInstance();



    private String loginId = "#maincontent";
    private String loginHtml = "" + 
        "<div>" +
            "<form id=\"loginform\" method=\"post\">" +
                "Username: <input type=\"text\" name=\"username\"/><br/><br/>" +
                "Password: <input type=\"password\" name=\"usrpass\"/><br/><br/>" +
                "<input type=\"submit\" value=\"login\"/><br/>" +
                "<a id=\"forgotuname\" href=\"#\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
                "<a href=\"createaccount\" onclick=\"create(event)\">create account</a><br/>" +
            "</form>" +
        "</div>";
    private String loginSrc = "login.js";
    private String loginCmpntId = "mainComponent";


    private String forgotUnHtml = "" +
        "<div>" +
            "<p>Please enter your email and if it is registered with an account, we will email the username.</p>" +
            "<form id=\"unForm\" method=\"get\">" +
                "e-mail: <input type=\"email\"/><br/><br/>" +
                "<input type=\"submit\" value=\"submit\"/><br/>" +
                "<a href=\"login\" onclick=\"startLogin(event)\">Login</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
                "<a href=\"createaccount\" onclick=\"create(event)\">create account</a><br/>" +
            "</form>" +
        "</div>";
    private String forgotUnHtmlSrc = "forgotUn.js";

    private String forgotPwHtml = "" +
        "<div>" +
            "<p>Please enter your email and if it is registered with an account, we will email instructions to reset password.</p>" +
            "<form id=\"pwForm\" method=\"get\">" +
                "e-mail: <input type=\"email\"/><br/><br/>" +
                "<input type=\"submit\" value=\"submit\"/><br/>" +
                "<a href=\"login\" onclick=\"startLogin(event)\">Login</a><br/>" +
                "<a href=\"forgotuname\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"createaccount\" onclick=\"create(event)\">create account</a><br/>" +
            "</form>" +
        "</div>";
    private String forgotPwHtmlSrc = "forgotPw.js";

    private String createHtml = "" +
        "<div>" +
            "<form id=\"create\" method=\"post\">" +
                "Username: <input type=\"text\"/><br/><br/>" +
                "Email: <input type=\"email\"/><br/><br/>" +
                "Password: <input type=\"password\"/><br/><br/>" +
                "<a href=\"login\" onclick=\"startlogin(event)\">Login</a><br/>" +
                "<a href=\"forgotuname\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
            "</form>" +
        "</div>";
    private String createSrc = "create.js";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException,
                IOException 
    {
        String code = req.getParameter("code");
        logger.info("doGet -> request code: {}",code);

        HttpSession session = req.getSession();
        String uname = "username";
        if (session.getAttribute(uname)!=null) {
            String isadmin = "isadmin";
            if (session.getAttribute(isadmin)!=null) {
                req.getRequestDispatcher("/admin").forward(req, res);
            } else {
                req.getRequestDispatcher("/user").forward(req,res);
            }
        }

        String rString = "";

        switch(code) {
            case "login":
                rString = rb.buildResponseString(loginCmpntId, loginId, loginHtml, loginSrc);
                break;
            case "emailUsername":
                rString = rb.buildResponseString(loginCmpntId, loginId, forgotUnHtml, forgotUnHtmlSrc);
                // TODO: email username
                break;
            case "emailPasswordReset":
                rString = rb.buildResponseString(loginCmpntId, loginId, forgotPwHtml, forgotPwHtmlSrc);
                // TODO: create temporary password
                break;
            case "create":
                rString = rb.buildResponseString(loginCmpntId, loginId, createHtml, createSrc);
                break;
            }

        logger.info("doGet -> response string: {}", rString);

        if (rString != "") {
            res.setContentType("application/json");
            res.getWriter().println(rString);
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException,
                IOException
    {
        HttpSession session = req.getSession();

        String code = req.getParameter("code");
        logger.info("doPost -> request code: {}",code);

        switch(code) {
            case "login":

                try {
                    JSONObject obj = JSONPartsHelper.getJSONParts(req.getParts());
                    String username = obj.getString("username");
                    String password = obj.getString("usrpass");
                    
                    try {
                        Connection connection = DriverManager.getConnection(url, uname, pwDB);
                        UserDAO userDAO = new UserDAO(connection);
        
                        User usr = userDAO.getUser(username, password);
                        if (usr != null) {
                            // User found and password checks out
                            logger.info("usr found");
        
                            session.setAttribute("username", usr.getUsername());
                            session.setAttribute("email", usr.getEmail());
                            session.setAttribute("id", usr.getId());
        
                            if(usr.isAdmin()) {
                                // user is admin
                                logger.info("admin user");
        
                                session.setAttribute("isadmin",true);
        
                                req.getRequestDispatcher("/admin").forward(req, res);
        
        
                            } else {
                                // user is NOT admin
                                logger.info("NOT admin user");
                                
                                req.getRequestDispatcher("/user").forward(req, res);
        
                            }
                        } else {
                            // User password and/or username did not check out
                            logger.info("password or username invalid");
                            
        
                            Object aObj = session.getAttribute("loginattempts");
                            int a = 0;
                            if (aObj != null) {a  = (int)aObj;}
                            session.setAttribute("loginattempts",++a);
                            //TODO: invalid password or user response
        
                        }
        
                    } catch(SQLException sqlE) {
                        sqlE.printStackTrace();
                    }
                } catch(IOException ioE) {
                    ioE.printStackTrace();
                }
                break;
            

            case "create":
                break;

            }
        

        
    }
    
}
