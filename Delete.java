import java.io.Serializable;

public class Delete implements Serializable {
    Object file;
    String filename;
    public Delete (String filename , Object file )
    {
        setFile(file);
        setFilename(filename);
    }
    public void setFile(Object file) {
        this.file = file;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public Object getFile() {
        return file;
    }
    public String getFilename() {
        return filename;
    }
}
