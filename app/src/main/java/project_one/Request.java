package project_one;



import java.io.BufferedReader;
import java.io.IOException;



class Request {

    BufferedReader _in;
    char[] buf;
    int pos;
    StringBuffer lineBuf;
    String _startLine;
    Headers _headers;

    final static byte CR = 13;
    final static byte LF = 10;


    Request(BufferedReader in) {
        App.logger.traceEntry();

        _in = in;
        try {
            do {
                _startLine = _in.readLine();
                if (_startLine == null) {
                    return;
                }
            } while (_startLine == null ? false : _startLine.equals(""));
        } catch (IOException ioE) {
            App.logEx("Request", "Request", ioE);
        }

        App.logger.traceExit();
    }

    Headers headers() throws IOException {
        App.logger.traceEntry();

        if (_headers != null) {
            return _headers;
        }
        _headers = new Headers();

        char s[] = new char[10];
        int len = 0;

        int firstc = _in.read();

        if (firstc == CR || firstc == LF) {
            int c = _in.read();
            if (c == CR || c == LF) {
                return _headers;
            }
            s[0] = (char)firstc;
            len = 1;
            firstc = c;
        }

        while (firstc != LF && firstc != CR && firstc >= 0) {
            int keyend = -1;
            int c;
            boolean inKey = firstc > ' ';
            s[len++] = (char) firstc;
            parseloop:{
                while((c = _in.read()) >= 0) {
                    switch (c) {
                        case ':':
                            if (inKey && len>0)
                                keyend = len;
                            inKey = false;
                            break;
                        case '\t':
                            c = ' ';
                        case CR:
                        case LF:
                            firstc = _in.read();
                            if (c == CR && firstc == LF) {
                                firstc = _in.read();
                                if (firstc == CR)
                                    firstc = _in.read();
                            }
                            if (firstc == LF || firstc == CR || firstc > ' ')
                                break parseloop;
                            c = ' ';
                            break;
                    }
                    if (len >= s.length) {
                        char ns[] = new char[s.length * 2];
                        System.arraycopy(s, 0, ns, 0, len);
                        s = ns;
                    }
                    s[len++] = (char)c;
                }
                firstc = -1;
            }
            while (len > 0 && s[len-1] <= ' ')
                len--;
            String k;
            if (keyend <= 0) {
                k = null;
                keyend = 0;
            } else {
                k = String.copyValueOf(s, 0, keyend);
                if (keyend < len && s[keyend] == ':')
                    keyend++;
                while (keyend < len && s[keyend] <= ' ')
                    keyend++;
            }
            String v;
            if (keyend >= len)
                v = new String();
            else
                v = String.copyValueOf(s, keyend, len-keyend);

            if (_headers.size() >= App.maxReqHeaders) {
                throw new IOException("Maximum number of request headers (" +
                        "httpsServer.maxReqHeaders) exceeded, " +
                        App.maxReqHeaders + ".");

                // logger
            }
            _headers.add(k,v);
            len = 0;
        }
        // while (firstc != LF && firstc != CR && firstc >= 0) 

        return App.logger.traceExit(_headers);
    }

}