package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "User")
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receipt> receipts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptType> receiptTypes;

    @Column(unique = true)
    private String username;

    private String password;

    private String token;


    public User() {
        this.receipts = new ArrayList<Receipt>();
        this.receiptTypes = new ArrayList<ReceiptType>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.receipts = new ArrayList<Receipt>();
        this.receiptTypes = new ArrayList<ReceiptType>();
    }

    /**
     * Getters and setters
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Receipt> getReceipts() {return receipts;}

    public void setReceipts(List<Receipt> receipts) {this.receipts = receipts;}

    public List<ReceiptType> getReceiptTypes() {return receiptTypes;}

    public void setReceiptTypes(List<ReceiptType> receiptTypes) {this.receiptTypes = receiptTypes;}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Receipt getReceipt(Long id) {
        for (Receipt r: this.receipts) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
        receipt.setUser(this);
    }

    public void deleteReceipt(Receipt receipt) {
        receipts.remove(receipt);
        receipt.setUser(null);
    }

    public void deleteReceiptsOfType(Long type) {
        ArrayList<Receipt> receiptsToDelete = new ArrayList<Receipt>();
        for (Receipt r: this.receipts) {
            if (Long.compare(r.getType(), type) == 0) {
                receiptsToDelete.add(r);
            }
        }
        for (Receipt r : receiptsToDelete) {
            deleteReceipt(r);
        }
    }

    public ReceiptType getReceiptType(Long id) {
        for (ReceiptType rt: this.receiptTypes) {
            if (rt.getId().equals(id)) {
                return rt;
            }
        }
        return null;
    }

    public void addReceiptType(ReceiptType receiptType) {
        receiptTypes.add(receiptType);
        receiptType.setUser(this);
    }

    public void deleteReceiptType(ReceiptType receiptType) {
        receiptTypes.remove(receiptType);
        receiptType.setUser(null);
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tusername='"+username+"\'\n"+
                "\tpassword='"+password+"\'\n"+
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                //receipts.equals(user.receipts) &&
                username.equals(user.username) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}