package com.test;


import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.deploy.ContextEnvironment;
import org.apache.catalina.deploy.ResourceBase;
import org.apache.naming.LookupRef;
import org.apache.naming.NamingContext;

import javax.naming.Context;
import javax.naming.NamingException;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.StringTokenizer;


@Slf4j
public class Test2 {
    public static void main(String[] args) throws Exception {
        Hashtable<String, Object> contextEnv = new Hashtable<String, Object>();
        NamingContext namingContext = new NamingContext(contextEnv, "/");
        NamingContext envCtx = namingContext;

        // Environment entries
        ContextEnvironment[] contextEnvironments = new ContextEnvironment[1];
        ContextEnvironment contextEnvironment = new ContextEnvironment();
        contextEnvironment.setValue("mysql");
        contextEnvironment.setType("java.lang.String");
        contextEnvironment.setName("sql_type");
        contextEnvironments[0] = contextEnvironment;
        for (int i = 0; i < contextEnvironments.length; i++) {
            addEnvironment(envCtx, contextEnvironments[i]);
        }
        Object o = envCtx.lookup("sql_type");


        System.out.println(o);
    }


    /**
     * Set the specified environment entries in the naming context.
     */
    public static void addEnvironment(NamingContext envCtx, ContextEnvironment env) {

        Object value = lookForLookupRef(env);

        if (value == null) {
            // Instantiating a new instance of the correct object type, and
            // initializing it.
            String type = env.getType();
            try {
                if (type.equals("java.lang.String")) {
                    value = env.getValue();
                } else if (type.equals("java.lang.Byte")) {
                    if (env.getValue() == null) {
                        value = Byte.valueOf((byte) 0);
                    } else {
                        value = Byte.decode(env.getValue());
                    }
                } else if (type.equals("java.lang.Short")) {
                    if (env.getValue() == null) {
                        value = Short.valueOf((short) 0);
                    } else {
                        value = Short.decode(env.getValue());
                    }
                } else if (type.equals("java.lang.Integer")) {
                    if (env.getValue() == null) {
                        value = Integer.valueOf(0);
                    } else {
                        value = Integer.decode(env.getValue());
                    }
                } else if (type.equals("java.lang.Long")) {
                    if (env.getValue() == null) {
                        value = Long.valueOf(0);
                    } else {
                        value = Long.decode(env.getValue());
                    }
                } else if (type.equals("java.lang.Boolean")) {
                    value = Boolean.valueOf(env.getValue());
                } else if (type.equals("java.lang.Double")) {
                    if (env.getValue() == null) {
                        value = Double.valueOf(0);
                    } else {
                        value = Double.valueOf(env.getValue());
                    }
                } else if (type.equals("java.lang.Float")) {
                    if (env.getValue() == null) {
                        value = Float.valueOf(0);
                    } else {
                        value = Float.valueOf(env.getValue());
                    }
                } else if (type.equals("java.lang.Character")) {
                    if (env.getValue() == null) {
                        value = Character.valueOf((char) 0);
                    } else {
                        if (env.getValue().length() == 1) {
                            value = Character.valueOf(env.getValue().charAt(0));
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } else {
                    value = constructEnvEntry(env.getType(), env.getValue());
                    if (value == null) {
                        log.error("naming.invalidEnvEntryType", env.getName());
                    }
                }
            } catch (NumberFormatException e) {
                log.error("naming.invalidEnvEntryValue", env.getName());
            } catch (IllegalArgumentException e) {
                log.error("naming.invalidEnvEntryValue", env.getName());
            }
        }

        // Binding the object to the appropriate name
        if (value != null) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("naming.addEnvEntry" + env.getName());
                }
                createSubcontexts(envCtx, env.getName());
                envCtx.bind(env.getName(), value);
            } catch (NamingException e) {
                log.error("naming.invalidEnvEntryValue", e);
            }
        }
    }


    private static Object constructEnvEntry(String type, String value) {
        try {
            Class<?> clazz = Class.forName(type);
            Constructor<?> c = null;
            try {
                c = clazz.getConstructor(String.class);
                return c.newInstance(value);
            } catch (NoSuchMethodException e) {
                // Ignore
            }

            if (value.length() != 1) {
                return null;
            }
            try {
                c = clazz.getConstructor(char.class);
                return c.newInstance(Character.valueOf(value.charAt(0)));
            } catch (NoSuchMethodException e) {
                // Ignore
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }


    /**
     * Create all intermediate subcontexts.
     */
    private static void createSubcontexts(Context ctx, String name)
            throws NamingException {
        Context currentContext = ctx;
        StringTokenizer tokenizer = new StringTokenizer(name, "/");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if ((!token.equals("")) && (tokenizer.hasMoreTokens())) {
                try {
                    currentContext = currentContext.createSubcontext(token);
                } catch (NamingException e) {
                    // Silent catch. Probably an object is already bound in
                    // the context.
                    currentContext =
                            (Context) currentContext.lookup(token);
                }
            }
        }
    }


    private static LookupRef lookForLookupRef(ResourceBase resourceBase) {
        String lookupName = resourceBase.getLookupName();
        if ((lookupName != null && !lookupName.equals(""))) {
            return new LookupRef(resourceBase.getType(), lookupName);
        }
        return null;
    }


}
