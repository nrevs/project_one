package project_one;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Headers implements Map<String, List<String>> {

    HashMap<String, List<String>> map;

    Headers() {
        map = new HashMap<String, List<String>>(32);
    }

    private String normalize(String key) {
        App.logger.traceEntry(key);
        if (key == null) {
            return null;
        }
        int len = key.length();
        if (len == 0) {
            return App.logger.traceExit(key);
        }
        char[] b = key.toCharArray();
        if (b[0] >= 'a' && b[0] <= 'z') {
            b[0] = (char)(b[0] - ('a' - 'A'));
        } else if (b[0] == '\r' || b[0] == '\n') {
            throw new IllegalArgumentException("illegal character in key");
        }

        for (int i=1; i<len; i++) {
            if (b[i] >= 'A' && b[i] <= 'Z') {
                b[i] = (char) (b[i] + ('a' - 'A'));
            } else if (b[i] == '\r' || b[i] == '\n') {
                throw new IllegalArgumentException("illegal character in key");
            }
        }
        return App.logger.traceExit(new String(b));
    }

    public int size() {
        App.logger.traceEntry();
        return App.logger.traceExit(map.size());
    }

    public boolean isEmpty() {
        App.logger.traceEntry();
        return App.logger.traceExit(map.isEmpty());
    }

    public boolean containsKey(Object key) {
        App.logger.traceEntry();
        if (key == null) {
            return App.logger.traceExit(false);
        }
        if (!(key instanceof String)) {
            return false;
        }
        return App.logger.traceExit(map.containsKey(normalize((String)key)));
    }

    public boolean containsValue(Object value) {
        App.logger.traceEntry();
        return App.logger.traceExit(map.containsValue(value));
    }

    public List<String> get(Object key) {
        App.logger.traceEntry();
        return App.logger.traceExit(map.get(normalize((String)key)));
    }

    public String getFirst(String key) {
        App.logger.traceEntry();
        List<String> l = map.get(normalize(key));
        if (l == null) {
            return null;
        }
        return App.logger.traceExit(l.get(0));
    }

    public List<String> put(String key, List<String> value) {
        App.logger.traceEntry();
        for (String v : value) {
            checkValue(v);
        }
        return App.logger.traceExit(map.put(normalize(key), value));
    }

    public void add(String key, String value) {
        App.logger.traceEntry();

        String k = normalize(key);
        List<String> l = map.get(k);
        if (l==null) {
            l = new LinkedList<String>();
            map.put(k,l);
        }
        l.add(value);
        App.logger.traceExit();
    }

    private static void checkValue(String value) {
        App.logger.traceEntry();

        int len = value.length();
        for (int i=0; i<len; i++) {
            char c = value.charAt(i);
            if (c == '\r') {
                if (i >= len - 2) {
                    throw new IllegalArgumentException("Illegal CR found in header");
                }
                char c1 = value.charAt(i+1);
                char c2 = value.charAt(i+2);
                if (c1 != '\n') {
                    throw new IllegalArgumentException("Illegal char found after CR in header");
                }
                if (c1 != ' ' && c2 != '\t') {
                    throw new IllegalArgumentException("No whitespace found after CRLF in header");
                }
                i+=2;
            } else if (c == '\n') {
                throw new IllegalArgumentException("Illegal LF found in header");
            }

        }
        App.logger.traceExit();
    }

    public void set(String key, String value) {
        App.logger.traceEntry();
        LinkedList<String> l = new LinkedList<String>();
        l.add(value);
        put(key, l);
        App.logger.traceExit();
    }

    public List<String> remove(Object key) {
        App.logger.traceEntry();
        return App.logger.traceExit(map.remove(normalize((String)key)));
    }

    public void putAll(Map<? extends String, ? extends List<String>> t) {
        App.logger.traceEntry();
        map.putAll(t);
        App.logger.traceExit();
    }

    public void clear() {
        App.logger.traceEntry();
        map.clear();
        App.logger.traceExit();
    }

    public Set<String> keySet() {
        App.logger.traceEntry();
        return App.logger.traceExit(map.keySet());
    }

    public Collection<List<String>> values() {
        App.logger.traceEntry();
        return App.logger.traceExit(map.values());
    }

    public Set<Map.Entry<String, List<String>> > entrySet() {
        App.logger.traceEntry();
        return App.logger.traceExit(map.entrySet());
    }

    public boolean equals(Object o) {
        App.logger.traceEntry();
        return App.logger.traceExit(map.equals(o));
    }

    public int hashCode() {
        App.logger.traceEntry();
        return App.logger.traceExit(map.hashCode());
    }
}
