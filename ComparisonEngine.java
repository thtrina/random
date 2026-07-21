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