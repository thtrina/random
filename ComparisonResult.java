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
