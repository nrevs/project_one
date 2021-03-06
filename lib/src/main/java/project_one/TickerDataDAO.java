package project_one;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
        symbolstring = symbolstring.toLowerCase(); //!IMPORTANT! per SQL standard
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

                String createTable = "" +
                    "CREATE TABLE {TABLE_NAME} (" +
                        "date date NOT NULL," +
                        "high numeric(10,4) NOT NULL," +
                        "low numeric(10,4) NOT NULL," +
                        "open numeric(10,4) NOT NULL," +
                        "close numeric(10,4) NOT NULL," +
                        "sharevolume integer NOT NULL," +
                        "totaltrades integer NOT NULL," +
                        "CONSTRAINT {TABLE_NAME}_pkey PRIMARY KEY (date)" +
                    ");";
                createTable = createTable.replaceAll("\\{TABLE_NAME\\}",symbolstring);

                pStatement = _connection.prepareStatement(createTable);
                pStatement.execute();
                System.out.println("SQL CODE: "+String.valueOf(code));
                

                pStatement = _connection.prepareStatement(
                    "INSERT INTO tickers (ticker) VALUES (?);"
                );
                pStatement.setString(1, symbolstring);
                pStatement.executeUpdate();

                String insertString = "" +
                "INSERT INTO {TABLE_NAME} (date, high, low, open, close, sharevolume, totaltrades) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
                    //  1, 2, 3, 4, 5, 6, 7
                insertString = insertString.replaceAll("\\{TABLE_NAME\\}",symbolstring);
                System.out.println(eoddata.toJSONString());
                int eoddataSize = eoddata.size();
                System.out.println("eoddataSize: "+String.valueOf(eoddataSize));
                for(int idx = 0;  idx<eoddataSize ; idx++){
                    JSONObject jsonEod = eoddata.getJSONObject(idx);
                    System.out.println("enterind data");
                    
                    //JSONObject jsonEod = (JSONObject) eod;
                    System.out.println(jsonEod.toJSONString());

                    //Date date = (Date) jsonEod.getDate("date");
                    String date = jsonEod.getString("date");
                    LocalDate lclDate = LocalDate.parse(date);
                    Date sqlDate =  Date.valueOf(lclDate);
                    System.out.println("date: "+String.valueOf(date));

                    Float high = jsonEod.getFloat("high");
                    System.out.println("high: "+String.valueOf(high));

                    Float low = jsonEod.getFloat("low");
                    System.out.println("low: "+String.valueOf(low));

                    Float open = jsonEod.getFloat("open");
                    System.out.println("open: "+String.valueOf(open));

                    Float close = jsonEod.getFloat("close");
                    System.out.println("close: "+String.valueOf(close));

                    int sharevolume = jsonEod.getIntValue("sharevolume");
                    System.out.println("sharevolume: "+String.valueOf(sharevolume));

                    int totaltrades = jsonEod.getIntValue("totaltrades");
                    System.out.println("totaltrades: "+String.valueOf(totaltrades));
                    if (totaltrades > 0) {
                        pStatement = _connection.prepareStatement(insertString);
                        
                        pStatement.setDate(1, sqlDate);
                        pStatement.setFloat(2,high);
                        pStatement.setFloat(3,low);
                        pStatement.setFloat(4,open);
                        pStatement.setFloat(5,close);
                        pStatement.setInt(6,sharevolume);
                        pStatement.setInt(7,totaltrades);
                        code = pStatement.executeUpdate();
                        System.out.println("code: "+String.valueOf(code));
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


    public JSONArray getTickersDataByTicker(String ticker) {
        JSONArray jsonArray = new JSONArray();
        String queryString = "SELECT date, high, low, open, close, sharevolume, totaltrades FROM {TICKER};";
        
        queryString = queryString.replaceAll("\\{TICKER\\}",ticker);
        System.out.println("queryString: "+queryString);
        try {
            PreparedStatement pStatement = _connection.prepareStatement(queryString);
            ResultSet rSet = pStatement.executeQuery();
            while(rSet.next()) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("date",rSet.getDate("date"));
                jsonObj.put("high",rSet.getFloat("high"));
                jsonObj.put("low",rSet.getFloat("low"));
                jsonObj.put("open",rSet.getFloat("open"));
                jsonObj.put("close",rSet.getFloat("close"));
                jsonObj.put("sharevolume",rSet.getInt("sharevolume"));
                jsonObj.put("totaltrades",rSet.getInt("totaltrades"));
                jsonArray.add(jsonObj);
            }

        } catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        return jsonArray;
    }

    /*
    *   GETS THE TABLE OF TICKER SYMBOLS
    */
    public JSONArray getTickersArray() {
        JSONArray jsonArray = new JSONArray();
        try {
            PreparedStatement pStatement = _connection.prepareStatement(
                    "SELECT ticker from tickers;"
            );
            ResultSet rSet = pStatement.executeQuery();
            while(rSet.next()){
                Ticker ticker = new Ticker(rSet.getString("ticker"));
                jsonArray.add(ticker);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonArray;
    }
    
}
