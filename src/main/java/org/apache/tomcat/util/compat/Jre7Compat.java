/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.tomcat.util.compat;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.zip.GZIPOutputStream;

class Jre7Compat extends JreCompat {

    private static final int RUNTIME_MAJOR_VERSION = 7;

    private static final Method forLanguageTagMethod;
    private static final Constructor<GZIPOutputStream> gzipOutputStreamConstructor;
    private static final Method callableStatementGetObjectIndex;
    private static final Method callableStatementGetObjectName;
    private static final Method connectionSetSchema;
    private static final Method connectionGetSchema;
    private static final Method connectionAbort;
    private static final Method connectionSetNetworkTimeout;
    private static final Method connectionGetNetworkTimeout;
    private static final Method databaseMetaDataGetPseudoColumns;
    private static final Method databaseMetaDataGeneratedKeyAlwaysReturned;
    private static final Method resultSetGetObjectIndex;
    private static final Method resultSetGetObjectName;
    private static final Method statementCloseOnCompletion;
    private static final Method statementIsCloseOnCompletion;

    static {
        Method m1 = null;
        Method m2 = null;
        Method m3 = null;
        Method m4 = null;
        Method m5 = null;
        Method m6 = null;
        Method m7 = null;
        Method m8 = null;
        Method m9 = null;
        Method m10 = null;
        Method m11 = null;
        Method m12 = null;
        Method m13 = null;
        Method m14 = null;
        Constructor<GZIPOutputStream> c = null;
        try {
            m1 = Locale.class.getMethod("forLanguageTag", String.class);
            c = GZIPOutputStream.class.getConstructor(OutputStream.class, boolean.class);
            m2 = CallableStatement.class.getMethod("getObject", int.class, Class.class);
            m3 = CallableStatement.class.getMethod("getObject", String.class, Class.class);
            m4 = Connection.class.getMethod("setSchema", String.class);
            m5 = Connection.class.getMethod("getSchema");
            m6 = Connection.class.getMethod("abort", Executor.class);
            m7 = Connection.class.getMethod("setNetworkTimeout", Executor.class, int.class);
            m8 = Connection.class.getMethod("getNetworkTimeout");
            m9 = DatabaseMetaData.class.getMethod("getPseudoColumns");
            m10 = DatabaseMetaData.class.getMethod("generatedKeyAlwaysReturned");
            m11 = ResultSet.class.getMethod("getObject", int.class, Class.class);
            m12 = ResultSet.class.getMethod("getObject", String.class, Class.class);
            m13 = Statement.class.getMethod("closeOnCompletion");
            m14 = Statement.class.getMethod("isCloseOnCompletion");
        } catch (SecurityException e) {
            // Should never happen
        } catch (NoSuchMethodException e) {
            // Expected on Java < 7
        }
        forLanguageTagMethod = m1;
        gzipOutputStreamConstructor = c;
        callableStatementGetObjectIndex = m2;
        callableStatementGetObjectName = m3;
        connectionSetSchema = m4;
        connectionGetSchema = m5;
        connectionAbort = m6;
        connectionSetNetworkTimeout = m7;
        connectionGetNetworkTimeout = m8;
        databaseMetaDataGetPseudoColumns = m9;
        databaseMetaDataGeneratedKeyAlwaysReturned = m10;
        resultSetGetObjectIndex = m11;
        resultSetGetObjectName = m12;
        statementCloseOnCompletion = m13;
        statementIsCloseOnCompletion = m14;
    }


    static boolean isSupported() {
        return forLanguageTagMethod != null;
    }


    // Java 7 methods

    @Override
    public Locale forLanguageTag(String languageTag) {
        try {
            return (Locale) forLanguageTagMethod.invoke(null, languageTag);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }


    @Override
    public GZIPOutputStream getFlushableGZipOutputStream(OutputStream os) {
        try {
            return gzipOutputStreamConstructor.newInstance(os, Boolean.TRUE);
        } catch (InstantiationException e) {
            throw new UnsupportedOperationException(e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(e);
        } catch (InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(CallableStatement callableStatement, int parameterIndex, Class<T> type)
            throws SQLException {
        try {
            return (T) callableStatementGetObjectIndex.invoke(
                    callableStatement, Integer.valueOf(parameterIndex), type);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(CallableStatement callableStatement, String parameterName, Class<T> type)
            throws SQLException {
        try {
            return (T) callableStatementGetObjectName.invoke(callableStatement, parameterName, type);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public void setSchema(Connection connection, String schema) throws SQLException {
        try {
            connectionSetSchema.invoke(connection,  schema);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public String getSchema(Connection connection) throws SQLException {
        try {
            return (String) connectionGetSchema.invoke(connection);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public void abort(Connection connection, Executor executor) throws SQLException {
        try {
            connectionAbort.invoke(connection);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public void setNetworkTimeout(Connection connection, Executor executor, int milliseconds)
            throws SQLException {
        try {
            connectionSetNetworkTimeout.invoke(connection, executor, Integer.valueOf(milliseconds));
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public int getNetworkTimeout(Connection connection) throws SQLException {
        try {
            return ((Integer) connectionGetNetworkTimeout.invoke(connection)).intValue();
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public ResultSet getPseudoColumns(DatabaseMetaData databaseMetaData, String catalog,
            String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        try {
            return (ResultSet) databaseMetaDataGetPseudoColumns.invoke(databaseMetaData,
                    catalog, schemaPattern, tableNamePattern, columnNamePattern);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean generatedKeyAlwaysReturned(DatabaseMetaData databaseMetaData) throws SQLException {
        try {
            return ((Boolean) databaseMetaDataGeneratedKeyAlwaysReturned.invoke(databaseMetaData)).booleanValue();
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(ResultSet resultSet, int parameterIndex, Class<T> type)
            throws SQLException {
        try {
            return (T) resultSetGetObjectIndex.invoke(
                    resultSet, Integer.valueOf(parameterIndex), type);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(ResultSet resultSet, String parameterName, Class<T> type)
            throws SQLException {
        try {
            return (T) resultSetGetObjectName.invoke(resultSet, parameterName, type);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public void closeOnCompletion(Statement statement) throws SQLException {
        try {
            statementCloseOnCompletion.invoke(statement);
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public boolean isCloseOnCompletion(Statement statement) throws SQLException {
        try {
            return ((Boolean) statementIsCloseOnCompletion.invoke(statement)).booleanValue();
        } catch (IllegalArgumentException e) {
            throw new SQLException(e);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        } catch (InvocationTargetException e) {
            throw new SQLException(e);
        }
    }


    // Java 9 methods

    @Override
    public int jarFileRuntimeMajorVersion() {
        return RUNTIME_MAJOR_VERSION;
    }


    @Override
    public boolean isCommonsAnnotations1_1Available() {
        // True for all Java versions from 7 upwards
        // Java 7 and Java 8 include it.
        // Java 9 onwards does not include it and in that case the version
        // supplied by Tomcat will be used so it will still be available.
        return true;
    }
}
