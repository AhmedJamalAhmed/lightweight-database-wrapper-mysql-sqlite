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
	        implementation 'com.github.AhmedJamalAhmed:lightweight-database-wrapper-mysql-sqlite:v6'
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
	
	    public UsersTable(Link link) {
	        super(link, "UsersTable", new _ID_AI<>());
	        register(name);
	    }
	
	    @Override
	    public User createNewElement() {
	        return new User();
	    }
	}
```
### use table class to save and retrive data to db

```java
	public static void main(String[] strings) {
	        Link link = new MysqlHelper("localhost", "DBName", "user", "pass", Throwable::printStackTrace);
	        UsersTable usersTable = new UsersTable(link);
	
	        try {
	            User user = new User();
	            user.setName("ahmed");
	
	            usersTable.insert(user);
	            user.setName("john");
	            
	            usersTable.update(user);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
```
