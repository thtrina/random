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


    /*
     * Compare the column headers in DEV and PROD.
     */
    private static void compareColumns(
            CsvFile dev,
            CsvFile prod,
            List<ComparisonResult> results) {

        Set<String> allColumns = new TreeSet<>();

        allColumns.addAll(dev.headers);
        allColumns.addAll(prod.headers);


        for (String column : allColumns) {

            // Completely ignore these columns
            if (isIgnoredColumn(column)) {
                continue;
            }


            boolean inDev =
                    dev.headers.contains(column);

            boolean inProd =
                    prod.headers.contains(column);


            if (!inDev || !inProd) {

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
                        "",
                        "",
                        "Column exists in only one file"
                    )
                );
            }
        }
    }


    /*
     * Compare records by ID.
     */
    private static void compareRecords(
            CsvFile dev,
            CsvFile prod,
            List<ComparisonResult> results) {


        Set<String> allIds = new TreeSet<>();

        allIds.addAll(dev.rows.keySet());
        allIds.addAll(prod.rows.keySet());


        for (String id : allIds) {

            Map<String, String> devRow =
                    dev.rows.get(id);

            Map<String, String> prodRow =
                    prod.rows.get(id);


            // Record exists only in PROD
            if (devRow == null) {

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


            // Record exists only in DEV
            if (prodRow == null) {

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


            // Record exists in both
            compareFields(
                devRow,
                prodRow,
                results
            );
        }
    }


    /*
     * Compare all fields for records that have
     * the same ID.
     */
    private static void compareFields(
            Map<String, String> devRow,
            Map<String, String> prodRow,
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


        String devStatus =
                devRow.getOrDefault(
                    "status",
                    ""
                );


        String prodStatus =
                prodRow.getOrDefault(
                    "status",
                    ""
                );


        Set<String> columns =
                new TreeSet<>();


        columns.addAll(devRow.keySet());
        columns.addAll(prodRow.keySet());


        for (String column : columns) {


            // Ignore load_ts and file_name
            if (isIgnoredColumn(column)) {
                continue;
            }


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


            /*
             * Ignore case and leading/trailing spaces
             */
            if (!valuesEqual(devValue, prodValue)) {

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
                        devStatus,
                        prodStatus,
                        "Value differs"
                    )
                );
            }
        }
    }


    /*
     * Check for people with the same first/last name
     * but different IDs.
     */
    private static void checkIdentityMismatch(
            CsvFile dev,
            CsvFile prod,
            List<ComparisonResult> results) {


        Map<String, Map<String, String>> prodNameLookup =
                new HashMap<>();


        for (Map<String, String> row :
                prod.rows.values()) {

            String key =
                    buildNameKey(row);


            /*
             * Do not create a lookup entry for a blank
             * first and last name.
             */
            if (!key.equals("|")) {

                prodNameLookup.put(
                    key,
                    row
                );
            }
        }


        for (Map<String, String> devRow :
                dev.rows.values()) {


            String devId =
                    devRow.get("id");


            if (!prod.rows.containsKey(devId)) {


                String key =
                        buildNameKey(devRow);


                Map<String, String> prodRow =
                        prodNameLookup.get(key);


                if (prodRow != null) {


                    String prodId =
                            prodRow.get("id");


                    if (!valuesEqual(devId, prodId)) {


                        String devStatus =
                                devRow.getOrDefault(
                                    "status",
                                    ""
                                );


                        String prodStatus =
                                prodRow.getOrDefault(
                                    "status",
                                    ""
                                );


                        results.add(
                            new ComparisonResult(
                                "ID Mismatch",
                                devId,
                                prodId,
                                devRow.getOrDefault(
                                    "last_name",
                                    ""
                                ),
                                devRow.getOrDefault(
                                    "first_name",
                                    ""
                                ),
                                "id",
                                devId,
                                prodId,
                                devStatus,
                                prodStatus,
                                "Same first and last name, different ID"
                            )
                        );
                    }
                }
            }
        }
    }


    /*
     * Create results for records that exist
     * in only one file.
     */
    private static ComparisonResult createResult(
            String type,
            Map<String, String> devRow,
            Map<String, String> prodRow,
            String id,
            String notes) {


        String first = "";
        String last = "";

        String devStatus = "";
        String prodStatus = "";


        if (devRow != null) {

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

            devStatus =
                devRow.getOrDefault(
                    "status",
                    ""
                );
        }


        if (prodRow != null) {

            if (first.isEmpty()) {

                first =
                    prodRow.getOrDefault(
                        "first_name",
                        ""
                    );
            }


            if (last.isEmpty()) {

                last =
                    prodRow.getOrDefault(
                        "last_name",
                        ""
                    );
            }


            prodStatus =
                prodRow.getOrDefault(
                    "status",
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

            devRow == null
                ? ""
                : devRow.toString(),

            prodRow == null
                ? ""
                : prodRow.toString(),

            devStatus,

            prodStatus,

            notes
        );
    }


    /*
     * Build lookup key using last name + first name.
     */
    private static String buildNameKey(
            Map<String, String> row) {


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


    /*
     * Ignore these columns completely.
     */
    private static boolean isIgnoredColumn(
            String column) {

        if (column == null) {
            return false;
        }


        return column.equalsIgnoreCase("load_ts")
            || column.equalsIgnoreCase("file_name");
    }


    /*
     * Compare values without considering
     * capitalization or leading/trailing spaces.
     */
    private static boolean valuesEqual(
            String a,
            String b) {

        if (a == null) {
            a = "";
        }

        if (b == null) {
            b = "";
        }


        return a.trim().equalsIgnoreCase(
            b.trim()
        );
    }

}






---------------------------------------Ignore Terms
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
