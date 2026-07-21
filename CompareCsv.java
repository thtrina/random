import java.util.*;
public class CompareCsv{
 public static void main(String[] args)throws Exception{
  CsvFile dev=CsvLoader.load("C:/Users/58785/IIQ/InProgress/dev.csv");
  CsvFile prod=CsvLoader.load("C:/Users/58785/IIQ/InProgress/prod.csv");
  List<ComparisonResult> results=ComparisonEngine.compare(dev,prod);
  ReportWriter.write("C:/Users/58785/IIQ/InProgress/ComparisonReport.csv",results);
  long diff=results.stream().filter(r->r.type.equals("Difference")).count();
  long missRec=results.stream().filter(r->r.type.equals("Missing Record")).count();
  long missCol=results.stream().filter(r->r.type.equals("Missing Column")).count();
  System.out.println("Comparison complete");
  System.out.println("Missing Columns: "+missCol);
  System.out.println("Missing Records: "+missRec);
  System.out.println("Differences: "+diff);
 }
}