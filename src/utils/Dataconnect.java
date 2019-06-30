package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dataconnect {
    public static String connect(String sql,String modelType) {
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名studb
        String url = "jdbc:mysql://localhost:3306/ruleVerify?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "root";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
//            if (!con.isClosed())
//                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
//            sql = "select * from mapping";

            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
//            System.out.println("-----------------");
//            System.out.println("执行结果如下所示:");
//            System.out.println("-----------------");
//            System.out.println("姓名" + "\t" + "地址");
//            System.out.println("-----------------");
            String sysmlId = null;
            String aadlId = null;

            String simulinkId = null;

            while (rs.next()) {
                aadlId = rs.getString("aadl_id");
                sysmlId = rs.getString("sysml_id");
                simulinkId = rs.getString("simulink_id");
                if(modelType.equals("aadl"))
                    return aadlId;
                if(modelType.equals("sysml"))
                    return sysmlId;
                if(modelType.equals("simulink"))
                    return simulinkId;

//                System.out.println(aadlId + "\t" + sysmlId + "\t" + simulinkId);
            }
            rs.close();
            con.close();
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
//            System.out.println("数据库数据成功获取！！");
        }
        return null;
    }

    public static void main(String[] args) {
        String sql="select * from mapping where sysml_id=\"966564649\"";
        System.out.println(connect(sql,"aadl"));
    }
}

