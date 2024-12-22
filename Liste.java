import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Liste  implements Serializable {
    List<String> allfiles = new ArrayList<>();

     public  Liste   (List<String> allfiles ){
        setAllfiles(allfiles);
     }  
    public void setAllfiles(List<String> allfiles) {
        this.allfiles = allfiles;
    }
    public List<String> getAllfiles() {
        return allfiles;
    }

    
 }
