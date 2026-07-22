import java.util.*;

public class ComparisonEngine {


    public static List<ComparisonResult> compare(
            CsvFile dev,
            CsvFile prod) {


        List<ComparisonResult> results = new ArrayList<>();


        // Compare columns first
        compareColumns(dev, prod, results);


        // Compare records by ID
        compareRecords(dev, prod, results);


        // Check same person with different ID
        checkIdentityMismatch(dev, prod, results);


        return results;
    }



    private static void compareColumns(
            CsvFile dev,
            CsvFile prod,
            List<ComparisonResult> results) {


        Set<String> allColumns = new TreeSet<>();

        allColumns.addAll(dev.columns);
        allColumns.addAll(prod.columns);


        for(String column : allColumns) {


            boolean inDev =
                    dev.columns.contains(column);

            boolean inProd =
                    prod.columns.contains(column);


            if(!inDev || !inProd) {


                results.add(
                    new ComparisonResult(
                        "Missing Column",
                        "",
                        "",
                        "",
                        "",
                        column,
                        inDev ? "Exists" : "Missing",
                        inProd ? "Exists" : "Missing",
                        "Column exists in only one file"
                    )
                );
            }
        }
    }





    private static void compareRecords(
            CsvFile dev,
            CsvFile prod,
            List<ComparisonResult> results) {



        Set<String> allIds = new TreeSet<>();

        allIds.addAll(dev.rows.keySet());
        allIds.addAll(prod.rows.keySet());



        for(String id : allIds) {


            Map<String,String> devRow =
                    dev.rows.get(id);


            Map<String,String> prodRow =
                    prod.rows.get(id);



            // Record missing from DEV
            if(devRow == null) {


                results.add(
                    createResult(
                        "Missing DEV Record",
                        null,
                        prodRow,
                        id,
                        "Record exists only in PROD"
                    )
                );

                continue;
            }



            // Record missing from PROD
            if(prodRow == null) {


                results.add(
                    createResult(
                        "Missing PROD Record",
                        devRow,
                        null,
                        id,
                        "Record exists only in DEV"
                    )
                );

                continue;
            }



            compareFields(
                devRow,
                prodRow,
                results
            );
        }
    }





    private static void compareFields(
            Map<String,String> devRow,
            Map<String,String> prodRow,
            List<ComparisonResult> results) {



        String id =
                devRow.get("id");


        String first =
                devRow.getOrDefault(
                    "first_name",
                    ""
                );


        String last =
                devRow.getOrDefault(
                    "last_name",
                    ""
                );



        Set<String> columns =
                new TreeSet<>();


        columns.addAll(devRow.keySet());
        columns.addAll(prodRow.keySet());



        for(String column : columns) {


            String devValue =
                    devRow.getOrDefault(
                        column,
                        ""
                    );


            String prodValue =
                    prodRow.getOrDefault(
                        column,
                        ""
                    );



            if(!devValue.equals(prodValue)) {


                results.add(
                    new ComparisonResult(
                        "Data Difference",
                        id,
                        id,
                        last,
                        first,
                        column,
                        devValue,
                        prodValue,
                        "Value differs"
                    )
                );
            }
        }
    }







    private static void checkIdentityMismatch(
            CsvFile dev,
            CsvFile prod,
            List<ComparisonResult> results) {



        Map<String,Map<String,String>> prodNameLookup =
                new HashMap<>();



        for(Map<String,String> row :
                prod.rows.values()) {


            String key =
                    buildNameKey(row);


            prodNameLookup.put(
                    key,
                    row
            );
        }



        for(Map<String,String> devRow :
                dev.rows.values()) {



            String devId =
                    devRow.get("id");



            if(!prod.rows.containsKey(devId)) {


                String key =
                        buildNameKey(devRow);



                Map<String,String> prodRow =
                        prodNameLookup.get(key);



                if(prodRow != null) {


                    String prodId =
                            prodRow.get("id");



                    if(!devId.equals(prodId)) {



                        results.add(
                            new ComparisonResult(
                                "ID Mismatch",
                                devId,
                                prodId,
                                devRow.get("last_name"),
                                devRow.get("first_name"),
                                "id",
                                devId,
                                prodId,
                                "Same first and last name, different ID"
                            )
                        );
                    }
                }
            }
        }
    }







    private static ComparisonResult createResult(
            String type,
            Map<String,String> devRow,
            Map<String,String> prodRow,
            String id,
            String notes) {


        String first = "";
        String last = "";


        if(devRow != null) {

            first =
                devRow.getOrDefault(
                    "first_name",
                    ""
                );

            last =
                devRow.getOrDefault(
                    "last_name",
                    ""
                );
        }


        if(prodRow != null) {

            if(first.isEmpty())
                first =
                    prodRow.getOrDefault(
                        "first_name",
                        ""
                    );


            if(last.isEmpty())
                last =
                    prodRow.getOrDefault(
                        "last_name",
                        ""
                    );
        }



        return new ComparisonResult(
                type,
                devRow == null ? "" : id,
                prodRow == null ? "" : id,
                last,
                first,
                "",
                devRow == null ? "" : devRow.toString(),
                prodRow == null ? "" : prodRow.toString(),
                notes
        );
    }







    private static String buildNameKey(
            Map<String,String> row) {


        String first =
                row.getOrDefault(
                    "first_name",
                    ""
                )
                .trim()
                .toLowerCase();



        String last =
                row.getOrDefault(
                    "last_name",
                    ""
                )
                .trim()
                .toLowerCase();



        return last + "|" + first;
    }

}

---------------------------- file format misaligned
import java.util.*;

public class ComparisonEngine {


    public static List<ComparisonResult> compare(
            CsvFile dev,
            CsvFile prod) {


        List<ComparisonResult> results = new ArrayList<>();


        /*
         * Existing comparison logic stays here
         */



        /*
         * New Identity Correlation Check
         */
        results.addAll(
            checkIdentityMismatch(dev, prod)
        );


        return results;
    }



    private static List<ComparisonResult> checkIdentityMismatch(
            CsvFile dev,
            CsvFile prod) {


        List<ComparisonResult> results = new ArrayList<>();


        /*
         Create lookup:
         FirstName|LastName --> PROD record
        */

        Map<String, Map<String,String>> prodNameLookup =
                new HashMap<>();


        for(Map<String,String> row : prod.rows.values()) {

            String key =
                buildNameKey(row);


            prodNameLookup.put(key,row);
        }



        /*
          Check DEV users missing by ID
        */

        for(Map<String,String> devRow : dev.rows.values()) {


            String devId =
                devRow.get("id");


            if(!prod.rows.containsKey(devId)) {


                String key =
                    buildNameKey(devRow);


                Map<String,String> prodRow =
                    prodNameLookup.get(key);



                if(prodRow != null) {


                    String prodId =
                        prodRow.get("id");


                    if(!devId.equals(prodId)) {


                        results.add(
                            new ComparisonResult(
                                "ID Mismatch",
                                devId,
                                "id",
                                devId,
                                prodId,
                                "Same first and last name, different ID"
                            )
                        );
                    }
                }
            }
        }


        return results;
    }



    private static String buildNameKey(
            Map<String,String> row) {


        String first =
            row.getOrDefault(
                "first_name",
                ""
            ).trim().toLowerCase();


        String last =
            row.getOrDefault(
                "last_name",
                ""
            ).trim().toLowerCase();


        return last + "|" + first;
    }

}


-------------------------------------------- changed to include dups
import java.util.*;
public class ComparisonEngine{
 public static java.util.List<ComparisonResult> compare(CsvFile dev,CsvFile prod){
  List<ComparisonResult> out=new ArrayList<>();
  for(String h:dev.headers) if(!prod.headers.contains(h))
    out.add(new ComparisonResult("Missing Column","","","",h,"Exists","Missing"));
  for(String h:prod.headers) if(!dev.headers.contains(h))
    out.add(new ComparisonResult("Missing Column","","","",h,"Missing","Exists"));
  Set<String> ids=new TreeSet<>();
  ids.addAll(dev.rows.keySet()); ids.addAll(prod.rows.keySet());
  for(String id:ids){
    var d=dev.rows.get(id); var p=prod.rows.get(id);
    if(d==null){out.add(new ComparisonResult("Missing Record",id,p.getOrDefault("last_name",""),p.getOrDefault("first_name",""),"","Missing","Exists"));continue;}
    if(p==null){out.add(new ComparisonResult("Missing Record",id,d.getOrDefault("last_name",""),d.getOrDefault("first_name",""),"","Exists","Missing"));continue;}
    Set<String> cols=new TreeSet<>(); cols.addAll(dev.headers); cols.addAll(prod.headers);
    for(String c:cols){
      String dv=d.getOrDefault(c,"").trim();
      String pv=p.getOrDefault(c,"").trim();
      if(!dv.equalsIgnoreCase(pv))
        out.add(new ComparisonResult("Difference",id,d.getOrDefault("last_name",""),d.getOrDefault("first_name",""),c,dv,pv));
    }
  }
  return out;
 }
}
