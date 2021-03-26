package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Reference;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity(name = "Receipt")
@Table(name = "receipt")
public class Receipt implements Comparable<Receipt>{
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private Date date;
    private LocalTime time;
    private int type;
    private int amount;

    public Receipt() {

    }

    /**
     * Receipt(Date date, LocalTime time, Type type, int amount)
     * @param date date of the receipt
     * @param time time of the receipt
     * @param type type of the receipt
     * @param amount amount of the receipt
     */
    public Receipt(Date date, LocalTime time, int type, int amount) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.amount = amount;
    }

    public Receipt(int amount) {
        this.amount = amount;
    }

    /**
     * getters and setters
     */
    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void modify(ReceiptHost rh) {
        if (rh.getAmount() > 0) {
            this.amount = rh.getAmount();
        }
        if (rh.getDate() != null) {
            try {
                this.date = rh.getParsedDate();
            }
            catch (Exception e) {
            }
        }
        if (rh.getType() != null) {
            try {
                this.type = Integer.parseInt(rh.getType());
            }
            catch (Exception e) {
            }
        }
        if (rh.getTime() != null) {
            this.time = rh.getParsedTime();
        }
    }

    @Override
    public int compareTo(Receipt r) {
        return this.date.compareTo(r.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((Receipt) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString(){
        return "" + id + "\n" + date + "\n" + time + "\n" + type + "\n" + amount;
    }
}
