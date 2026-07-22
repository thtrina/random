import java.util.*;

public class CsvFile {

    public Map<String, Map<String,String>> rows;

    public Set<String> columns;


    public CsvFile() {

        rows = new LinkedHashMap<>();

        columns = new TreeSet<>();
    }
}

-------------------------------------------------------try this instead
import java.util.*;

public class CsvFile {

    // Column headers
    public List<String> headers = new ArrayList<>();

    // Records keyed by ID
    public Map<String, Map<String, String>> rows = new LinkedHashMap<>();

    // Duplicate IDs found while loading
    public List<String> duplicateIds = new ArrayList<>();

}


---------------  new file format misaligned
import java.util.*;
public class CsvFile {
    public List<String> headers=new ArrayList<>();
    public Map<String,Map<String,String>> rows=new HashMap<>();
}
