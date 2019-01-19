# ByteDataSteam
An libs that helps you serialize and deserialize your data class

## Usage

##### Serialize/Deserialize simple data

```java
byte[] data = ByteSerialization.serialize("Hello ByteDataStream", 1, true);

Object[] objs = ByteSerialization.deserialize(data, String.class, int.class, boolean.class);
// objs[0] => "Hello ByteDataStream"
// objs[1] => 1
// objs[2] => true
```


##### Serialize/Deserialize data class

You need to implement interface `ByteSteamDeserializable` and `ByteSteamSerializable` first.

```java
public class SimpleData implements ByteSteamDeserializable, ByteSteamDeserializable {
    public final String message;
    public final int id;
    public final Date date;
    
    public SimpleData(String message, int id, Date date) {
        this.message = message;
        this.id = id;
        this.date = date;
    }
    
   
    @Override
    public void serialize(DataOutput out) throws IOException {
        out.write(message, id, date);
    }

    /**
    * The deserialize method must be "public static".
    */
    public static SimpleData deserialize(DataInput in) throws IOException {
        return new SimpleData(in.readUTF(), in.readInt(), in.readDate());
    }
}
```

Usage:

```java
byte[] data = ByteSerialization.serialize(new SimpleData("Hello ByteDataStream", 1, new Date()));
SimpleData data = ByteSerialization.deserialize(data, SimpleData.class);
```

##### Custom Serializer/Deserializer

Your can use the following method to register your serializer/deserializer to some specific type.
```java
ByteSerialization.register(java.lang.Class<T>, ByteSteamSerializer<T>, ByteSteamDeserializer<T>);
```
Usage:
```java
ByteSerialization.register(Date.class, (out, o) -> out.writeDate(o), in -> in.readDate());
```

##### Supported types

* int
* long
* float
* double
* boolean
* short
* byte
* byte[]
* String
* Date
* Enum
* UUID
* Locale
* LocalTime