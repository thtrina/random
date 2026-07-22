import java.util.*;

public class CompareCsv {


    public static void main(String[] args) throws Exception {


        String devFile =
            "C:/Users/58785/IIQ/InProgress/dev.csv";


        String prodFile =
            "C:/Users/58785/IIQ/InProgress/prod.csv";


        String reportFile =
            "C:/Users/58785/IIQ/InProgress/ComparisonReport.csv";



        System.out.println("Loading DEV file...");
        CsvFile dev =
            CsvLoader.load(devFile);



        System.out.println("Loading PROD file...");
        CsvFile prod =
            CsvLoader.load(prodFile);



        System.out.println("Comparing files...");

        List<ComparisonResult> results =
            ComparisonEngine.compare(
                dev,
                prod
            );



        System.out.println("Writing report...");

        ReportWriter.write(
            reportFile,
            results
        );



        long differences =
            results.stream()
                .filter(r ->
                    r.type.equals("Data Difference"))
                .count();



        long missingDev =
            results.stream()
                .filter(r ->
                    r.type.equals("Missing DEV Record"))
                .count();



        long missingProd =
            results.stream()
                .filter(r ->
                    r.type.equals("Missing PROD Record"))
                .count();



        long missingColumns =
            results.stream()
                .filter(r ->
                    r.type.equals("Missing Column"))
                .count();



        long idMismatch =
            results.stream()
                .filter(r ->
                    r.type.equals("ID Mismatch"))
                .count();



        System.out.println();
        System.out.println("==============================");
        System.out.println("Comparison Complete");
        System.out.println("==============================");

        System.out.println(
            "Data Differences : " + differences
        );

        System.out.println(
            "Missing DEV Records : " + missingDev
        );

        System.out.println(
            "Missing PROD Records : " + missingProd
        );

        System.out.println(
            "Missing Columns : " + missingColumns
        );

        System.out.println(
            "ID Mismatches : " + idMismatch
        );


        System.out.println();
        System.out.println(
            "Report created: " + reportFile
        );
    }
}



-----------------------file format misaligned
    results.stream()
    .filter(r -> r.type.equals("ID Mismatch"))
    .count();


System.out.println("ID Mismatches: " + idMismatch);
-------------------------------- updates to include dups
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
  long idMismatch=results.stream().filter(r -> r.type.equals("ID Mismatch")).count();
  long idMismatch = results.stream().filter(r -> r.type.equals("ID Mismatch")).count();  
System.out.println("ID Mismatches: " + idMismatch);
  System.out.println("Comparison complete");
  System.out.println("Missing Columns: "+missCol);
  System.out.println("Missing Records: "+missRec);
  System.out.println("Differences: "+diff);
 }
}

