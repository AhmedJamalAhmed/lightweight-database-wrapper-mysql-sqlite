# lightweight database wrapper for mysql and sqlite

FontIcon [![](https://jitpack.io/v/AhmedJamalAhmed/lightweight-database-wrapper-mysql-sqlite.svg)](https://jitpack.io/#AhmedJamalAhmed/lightweight-database-wrapper-mysql-sqlite)
======== 


Install
=======

To install the library add: 
 
   ```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
	        implementation 'com.github.gitjemy:zdb:v6'
	}
   ```  
## Usage
  
### create model 

```java
public class User implements ZSqlRow {
    
    private String name;
    private int id;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
}
```

### create table class 

```java
public class UsersTable extends ZTable<User> { 
    _String<User> name = new _String<>("name", 250,
    new WritableProperty<>("name", User::getName, User::setName));
       
    public UsersTable(JavaSeLink link) throws Exception {
        super(link, "UsersTable", new _ID_AI<>());
        register(name);
    }
      
    @Override
    public User createNewElement() {
        return new User(); 
    }
}
```
### use table class to save and retrive data to/from db

```java
class main{
    public static void main(String[] strings) { 
        try { 
            JavaSeLink link = new MysqlHelper("localhost", "DBName", "user", "pass");
           
            UsersTable usersTable = new UsersTable(link);
           
            User user = new User();
            user.setName("ahmed");
            usersTable.insert(user);
            user.setName("john");
           
            usersTable.update(user);
            
            List<User> list = usersTable.list(); // return all items
            
            List<User> listAhmeds = usersTable.list(usersTable.name.equal("ahmed"));
           
                    
            List<User> listA = usersTable.list(usersTable.name.equal("ahmed").or(usersTable.getID().equal(10)));
           
                    
            link.count(usersTable,usersTable.name.equal("ahmed"));
           
                    
            link.exist(usersTable.name.equal("ahmed"));
           
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


## support android db

