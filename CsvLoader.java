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