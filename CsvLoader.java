import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.commons.csv.*;

public class CsvLoader {

    static String clean(String s) {
        return s == null ? "" : s.trim();
    }

    public static CsvFile load(String file) throws Exception {

        CsvFile csv = new CsvFile();

        try (
            Reader reader = new InputStreamReader(
                new FileInputStream(file),
                StandardCharsets.UTF_8
            );

            CSVParser parser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreSurroundingSpaces(true)
                    .build()
                    .parse(reader)
        ) {

            csv.headers.addAll(parser.getHeaderNames());

            int idIndex = csv.headers.indexOf("id");

            if (idIndex < 0) {
                throw new Exception("id column missing");
            }

            for (CSVRecord record : parser) {

                Map<String,String> row = new HashMap<>();

                for (String header : csv.headers) {
                    row.put(
                        header,
                        clean(record.get(header))
                    );
                }

                csv.rows.put(row.get("id"), row);
            }
        }

        return csv;
    }
}

-------------------------------------another change for commas and "" ------------------------------------------
import java.io.*;
import java.util.*;

public class CsvLoader {

    static String clean(String s) {
        return s == null ? "" : s.trim();
    }

    public static CsvFile load(String file) throws Exception {

        CsvFile csv = new CsvFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String h = br.readLine();

            if (h == null) {
                throw new Exception("Empty file");
            }

            String[] heads = h.split(",", -1);

            for (String x : heads) {
                csv.headers.add(clean(x));
            }

            int idIndex = csv.headers.indexOf("id");

            if (idIndex < 0) {
                throw new Exception("id column missing");
            }

            String line;

            while ((line = br.readLine()) != null) {

                String[] cols = line.split(",", -1);

                Map<String, String> row = new HashMap<>();

                for (int i = 0; i < csv.headers.size(); i++) {
                    row.put(
                        csv.headers.get(i),
                        i < cols.length ? clean(cols[i]) : ""
                    );
                }

                csv.rows.put(row.get("id"), row);
            }
        }

        return csv;
    }
}






======================================================old below -------------
import java.io.*;
public class CsvLoader {
    static String clean(String s){ return s==null?"":s.trim(); }
    public static CsvFile load(String file) throws Exception{
        CsvFile csv=new CsvFile();
        BufferedReader br=new BufferedReader(new FileReader(file));
        String h=br.readLine();
        if(h==null) throw new Exception("Empty file");
        String[] heads=h.split(",",-1);
        for(String x:heads) csv.headers.add(clean(x));
        int idIndex=csv.headers.indexOf("id");
        if(idIndex<0) throw new Exception("id column missing");
        String line;
        while((line=br.readLine())!=null){
            String[] cols=line.split(",",-1);
            java.util.Map<String,String> row=new java.util.HashMap<>();
            for(int i=0;i<csv.headers.size();i++){
                row.put(csv.headers.get(i), i<cols.length?clean(cols[i]):"");
            }
            csv.rows.put(row.get("id"),row);
        }
        br.close();
        return csv;
    }
}
