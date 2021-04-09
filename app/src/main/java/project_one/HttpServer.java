package project_one;

import java.io.IOException;
import java.net.Socket;

public class HttpServer {

    private ServerSocket _ssock;

    private enum Status {
        INIT,
        STARTED,
        FINISHED
    }

    private volatile Status _status;


    private RequestDispatcher _reqD;
    private ExchangeExecutor _exE;

    private Thread _reqDThread;


    // CONSTRUCTORS
    public HttpServer(int port){
        App.logger.traceEntry("port: {}", port);
        
        _status = INIT;

        _reqD = new RequestDispatcher();

        try {
            _ssock = new ServerSocket(port);
        } catch(IOException ioE) { App.logEx(HttpServer,HttpServer, ioE); }



        App.logger.traceExit();
    }


    // METHODS
    public void start() {
        App.logger.traceEntry();

        if (_status==STARTED || _status==FINISHED) {
            throw new IllegalStateException(String.format("Server has incorrect status: %s", _status.toString()) );
        }

        _reqDThread = new Thread(null, _reqD, "RequestDispatcher");
        _status = STARTED;
        _reqDThread.start();

        App.logger.traceExit();
    }


    // INNER CLASSES
    class RequestDispatcher implements Runnable {


        public void run() {
            App.logger.traceEntry();

            while(!_status.FINSIHED){
                try {
                    Socket sclient;

                    while ((client = _ssock.accept()) != null) {
                        App.logger.info("incoming request");

                        BufferedReader in = new BufferedReader(new InputStreamReader(sclient.getInetAddress()));
                        PrintWriter out = new PrintWriter(ssock.getOutputStream());
                        Request req = new Request(in);
                        Exchange ex = new Exchange(req, out);
                        _exE.execute(ex);
                    }
                } catch (IOException ioE) { App.logEx(HttpServer, start, ioE); }
            } // while

            App.logger.traceExit();
        } // run

    }







}