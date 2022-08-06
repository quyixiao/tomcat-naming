package com.sun.naming.internal;

import com.alibaba.fastjson.JSON;
import com.sun.naming.internal.VersionHelper;

import javax.naming.ConfigurationException;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.WeakHashMap;

public class Test3 {

    private static final WeakHashMap<Object, Hashtable<? super String, Object>>
            propertiesCache = new WeakHashMap<>(11);

    private static final String PROVIDER_RESOURCE_FILE_NAME =
            "jndiprovider.properties";


    private static final VersionHelper helper =
            VersionHelper.getVersionHelper();

    public static void main(String[] args)  throws Exception{

        System.out.println(getProviderResource("bbb"));
    }

    public static Hashtable<? super String, Object>
    getProviderResource(Object obj)
            throws NamingException
    {
        if (obj == null) {
            return (new Hashtable<>(1));
        }
        synchronized (propertiesCache) {
            Class<?> c = Test3.class;

            Hashtable<? super String, Object> props =
                    propertiesCache.get(c);
            if (props != null) {
                return props;
            }
            props = new Properties();

            InputStream istream =
                    helper.getResourceAsStream(c, PROVIDER_RESOURCE_FILE_NAME);

            if (istream != null) {
                try {
                    ((Properties)props).load(istream);
                } catch (IOException e) {
                    NamingException ne = new ConfigurationException(
                            "Error reading provider resource file for " + c);
                    ne.setRootCause(e);
                    throw ne;
                }
            }
            propertiesCache.put(c, props);
            return props;
        }
    }


}
