package cs.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;


public class PropertyUtil {
	
    private String properiesName = "";
    private Properties properties;
    private URI uri;

    public PropertyUtil() {

    }
    
    public PropertyUtil(String fileName) {
        this.properiesName = fileName;
        properties = getProperties();
        
    }    

    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = PropertyUtil.class.getClassLoader().getResourceAsStream(properiesName);
            p.load(is);
            uri = this.getClass().getResource("/"+properiesName).toURI();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }
    
    public String readProperty(String key) {	
    	return properties.getProperty(key);       
    }
    
    public List<String> getList(String prefix) {   	
		if (properties == null || prefix == null) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				list.add(properties.getProperty(key));
			}
		}
		return list;
	}
	
	public Set<String> getSet(String prefix) {
		if (properties == null || prefix == null) {
			return Collections.emptySet();
		}
		Set<String>set=new TreeSet<String>();
		Enumeration<?> en = properties.propertyNames();
		String key;
		while (en.hasMoreElements()) {
			key = (String) en.nextElement();
			if (key.startsWith(prefix)) {
				set.add(properties.getProperty(key));
			}
		}
		return set;
	}

	public Map<String,Object> getAllProperty(){  
        Map<String,Object> map=new HashMap<String,Object>();    
        Enumeration<?> enu = properties.propertyNames();    
        while (enu.hasMoreElements()) {    
            String key = (String) enu.nextElement();    
            String value = properties.getProperty(key);    
            map.put(key, value);    
        }    
        return map;    
    } 
	
	
    public void writeProperty(String key, String value) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(uri));
            properties.setProperty(key, value);
            properties.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
