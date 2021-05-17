package pl.pjatk.squashme.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(indices = {
        @Index(value = {"name"}, unique = true)
})
public class Player implements Serializable {

    private static final long serialVersionUID = 3139539820614182032L;

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
