import java.io.*;import java.util.*;
public class ReportWriter{
 public static void write(String file,List<ComparisonResult> r)throws Exception{
  BufferedWriter w=new BufferedWriter(new FileWriter(file));
  w.write("Type,ID,LastName,FirstName,Column,DEV Value,PROD Value\n");
  for(ComparisonResult x:r)
   w.write(String.join(",",x.type,x.id,x.last,x.first,x.column,"\""+x.dev+"\"","\"" + x.prod + "\"")+"\n");
  w.close();
 }
}