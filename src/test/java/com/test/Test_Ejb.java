package com.test;


import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.deploy.ContextEjb;
import org.apache.catalina.deploy.ResourceBase;
import org.apache.naming.EjbRef;
import org.apache.naming.LookupRef;
import org.apache.naming.NamingContext;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;


@Slf4j
public class Test_Ejb {
    public static void main(String[] args) throws Exception {
        Hashtable<String, Object> contextEnv = new Hashtable<String, Object>();
        NamingContext namingContext = new NamingContext(contextEnv, "/");
        NamingContext envCtx = namingContext;


        // EJB references
        ContextEjb[] ejbs = new ContextEjb[1];
        ContextEjb ejb = new ContextEjb();
        ejb.setDescription("User Bean");
        ejb.setName("UserBean");
        ejb.setType("Session");
        ejb.setHome("com.test.lz.UserHome");
        ejb.setRemote("com.test.lz.User");
        ejbs[0] = ejb;

        for (int i = 0; i < ejbs.length; i++) {
            addEjb(envCtx, ejbs[i]);
        }

        Object o = envCtx.lookup("UserBean");

        System.out.println(o);
    }


    /**
     * Set the specified EJBs in the naming context.
     */
    public static void addEjb(NamingContext envCtx, ContextEjb ejb) {
        Reference ref = lookForLookupRef(ejb);
        if (ref == null) {
            // Create a reference to the EJB.
            ref = new EjbRef(ejb.getType(), ejb.getHome(), ejb.getRemote(), ejb.getLink());
            // Adding the additional parameters, if any
            Iterator<String> params = ejb.listProperties();
            while (params.hasNext()) {
                String paramName = params.next();
                String paramValue = (String) ejb.getProperty(paramName);
                StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
                ref.add(refAddr);
            }
        }

        try {
            createSubcontexts(envCtx, ejb.getName());
            envCtx.bind(ejb.getName(), ref);
        } catch (NamingException e) {
            log.error("naming.bindFailed", e);
        }
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
