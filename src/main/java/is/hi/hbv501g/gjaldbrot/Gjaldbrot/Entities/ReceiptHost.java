package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Klasi notaður til þess að geyma gögn úr ThymeLeaf á strengjaformi,
 * Svo gögn geti verið rétt parsed þegar þau eru sett í gagnagrunn
 */

public class ReceiptHost {

    public static List<ReceiptHost> receiptListToHostList(List<Receipt> rl, List<ReceiptType> tl) {
        System.out.println("Transforming receipt list");
        List<ReceiptHost> hostList = new ArrayList<>();
        for (Receipt r : rl) {
            System.out.println("Transforming receipt");
            ReceiptHost receiptHost = new ReceiptHost();
            receiptHost.setId(r.getId());
            receiptHost.setAmount(r.getAmount());
            receiptHost.setDate(r.getDate().toString());
            receiptHost.setTime(r.getTime().toString());
            receiptHost.setTypeId(r.getType()+"");
            for (ReceiptType rt : tl) {
                if (rt.getId().intValue() == r.getType()) {
                    receiptHost.setType(rt.getName());
                    hostList.add(receiptHost);
                }
            }
        }
        return hostList;
    }

    private long id;
    private String date;
    private String time;
    private String type;
    private String type_id;
    private int amount;

    public ReceiptHost(){}

    public ReceiptHost(int amount) {
        this.amount = amount;
    }

    public void setId(long id) {this.id = id;}
    public long getId() { return id; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeId(String type_id) { this.type_id = type_id; }

    public String getType_id() { return this.type_id; }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public Date getParsedDate() throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(this.date);
    }

    public String getTime() {
        return time;
    }

    public LocalTime getParsedTime() {
        return LocalTime.parse(this.time);
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }



    @Override
    public String toString(){
        return "" + "\n" + date + "\n" + time + "\n" + type + "\n" + amount;
    }

    public ReceiptHost(String date, String time, String type, int amount) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.amount = amount;
    }

    public Receipt createReceipt() throws Exception{
        Receipt r = new Receipt();
        r.setAmount(this.amount);
        System.out.println(this.date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = df.parse(this.date);
        System.out.println(d);
        r.setDate(d);
        LocalTime t = LocalTime.parse(this.time);
        r.setTime(t);
        r.setAmount(this.amount);
        r.setType(Integer.parseInt(type_id));
        return r;
    }
}
