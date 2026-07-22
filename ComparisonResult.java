public class ComparisonResult {

    public String type;

    public String devId;
    public String prodId;

    public String firstName;
    public String lastName;

    public String column;

    public String devValue;
    public String prodValue;

    public String notes;

    public ComparisonResult(
            String type,
            String devId,
            String prodId,
            String lastName,
            String firstName,
            String column,
            String devValue,
            String prodValue,
            String notes) {

        this.type = type;

        this.devId = devId;
        this.prodId = prodId;

        this.lastName = lastName;
        this.firstName = firstName;

        this.column = column;

        this.devValue = devValue;
        this.prodValue = prodValue;

        this.notes = notes;
    }
}
-------------------------------------New file format is misaligned
public class ComparisonResult {

    String type;
    String id;
    String column;
    String oldValue;
    String newValue;
    String message;

    public ComparisonResult(
            String type,
            String id,
            String column,
            String oldValue,
            String newValue,
            String message) {

        this.type = type;
        this.id = id;
        this.column = column;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.message = message;
    }
}
-----------------------------new to look for duplicate rows
public class ComparisonResult{
 public String type,id,last,first,column,dev,prod;
 public ComparisonResult(String t,String i,String l,String f,String c,String d,String p){
 type=t;id=i;last=l;first=f;column=c;dev=d;prod=p;}
}
