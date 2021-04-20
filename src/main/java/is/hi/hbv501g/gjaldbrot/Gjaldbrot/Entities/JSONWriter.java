package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSONWriter {
    private static int getIndexOfType(List<ReceiptType> lr, int type) {
        int index = 0;
        for(ReceiptType rt : lr) {
            if(rt.getId() == (long) type) {
                return index;
            }
            index += 1;
        }
        return -1;
    }

    public static String writeReceipts(User user, List<Receipt> receipts) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        Calendar receiptDate = Calendar.getInstance();

        List<ReceiptType> receiptTypes = user.getReceiptTypes();
        int[] amounts = new int[receiptTypes.size()];
        System.out.println(user.getReceipts().size());
        for(Receipt r : receipts) {
            receiptDate.setTime(r.getDate());
            if (receiptDate.get(Calendar.YEAR) != currentDate.get(Calendar.YEAR)
            || receiptDate.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH)) {
                continue;
            }
            int typeIndex = getIndexOfType(receiptTypes, r.getType());
            if (typeIndex >= 0) {
                amounts[typeIndex] += r.getAmount();
            }
        }

        String json = "{\"group\":[";

        for(int i = 0; i < receiptTypes.size(); i++) {
            String receiptTypeName = receiptTypes.get(i).getName();
            int receiptColor = receiptTypes.get(i).getColor();
            int amount = amounts[i];
            json += "{\"name\":\"" + receiptTypeName + "\",\"amount\":\"" + amount+"\", \"color\": \"" + receiptColor +"\"}";
            if (i < receiptTypes.size() - 1) {
                json += ",";
            }
        }
        return json + "]}";
    }

    public static String writeComparisonData(User user, List<Receipt> r) {
        Collections.sort(r);

        if (r.isEmpty()) {
            return "";
        }
        Calendar start = Calendar.getInstance();
        start.setTime(r.get(0).getDate());
        Calendar end = Calendar.getInstance();
        end.setTime(r.get(r.size()-1).getDate());

        int diff = 12*(end.get(Calendar.YEAR)-start.get(Calendar.YEAR));
        diff += (end.get(Calendar.MONTH) - start.get(Calendar.MONTH));
        diff += 1;
        //búa til fylki með jafn mörgum og diff

        Calendar temp = Calendar.getInstance();
        Calendar cur = (Calendar) start.clone();

        List<ReceiptType> receiptTypes = user.getReceiptTypes();

        int[][] comp = new int[receiptTypes.size()][diff];
        String[] months = new String[diff];

        int monthIndex = 0;
        for (Receipt _r : r) {
            temp.setTime(_r.getDate()); // get the date of the receipt

            while(temp.get(Calendar.MONTH) > cur.get(Calendar.MONTH) || temp.get(Calendar.YEAR) > cur.get(Calendar.YEAR)) {
                months[monthIndex] = cur.get(Calendar.YEAR)+"-"+cur.get(Calendar.MONTH);
                cur.add(Calendar.MONTH, 1);
                monthIndex += 1;
            }
            if (monthIndex == diff){
                System.out.println("something went wrong");
                break;
            }

            int typeIndex = getIndexOfType(receiptTypes, _r.getType());

            if (typeIndex < 0) { continue; }

            comp[typeIndex][monthIndex] += _r.getAmount();
        }
        months[diff-1] = end.get(Calendar.YEAR)+"-"+end.get(Calendar.MONTH);

        DateFormat df = new SimpleDateFormat("yyyy-MM");


        String json = "{\"startDate\":\""+df.format(start.getTime())+"\",";
        json += "\"endDate\":\""+df.format(end.getTime())+"\",";
        json += "\"groups\":[";

        for(int i = 0; i < receiptTypes.size(); i++) {
            String typeName = receiptTypes.get(i).getName();
            int typeColor = receiptTypes.get(i).getColor();
            json += "{\"name\":\""+typeName+"\",\"color\":"+typeColor+",\"amounts\":[";
            for(int month = 0; month < diff - 1; month++) {
                json += "{\"date\":\""+months[month]+"\",\"amount\":"+comp[i][month]+"},";
            }
            json += "{\"date\":\""+months[diff-1]+"\",\"amount\":"+comp[i][diff-1] +"}]}";
            if (i < receiptTypes.size() - 1) json += ",";
        }
        json += "]}";
        return json;
    }

    public String writeAllReceiptsOfUser(User user) {
        return "";
    }
}
