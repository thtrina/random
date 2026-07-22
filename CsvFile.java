import java.util.*;

public class CsvFile {


    // Column names from CSV header row
    public Set<String> headers =
            new LinkedHashSet<>();


    // Data stored by ID
    // Key = id column
    // Value = complete row
    public Map<String, Map<String,String>> rows =
            new LinkedHashMap<>();


    // Duplicate IDs found while loading
    public Set<String> duplicateIds =
            new LinkedHashSet<>();


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
