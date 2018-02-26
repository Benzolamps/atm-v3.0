package com.feicuiedu.atm.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.feicuiedu.atm.exception.BenzolampsException;
import com.feicuiedu.atm.util.CommonUtils;

/**
 * 用于连接数据库的工具类, 需要首先配置好dbconfig.properties
 * 
 * @author Benzolamps
 *
 */
public class DatabaseUtils {

    private static DatabaseProperties properties; // DatabaseProperties对象    
    private static Connection connection; // 连接对象    
    private static boolean inited = false; // 标识是否已注册驱动程序

    /**
     * 初始化
     * 
     * @param path 文件名
     * @throws ClassNotFoundException
     */
    private static void init(String path) throws ClassNotFoundException {
        
        if (!inited) { // 保证代码只执行一次
                
            inited = true;     
            properties = new DatabaseProperties(CommonUtils.fileToJSON(path));  
            Class.forName(properties.getDriver()); // 注册驱动程序
        }
    }

    /**
     * 构造DatabaseUtils对象
     * 
     * @throws ClassNotFoundException 
     */
    public DatabaseUtils() throws ClassNotFoundException {
        
        super();
        DatabaseUtils.init(CommonUtils.getClassPath() + "dbconfig.json");
    }
    
    /**
     * 获取Properties
     * 
     * @return
     * @throws ClassNotFoundException
     */
    public static DatabaseProperties getProperties() throws ClassNotFoundException {
        
        DatabaseUtils.init(CommonUtils.getClassPath() + "dbconfig.json");
        return properties;
    }

    /**
     * 根据对象类型执行SQL语句, 并返回指定类型对象的List
     * 
     * @param clazz 对象的类型
     * @param sql   SQL语句
     * @param args  参数列表
     * @param <T>   对象的类型
     * @return 指定类型对象的List
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public <T> List<T> executeList(Class<T> clazz, String sql, Object... args)
        throws SQLException, ReflectiveOperationException {
        
        connect();

        List<T> list = new ArrayList<>();
        ResultSet resultSet = generateStatement(sql, args).executeQuery();
        
        while (resultSet.next()) { // 获得每一行的对象

            T t = getCurrentResultData(clazz, resultSet);
            list.add(t);
        }
        
        disconnect();
        return list;
    }
    
    /**
     * 执行SQL语句, 返回合适的结果 (查询一条记录)
     * 如果clazz是位于com.feicuiedu.entity包下, 将数据封装到对象中返回
     * 否则, 返回表格中第一行第一个字段的数据
     * 如果无数据, 返回null
     *
     * @param clazz 对象的类型
     * @param sql   SQL语句
     * @param args  参数
     * @param <T>   对象的类型
     * @return 指定类型的对象
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public <T> T executeObject(Class<T> clazz, String sql, Object... args)
        throws SQLException, ReflectiveOperationException {
        
        // 查看是不是com.feicuiedu.atm.entity包下的类
        if (clazz.getName().contains("com.feicuiedu.atm.entity")) {
            
            // 查询列表取第一行
            List<T> list = executeList(clazz, sql, args);
            
            return list.size() == 0 ? null : list.get(0);      
              
        } else {
            // 获取第一行第一列的对象
            Object obj = executeObject0(sql, args);
            
            // 判断obj的类型是否和clazz兼容
            if (obj != null && clazz.isAssignableFrom(obj.getClass())) {
                
                return clazz.cast(obj);
                
            } else { // 不匹配
                String error = obj.getClass().getName() + "~~~" + clazz.getName();
                
                throw new BenzolampsException(error);
            }
        }
    }
    
    /**
     * 辅助executeObject
     */
    private Object executeObject0(String sql, Object... args) 
        throws SQLException, ClassNotFoundException {
        
        connect();
        
        ResultSet resultSet = generateStatement(sql, args).executeQuery();
        
        if (!resultSet.next()){ // 结果集不包含任何列, 
            return disconnect();
        }
            
        // 获取第一行第一列中的元素(猜想查询结果至少有一列)
        Object obj = resultSet.getObject(1); 
        
        disconnect();

        return obj;
    }
    
    /**
     * 执行SQL语句, 并返回受影响的行数
     * @param sql   SQL语句
     * @param args  参数列表
     * @return 行数
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public Integer executeCount(String sql, Object... args)
        throws SQLException, ReflectiveOperationException {
        
        connect();
        
        Integer row = generateStatement(sql, args).executeUpdate();

        disconnect();

        return row;
    }
    
    /**
     * 执行SQL语句, 并返回执行成功与否
     * @param clazz 对象的类型
     * @param sql   SQL语句
     * @param args  参数列表
     * @param <T>   对象的类型
     * @return 执行成功返回true, 否则返回falses
     * @throws SQLException
     * @throws ReflectiveOperationException
     */
    public Boolean execute(String sql, Object... args)
        throws SQLException, ReflectiveOperationException {
        
        connect();
        
        Boolean result = generateStatement(sql, args).execute();

        disconnect();

        return result;
    }
    
    /**
     * 获取结果集当前行对应的对象
     * @throws ReflectiveOperationException
     * @throws SQLException 
     */
    private <T> T getCurrentResultData(Class<T> clazz, ResultSet resultSet) 
        throws ReflectiveOperationException, SQLException {
        
        Constructor<T> constructor = clazz.getDeclaredConstructor(); // 获取构造器
        
        constructor.setAccessible(true); // 设置构造器访问权限
        
        T t = constructor.newInstance(); // 创建T对象
        
        ResultSetMetaData metaData = resultSet.getMetaData(); // 获取MetaData

        // 遍历
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            
            DatabaseTable table = properties.getTableByClass(clazz); // 获取表
            String column = metaData.getColumnName(i + 1); // 获取列名
            Object value = resultSet.getObject(i + 1); // 获取值
            DatabaseColumn column1 = table.getColumnByName(column); // 获取列
            Field field = null; // 变量
            if (column1 != null) {
                field = clazz.getDeclaredField(column1.getField());
            }
            
            if (field != null) { // 设置变量
                
                field.setAccessible(true);
                
                field.set(t, value);
             }
        }

        return t;
    }

    /**
     * 
     */
    private PreparedStatement generateStatement(String sql, Object... args)
        throws SQLException {
        
        PreparedStatement statement = null;

        statement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            
            statement.setObject(i + 1, args[i]);
        }

        return statement;
    }

    /**
     * 创建连接
     * 
     * @throws SQLException
     */
    private void connect() throws SQLException{
        
        if (connection == null || connection.isClosed()) {
        
            String url = properties.getUrl();
            String username = properties.getUserName();
            String password = properties.getPassword();
            
            connection = DriverManager.getConnection(url, username, password);
   
        }
    }

    /**
     * 关闭连接
     * 
     * @throws SQLException
     */
    private Object disconnect() throws SQLException {
        
        if (connection == null || connection.isClosed()) {
            return null;
        }
        
        connection.close();
        
        return null;
    }
    
}
