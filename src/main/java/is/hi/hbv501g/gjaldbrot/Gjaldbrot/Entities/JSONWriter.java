package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
        // TODO líklega þarf að sækja líka receiptTypes úr repository til að fá það sem notandi býr til sjálfur
        List<ReceiptType> receiptTypes = user.getReceiptTypes();
        int[] amounts = new int[receiptTypes.size()];
        System.out.println(user.getReceipts().size());
        for(Receipt r : receipts) {
            int typeIndex = getIndexOfType(receiptTypes, r.getType());
            if (typeIndex >= 0) {
                amounts[typeIndex] += r.getAmount();
            }
        }

        String json = "{\"group\":[";

        for(int i = 0; i < receiptTypes.size(); i++) {
            String receiptTypeName = receiptTypes.get(i).getName();
            int amount = amounts[i];
            json += "{\"name\":\"" + receiptTypeName + "\",\"amount\":" + amount+"}";
            if (i < receiptTypes.size() - 1) {
                json += ",";
            }
        }
        return json + "]}";
    }

    public static String writeComparisonData(User user, List<Receipt> r) {
        Collections.sort(r);

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

        int monthIndex = 0;

        List<ReceiptType> receiptTypes = user.getReceiptTypes();

        int[][] comp = new int[receiptTypes.size()][diff];


        for (Receipt _r : r) {
            temp.setTime(_r.getDate()); // get the date of the receipt

            while(temp.get(Calendar.MONTH) > cur.get(Calendar.MONTH)) {
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
            /*if (_r.getType() == Type.MATARINNKAUP) matur[monthIndex] += _r.getAmount();
            else if (_r.getType() == Type.FATNADUR) fatnadur[monthIndex] += _r.getAmount();
            else if (_r.getType() == Type.AFENGI) afengi[monthIndex] += _r.getAmount();
            else if (_r.getType() == Type.TOBAK) tobak[monthIndex] += _r.getAmount();
            else if (_r.getType() == Type.SKEMMTUN_OG_AFTREYING) skemmtun[monthIndex] += _r.getAmount();
            else if (_r.getType() == Type.VEITINGASTADUR) veitingar[monthIndex] += _r.getAmount();*/
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM");


        String json = "{\"start\":\""+df.format(start.getTime())+"\",";
        json += "\"end\":\""+df.format(end.getTime())+"\",";

        for(int i = 0; i < receiptTypes.size(); i++) {
            String typeName = receiptTypes.get(i).getName();
            json += '\"'+typeName+"\":[";
            for(int month = 0; month < diff - 1; month++) {
                json += comp[i][month]+",";
            }
            json += comp[i][diff-1] +"],";
        }
        json += '}';
        System.out.println(json);
        return json;
    }

    public String writeAllReceiptsOfUser(User user) {
        return "";
    }
}
