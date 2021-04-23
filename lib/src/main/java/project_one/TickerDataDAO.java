package project_one;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TickerDataDAO {
    private Logger logger = LogManager.getLogger(UsrSessionDAO.class);

    private Connection _connection;


    private String _symbolstring = "symbolstring";
    private String _date = "date";
    private String _high = "high";
    private String _low = "low";
    private String _open = "open";
    private String _close = "close";
    private String _sharevolume = "sharevolume";
    private String _totaltrades = "totaltrades";

    public TickerDataDAO(Connection connection) {
        _connection = connection;
    }

    public int loadData(JSONObject jsonObj) {
        
            // String symbolstring,
            // String date, 
            // String high, 
            // String low, 
            // String open,
            // String close,
            // String sharevolume,
            // String totaltrades) {

        String symbolstring = jsonObj.getString("symbolstring");
        JSONArray eoddata = jsonObj.getJSONArray("eoddata");

        int code = 0;
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                "SELECT ticker from tickers WHERE ticker=?;"
            );
            pStatement.setString(1, symbolstring);
            ResultSet rSet = pStatement.executeQuery();
            if(rSet.next()==false) {
                // Ticker does not exist, make table for ticker
                pStatement.close();
                pStatement = _connection.prepareStatement(
                    "CREATE TABLE public.? (" +
                        "data date COLLATE pg_catalog.\"default\" NOT NULL PRIMARY KEY," +
                        "high numeric(10,2) NOT NULL," +
                        "low numeric(10,2) NOT NULL," +
                        "open numeric(10,2) NOT NULL," +
                        "close numeric(10,2) NOT NULL," +
                        "sharevolume integer NOT NULL," +
                        "totaltrades integer NOT NULL" +
                    ") "
                );
                pStatement.setString(1, symbolstring);
                code = pStatement.executeUpdate();
                System.out.println("SQL CODE: "+String.valueOf(code));
                pStatement.close();
                for(Object eod : eoddata){
                    JSONObject jsonEod = (JSONObject) eod;
                    Date date = (Date) jsonEod.getDate("date");
                    Float high = jsonEod.getFloat("high");
                    Float low = jsonEod.getFloat("low");
                    Float open = jsonEod.getFloat("open");
                    Float close = jsonEod.getFloat("close");
                    int sharevolume = jsonEod.getIntValue("sharevolume");
                    int totaltrades = jsonEod.getIntValue("totaltrades");

                    if (totaltrades > 0) {
                        pStatement = _connection.prepareStatement(
                            "INSERT INTO ? (date, high, low, open, close, sharevolume, totaltrades) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);"
                                   //2, 3, 4, 5, 6, 7, 8
                        );
                        pStatement.setString(1, symbolstring);
                        pStatement.setDate(2, date);
                        pStatement.setFloat(3,high);
                        pStatement.setFloat(4,low);
                        pStatement.setFloat(5,open);
                        pStatement.setFloat(6,close);
                        pStatement.setInt(7,sharevolume);
                        pStatement.setInt(8,totaltrades);
                        code = pStatement.executeUpdate();
                    }
                    
                    
                }
                
            } else {
                //TODO: FILL IN THIS SITUATION
                // Ticker exists, so table should exist for ticker
            }
            

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return code;
    }

    public JSONObject getTickerData(String symbolstring) {
        JSONObject jsonObj = new JSONObject();
        return jsonObj;
    }
    
}
