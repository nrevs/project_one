/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package project_one;


import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.CharacterPredicates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




@WebServlet("/login")
@MultipartConfig(location="/tmp",
    fileSizeThreshold = 1024,
    maxFileSize = 1024,
    maxRequestSize = 1024)
public class PrimaryServlet extends HttpServlet{
    final Logger logger = LogManager.getLogger(PrimaryServlet.class);
    
    /**
     *
     */
    private static final long serialVersionUID = -6668597909242177978L;
    
    
    private String url = "jdbc:postgresql://database-project-one.cjsdfjt5gj6o.us-east-1.rds.amazonaws.com:5432/projectone";
    private String uname = "nrevs";
    private String pwDB = "KTw6bEi8dy9vxGdRjfrM";

    private DBManager dbManager = new DBManager();

private ResponseBuilder rb = ResponseBuilder.getInstance();


    /*
    *   LOGIN CONTENT
    */
    private String loginId = "#maincontent";
    private String loginHtml = "" + 
        "<div>" +
            "<form id=\"loginform\" method=\"post\">" +
                "Username: <input type=\"text\" name=\"username\"/><br/><br/>" +
                "Password: <input type=\"password\" name=\"password\"/><br/><br/>" +
                "<input type=\"submit\" value=\"login\"/><br/>" +
                "<a id=\"forgotuname\" href=\"#\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
                "<a href=\"createaccount\" onclick=\"create(event)\">create account</a><br/>" +
            "</form>" +
        "</div>";
    private String loginSrc = "login.js";
    private String loginCmpntId = "mainComponent";

    /*
    *   FORGOT USERNAME CONTENT
    */
    private String forgotUnHtml = "" +
        "<div>" +
            "<p>Please enter your email and if it is registered with an account, we will email the username.</p>" +
            "<form id=\"unForm\" method=\"get\">" +
                "e-mail: <input type=\"email\" name=\"email\"/><br/><br/>" +
                "<input type=\"submit\" value=\"submit\"/><br/>" +
                "<a href=\"login\" onclick=\"startLogin(event)\">login</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
                "<a href=\"createaccount\" onclick=\"create(event)\">create account</a><br/>" +
            "</form>" +
        "</div>";
    private String forgotUnHtmlSrc = "forgotUn.js";

    /*
    *   FORGOT PASSWORD CONTENT
    */
    private String forgotPwHtml = "" +
        "<div>" +
            "<p>Please enter your email and if it is registered with an account, we will email instructions to reset password.</p>" +
            "<form id=\"pwForm\" method=\"get\">" +
                "e-mail: <input type=\"email\" name=\"email\"/><br/><br/>" +
                "<input type=\"submit\" value=\"submit\"/><br/>" +
                "<a href=\"login\" onclick=\"startLogin(event)\">login</a><br/>" +
                "<a href=\"forgotuname\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"createaccount\" onclick=\"create(event)\">create account</a><br/>" +
            "</form>" +
        "</div>";
    private String forgotPwHtmlSrc = "forgotPw.js";

    /*
    *   CREATE ACCOUNT CONTENT
    */    
    private String createHtml = "" +
        "<div>" +
            "<form id=\"create\" method=\"post\">" +
                "Username: <input type=\"text\" name=\"username\"/><br/><br/>" +
                "Email: <input type=\"email\" name=\"email\"/><br/><br/>" +
                "Password: <input type=\"password\" name=\"password\"/><br/><br/>" +
                "<input type=\"submit\" value=\"submit\"/><br/>" +
                "<a href=\"login\" onclick=\"startLogin(event)\">login</a><br/>" +
                "<a href=\"forgotuname\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
            "</form>" +
        "</div>";
    private String createSrc = "create.js";


    /*
    *   RESET PASSWORD CONTENT
    */
    private String resetHtml = "" +
        "<div>" +
            "<form id=\"reset\" method=\"post\">" +
                "New Password: <input type=\"password\" name=\"password\"/><br/><br/>" +
                "<input type=\"submit\" value=\"submit\"/><br/>" +
                "<a href=\"login\" onclick=\"startlogin(event)\">Login</a><br/>" +
                "<a href=\"forgotuname\" onclick=\"forgotUsername(event)\">forgot username</a><br/>" +
                "<a href=\"forgotpass\" onclick=\"forgotPassword(event)\">forgot password</a><br/>" +
            "</form>" +
        "</div>";
    private String resetSrc = "reset.js";





    
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
                break;
            case "emailPasswordReset":
                rString = rb.buildResponseString(loginCmpntId, loginId, forgotPwHtml, forgotPwHtmlSrc);
                break;
            case "create":
                rString = rb.buildResponseString(loginCmpntId, loginId, createHtml, createSrc);
                break;
            case "logout":
                session.invalidate();
                rString = rb.buildResponseString(loginCmpntId, loginId, loginHtml, loginSrc);
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
        System.out.println("doPost -> request code: "+code);
        String rString;

        JSONObject jsonObj;
        try {
            Connection connection = DriverManager.getConnection(url, uname, pwDB);
            UserDAO userDAO = new UserDAO(connection);
            User usr;
            
            switch(code) {
                case "login":

                    jsonObj = JSONPartsHelper.getJSONParts(req.getParts());
                    String username = jsonObj.getString("username");
                    String password = jsonObj.getString("password");
                                    
                    usr = userDAO.getUserFromPassword(username, password);
                    if (usr != null) {
                        // User found and password checks out
                        logger.info("usr found");
                        System.out.println("usr found");

                        session.setAttribute("username", usr.getUsername());
                        session.setAttribute("email", usr.getEmail());
                        session.setAttribute("id", usr.getId());

                        if (usr.getTmpExpire()!=null) {
                            System.out.println("has tmpexpire");
                            // send to reset password
                            rString = rb.buildResponseString(loginCmpntId, loginId, resetHtml, resetSrc);
                            res.setContentType("application/json");
                            res.getWriter().println(rString);
                            return;
                        }

                        if(usr.isAdmin()) {
                            // user is admin
                            logger.info("admin user");
                            System.out.println("admin user -> /admin");
                            session.setAttribute("isadmin",true);

                            req.getRequestDispatcher("/admin").forward(req, res);


                        } else {
                            // user is NOT admin
                            logger.info("NOT admin user");
                            System.out.println("not admin user, sending to user servlet");
                            
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
        
                    break;

                case "emailUsername":
                    
                    System.out.println("doPost -> emailUsername");

                    jsonObj = JSONPartsHelper.getJSONParts(req.getParts());
                    String email1 = jsonObj.getString("email");
                    
                    usr = userDAO.getUserFromEmail(email1);
                    if (usr != null) {
                        session.setAttribute("username", usr.getUsername());
                        session.setAttribute("email", usr.getEmail());
                        session.setAttribute("id", usr.getId());

                        String subj = "Your Username";
                        String msg = "Your username is: "+usr.getUsername();
                        Mailer.send(usr.getEmail(), subj, msg);
                    }
                    rString = rb.buildResponseString(loginCmpntId, loginId, loginHtml, loginSrc);
                    res.setContentType("application/json");
                    res.getWriter().println(rString);
                    break;

                case "emailPasswordReset":
                    System.out.println("doPost -> emailPasswordReset");
                    
                    jsonObj = JSONPartsHelper.getJSONParts(req.getParts());
                    String email2 = jsonObj.getString("email");
                    RandomStringGenerator generator = new RandomStringGenerator.Builder()
                        .withinRange('0','z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
                    String tmppass = generator.generate(8);
                    System.out.println("temp password: "+tmppass);

                    Timestamp tstamp = Timestamp.from(Instant.now().plus(3, ChronoUnit.HOURS));
                    usr = userDAO.setTempPassword(email2, tmppass, tstamp);
                    
                    if (usr != null) {
                        session.setAttribute("username", usr.getUsername());
                        session.setAttribute("email", usr.getEmail());
                        session.setAttribute("id", usr.getId());

                        String subj = "Temporary Password";
                        String msg = "Your temprorary password is: "+tmppass;
                        Mailer.send(usr.getEmail(), subj, msg);
                    }
                    
                    rString = rb.buildResponseString(loginCmpntId, loginId, loginHtml, loginSrc);
                    res.setContentType("application/json");
                    res.getWriter().println(rString);
                    break;
                

                case "create":
                    System.out.println("doPost -> create");

                    jsonObj = JSONPartsHelper.getJSONParts(req.getParts());

                    String username2 = jsonObj.getString("username");
                    String password2 = jsonObj.getString("password");
                    String email3 = jsonObj.getString("email");
                    System.out.println("Username: "+username2);
                    System.out.println("Password: "+password2);
                    System.out.println("Email: "+email3);
                    

                    usr = userDAO.createUser(username2, password2, email3, false);

                    if (usr != null) {
                        session.setAttribute("username", usr.getUsername());
                        session.setAttribute("email", usr.getEmail());
                        session.setAttribute("id", usr.getId());

                        req.getRequestDispatcher("/user").forward(req, res);
                    }
                    break;


                case "reset":
                    System.out.println("doPost -> reset");
                    
                    jsonObj = JSONPartsHelper.getJSONParts(req.getParts());

                    String password3 = jsonObj.getString("password");

                    usr = userDAO.updateUserPassword((String)session.getAttribute("username"), password3);
                    
                    if (usr != null) {
                        req.getRequestDispatcher("/user").forward(req, res);
                    }

                    break;

            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
        

        
    }
    
}
