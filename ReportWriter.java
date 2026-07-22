import java.io.*;
import java.util.*;

public class ReportWriter {


    public static void write(
            String filename,
            List<ComparisonResult> results)
            throws IOException {


        try(FileWriter writer =
                new FileWriter(filename)) {


            writer.write(
                "Type,DEV ID,PROD ID,Last Name,First Name,Column,DEV Value,PROD Value,Notes\n"
            );



            for(ComparisonResult r : results) {


                writer.write(
                    clean(r.type) + "," +
                    clean(r.devId) + "," +
                    clean(r.prodId) + "," +
                    clean(r.lastName) + "," +
                    clean(r.firstName) + "," +
                    clean(r.column) + "," +
                    clean(r.devValue) + "," +
                    clean(r.prodValue) + "," +
                    clean(r.notes) +
                    "\n"
                );
            }
        }
    }




    private static String clean(String value) {


        if(value == null)
            return "";


        /*
         * CSV does not contain quotes or commas
         * so replace commas/new lines
         * to keep alignment correct
         */
        return value
                .replace(",", " ")
                .replace("\r", " ")
                .replace("\n", " ");
    }

}

------------------------- file format misaligned
import java.io.*;
import java.util.*;

public class ReportWriter {


public static void write(
        String filename,
        List<ComparisonResult> results)
        throws IOException {


    try(FileWriter writer =
            new FileWriter(filename)) {


        writer.write(
            "Type,ID,Column,Old Value,New Value,Message\n"
        );


        for(ComparisonResult r : results) {


            writer.write(
                clean(r.type)+","+
                clean(r.id)+","+
                clean(r.column)+","+
                clean(r.oldValue)+","+
                clean(r.newValue)+","+
                clean(r.message)+
                "\n"
            );
        }

    }
}



private static String clean(String value){

    if(value == null)
        return "";

    return value.replace(","," ");
}


}

---------------------------------- updated to include dups
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
