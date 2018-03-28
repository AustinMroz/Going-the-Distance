package Austin.Mroz;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.io.IOException;

public class Json implements Iterable<Json>{
    public static enum Type {ARRAY, DICT, STRING};
    public Type type;
    private ArrayList<Json> al;
    private TreeMap<String,Json> tm;
    private String s;

    public Json get(int i) {
	if(type!=Type.ARRAY)
	    throw new IllegalStateException("Parsed Json was of type "+type);
	return al.get(i);
    }
    public Json get(String s) {
	if(type!=Type.DICT)
	    throw new IllegalStateException("Parsed Json was of type "+type);
	return tm.get(s);
    }
    public String s() {
	if(type!=Type.STRING)
	    throw new IllegalStateException("Parsed Json was of type "+type);
	return s;
    }
    @Override
    public Iterator<Json> iterator() {
	if(type!=Type.ARRAY)
	    throw new IllegalStateException("Parsed Json was of type "+type);
	return al.iterator();
    }
    @Override
    public String toString() {
	//switch is too bloated for 3 cases
	if(type==Type.ARRAY)
	    return '*'+al.toString();
	else if (type==Type.DICT)
	    return '^'+tm.toString();
	else
	    return '#'+s;
    }

    public static String stringify(InputStream is) throws IOException {
	ByteArrayOutputStream result = new ByteArrayOutputStream();
	byte[] buffer = new byte[1024];
	int length;
	while ((length = is.read(buffer)) != -1) {
	    result.write(buffer, 0, length);
	}
	// StandardCharsets.UTF_8.name() > JDK 7
	String res = "";
	try {
	    res = result.toString("UTF-8");
	} catch(Exception e) {
	    System.err.println("UTF-8 encoding not supported");
	}
	return res;
    }
    public static Json read(byte[] b) {
	    return read(new JP(new String(b)));
    }
    public static Json read(InputStream is) throws IOException {
	return read(new JP(is));
    }
    public static Json read(JP j) {
	//determine if array, dict, or neither
	char c;
	do {
	    c = j.get();
	} while(Character.isWhitespace(c));
	if(c=='[')
	    return readArray(j);
	if(c=='{')
	    return readMap(j);
	//is just string
	j.ind--;
	return readString(j);
    }
    private static Json readString(JP j) {
	char c = j.get();
	StringBuilder sb = new StringBuilder();
	if(c=='"') {//is actual string
	    //bit of uglyness for escaped quotes
	    do {
		while((c=j.get())!='"')
		    sb.append(c);
	    } while(j.ind>=2 && j.json.charAt(j.ind-2)=='\\');
	    Json ret = new Json();
	    ret.s = sb.toString();
	    ret.type=Type.STRING;
	    return ret;
	}
	//is unknown type that'll be read as string anyways
	//probably boolean or int, but my problem
	do {
	    sb.append(c);
	    c = j.get();
	} while(c!=',' && c!='"' && c!=']' && c!='}'
		&& c!=':' && !Character.isWhitespace(c));
	//panic somewhat as we overread one character...
	j.ind--;
	Json ret = new Json();
	ret.s = sb.toString();
	ret.type=Type.STRING;
	return ret;
    }
    private static Json readArray(JP j) {
	ArrayList<Json> a = new ArrayList<>();
	j.trim();
	a.add(read(j));
	j.trim();
	while(j.get()==',')
	    a.add(read(j));
	assert(j.peekback()==']');
	Json ret = new Json();
	ret.al = a;
	ret.type=Type.ARRAY;
	return ret;
    }
    private static Json readMap(JP j) {
	TreeMap<String,Json> tm = new TreeMap<>();
	j.trim();
	do {
	    j.trim();
	    String key = readString(j).s();
	    j.trim();
	    char c = j.get();
	    assert(c==':');
	    j.trim();
	    Json value = read(j);
	    j.trim();
	    tm.put(key,value);
	} while(j.get()==',');
	//System.out.println(j.peekback());
	assert(j.peekback()=='}');
	Json ret = new Json();
	ret.tm = tm;
	ret.type=Type.DICT;
	return ret;
    }
    private static class JP {
	String json;
	int ind;
	private JP(String s) {
	    json=s+" ";
	    ind=0;
	    //System.out.println("json"+s);
	}
	private JP(InputStream is) throws IOException{
	    ind=0;
	    json = stringify(is);
	    //debugging:
	    //System.out.println(json);
	}
	private char peek() {
	    return json.charAt(ind);
	}
	private char peekback() {
	    return json.charAt(ind-1);
	}
	private char get() {
	    return json.charAt(ind++);
	}
	private void trim() {
	    while(Character.isWhitespace(json.charAt(ind++)));
	    ind--;
	}
    }
}
