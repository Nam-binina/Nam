import java.io.Serializable;

public class Fichier implements Serializable {
    
    String parentname;
    String nom;
    byte[] data  = new byte[0];

    public Fichier(String nom,   String parentname   ,  byte[] data) {
        setData(data);
        setNom(nom);
        setParentname(parentname);
    }
    
    
    public void setParentname(String parentname) {
        this.parentname = parentname;
    }
    public String getParentname() {
        return parentname;
    }
    

    public byte[] getData() {
        return data;
    }

    public String getNom() {
        return nom;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}
