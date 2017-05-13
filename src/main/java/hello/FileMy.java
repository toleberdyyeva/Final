package hello;

/**
 * Created by meraryslan on 29.04.17.
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity // This tells Hibernate to make a table out of this class
public class FileMy {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private  String userid;
    private ArrayList<String> filepaths = new ArrayList<String>();

    public void setId(String userid){
        this.userid = userid;
    }
    public void setAddPathFile(String path){
        filepaths.add(path);
    }

    public Integer getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public ArrayList<String> getFilepaths() {
        return filepaths;
    }

}
