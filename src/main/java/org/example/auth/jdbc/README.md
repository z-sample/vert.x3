# JDBC 授权模式

JDBC 授权模式需要关系型数据库支持, 比如MySQL.



[查看完整源码](https://github.com/vert-x3/vertx-auth/blob/master/vertx-auth-jdbc/src/main/java/io/vertx/ext/auth/jdbc/JDBCAuth.java)

```
@VertxGen
public interface JDBCAuth extends AuthProvider {

  /**
   * The default query to be used for authentication
   */
  String DEFAULT_AUTHENTICATE_QUERY = "SELECT PASSWORD, PASSWORD_SALT FROM USER WHERE USERNAME = ?";

  /**
   * The default query to retrieve all roles for the user
   */
  String DEFAULT_ROLES_QUERY = "SELECT ROLE FROM USER_ROLES WHERE USERNAME = ?";

  /**
   * The default query to retrieve all permissions for the role
   */
  String DEFAULT_PERMISSIONS_QUERY = "SELECT PERM FROM ROLES_PERMS RP, USER_ROLES UR WHERE UR.USERNAME = ? AND UR.ROLE = RP.ROLE";

  /**
   * The default role prefix
   */
  String DEFAULT_ROLE_PREFIX = "role:";

  static JDBCAuth create(JDBCClient client) {
    return new JDBCAuthImpl(client);
  }

  JDBCAuth setAuthenticationQuery(String authenticationQuery);

  JDBCAuth setRolesQuery(String rolesQuery);

  JDBCAuth setPermissionsQuery(String permissionsQuery);

  JDBCAuth setRolePrefix(String rolePrefix);

  @GenIgnore
  JDBCAuth setHashStrategy(JDBCHashStrategy strategy);
   ...
}
```

JDBCAuth 接口定义了默认的授权查询SQL, 当然自己可以修改.

**由上面代码可以推导出数据库表结构大概如下:**

USER表

字段 | 类型
---|---
USERNAME  | varchar
PASSWORDPA | varchar
PASSWORD_SALT |varchar
...|...

USER_ROLES表
字段 | 类型
---|---
ROLE  | varchar
USERNAME | varchar

ROLES_PERMS表
字段 | 类型
---|---
PERM   | varchar
ROLE | varchar

也可以查看[测试代码](https://github.com/vert-x3/vertx-auth/blob/master/vertx-auth-jdbc/src/test/java/io/vertx/ext/auth/test/jdbc/JDBCAuthTest.java)中的sql


```java
  static {
    SQL.add("drop table if exists user;");
    SQL.add("drop table if exists user_roles;");
    SQL.add("drop table if exists roles_perms;");
    SQL.add("create table user (username varchar(255), password varchar(255), password_salt varchar(255) );");
    SQL.add("create table user_roles (username varchar(255), role varchar(255));");
    SQL.add("create table roles_perms (role varchar(255), perm varchar(255));");

    SQL.add("insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');");
    SQL.add("insert into user_roles values ('tim', 'dev');");
    SQL.add("insert into user_roles values ('tim', 'admin');");
    SQL.add("insert into roles_perms values ('dev', 'commit_code');");
    SQL.add("insert into roles_perms values ('dev', 'eat_pizza');");
    SQL.add("insert into roles_perms values ('admin', 'merge_pr');");

    // and a second set of tables with slight differences

    SQL.add("drop table if exists user2;");
    SQL.add("drop table if exists user_roles2;");
    SQL.add("drop table if exists roles_perms2;");
    SQL.add("create table user2 (user_name varchar(255), pwd varchar(255), pwd_salt varchar(255) );");
    SQL.add("create table user_roles2 (user_name varchar(255), role varchar(255));");
    SQL.add("create table roles_perms2 (role varchar(255), perm varchar(255));");

    SQL.add("insert into user2 values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');");
    SQL.add("insert into user_roles2 values ('tim', 'dev');");
    SQL.add("insert into user_roles2 values ('tim', 'admin');");
    SQL.add("insert into roles_perms2 values ('dev', 'commit_code');");
    SQL.add("insert into roles_perms2 values ('dev', 'eat_pizza');");
    SQL.add("insert into roles_perms2 values ('admin', 'merge_pr');");

  }

  @Test
  public void testAuthenticate() {
    JsonObject authInfo = new JsonObject();
    authInfo.put("username", "tim").put("password", "sausages");
    //用户认证,判断是否存在该用户
    authProvider.authenticate(authInfo, onSuccess(user -> {
      assertNotNull(user);
      testComplete();
    }));
    await();
  }



  @Test
  public void testAuthoriseNotHasRole() {
    JsonObject authInfo = new JsonObject();
    authInfo.put("username", "tim").put("password", "sausages");
    authProvider.authenticate(authInfo, onSuccess(user -> {
      assertNotNull(user);
      //判断是否是manager角色用户
      user.isAuthorised("role:manager", onSuccess(has -> {
        assertFalse(has);
        testComplete();
      }));
    }));
    await();
  }

  @Test
  public void testAuthoriseHasPermission() {
    JsonObject authInfo = new JsonObject();
    authInfo.put("username", "tim").put("password", "sausages");
    authProvider.authenticate(authInfo, onSuccess(user -> {
      assertNotNull(user);
      //判断是否拥有commit_code权限
      user.isAuthorised("commit_code", onSuccess(has -> {
        assertTrue(has);
        testComplete();
      }));
    }));
    await();
  }
```

## 源码中的例子

```
package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jdbc.JDBCAuth;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class AuthJDBCExamples {


  public void example5(Vertx vertx, JsonObject jdbcClientConfig) {

    JDBCClient jdbcClient = JDBCClient.createShared(vertx, jdbcClientConfig);

    JDBCAuth authProvider = JDBCAuth.create(jdbcClient);
  }

  public void example6(AuthProvider authProvider) {

    JsonObject authInfo = new JsonObject().put("username", "tim").put("password", "sausages");

    authProvider.authenticate(authInfo, res -> {
      if (res.succeeded()) {
        User user = res.result();
      } else {
        // Failed!
      }
    });
  }

  public void example7(User user) {

    user.isAuthorised("commit_code", res -> {
      if (res.succeeded()) {
        boolean hasPermission = res.result();
      } else {
        // Failed to
      }
    });

  }

  public void example8(User user) {

    user.isAuthorised("role:manager", res -> {
      if (res.succeeded()) {
        boolean hasRole = res.result();
      } else {
        // Failed to
      }
    });

  }

}
```


[查看源码](https://github.com/vert-x3/vertx-auth/blob/master/vertx-auth-jdbc/src/main/java/examples/AuthJDBCExamples.java)

