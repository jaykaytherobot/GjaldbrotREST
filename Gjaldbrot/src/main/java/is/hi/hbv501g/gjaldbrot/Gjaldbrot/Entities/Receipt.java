package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;


@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userId;
    private Date timestamp;
    //String núna en seinna af type Type?
    private String type;
    private int amount;

    //Generic smiður
    public Receipt(){}

    public Receipt(Date timestamp, String type, int amount){
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
    }

    //vvv Getters og setters vvv
    public long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
