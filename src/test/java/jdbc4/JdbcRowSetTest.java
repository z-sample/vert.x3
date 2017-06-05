package jdbc4;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.junit.Before;
import org.junit.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;

/**
 * @author Zero
 *         Created on 2017/4/6.
 */
public class JdbcRowSetTest {

    InitialContext context;
//    @Before
    public void test() throws Exception {
        LocateRegistry.createRegistry(1099);
        context = new InitialContext();
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("test");
        dataSource.setPort(3306);
        context.rebind("ds", dataSource);
    }

    @Test
    public void test1() throws SQLException {
        RowSetFactory rowSetFactory = RowSetProvider.newFactory();//用缺省的RowSetFactory 实现
        //avax.sql.RowSet继承自ResultSet, RowSet的子接口有CachedRowSet, FilteredRowSet, JdbcRowSet, JoinRowSet, WebRowSet, 其中只有JdbcRowSet需要保持与数据库的连接, 其他都是离线RowSet.
        CachedRowSet rowSet = rowSetFactory.createCachedRowSet();
        //创建一个 RowSet 对象，配置数据库连接属性
        rowSet.setUrl("jdbc:mysql://localhost:3306/test");
        rowSet.setUsername("root");
        rowSet.setPassword("");

//        rowSet.setDataSourceName("ds");

        rowSet.setCommand("select * from user where username=?");
        rowSet.setString(1,"qq");
        rowSet.execute();
        System.out.println(rowSet);
        while (rowSet.next()) {
            for (int i = 1; i <= rowSet.getMetaData().getColumnCount(); i++) {
                System.out.println(rowSet.getObject(i)); //$NON-NLS-1$
            }
        }
        //
        rowSet.close();
    }
}
