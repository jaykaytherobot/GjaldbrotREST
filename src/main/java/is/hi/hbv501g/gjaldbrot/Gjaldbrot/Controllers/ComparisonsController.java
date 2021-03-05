package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Controllers;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.JSONWriter;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.Receipt;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.ReceiptService;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpSession;

@Controller
public class ComparisonsController {
    private ReceiptService receiptService;
    private UserService userService;

    @Autowired
    public ComparisonsController(ReceiptService receiptService, UserService userService){
        this.receiptService = receiptService;
        this.userService = userService;
    }

    /*
    * safnar saman upphæðum í útgjaldaflokka og skilar JSON streng
    * sem er á forminu: "útgjaldaflokkur: upphæð"
    * */
    private String writeReceipts(List<Receipt> receipts){
        int matur = 0, fot=0, afengi=0, tobak=0, skemmtun=0, veitingar=0;
        /*for (Receipt r : receipts) {
            if (r.getType() == Type.MATARINNKAUP) matur += r.getAmount();
            else if (r.getType() == Type.FATNADUR) fot += r.getAmount();
            else if (r.getType() == Type.AFENGI) afengi += r.getAmount();
            else if (r.getType() == Type.TOBAK) tobak += r.getAmount();
            else if (r.getType() == Type.SKEMMTUN_OG_AFTREYING) skemmtun += r.getAmount();
            else if (r.getType() == Type.VEITINGASTADUR) veitingar += r.getAmount();
        }*/
        String JSON = String.format("{\"matur\":%d,\"fatnadur\":%d,\"afengi\":%d,\"tobak\":%d,\"skemmtun\":%d,\"veitingar\":%d}",
                matur,fot,afengi,tobak,skemmtun,veitingar);
        System.out.println(JSON);
        return JSON;
    }

    @RequestMapping(value = "/overView", method = RequestMethod.GET)
    public String overview(Model model, HttpSession session){
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date dateobj = new Date();

        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            User user = userService.findByUsername(sessionUser.getUsername());
            List<Receipt> receipts = receiptService.getReceiptsByMonth(user, df.format(dateobj));
            System.out.println(JSONWriter.writeReceipts(user, receipts));

            model.addAttribute("receipts", writeReceipts(receipts));
            return "overView";
        }
        return "redirect:/";
    }

    /*
     * Safnar saman upplýsingum fyrir samanburð milli mánuða í JSON streng
     */
    private String writeComparisonData(List<Receipt> r) {
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

        int[] matur = new int[diff];
        int[] fatnadur = new int[diff];
        int[] afengi = new int[diff];
        int[] tobak = new int[diff];
        int[] skemmtun = new int[diff];
        int[] veitingar = new int[diff];

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
        json += "\"matur\":[";
        for(int i = 0; i < diff - 1; i++) {
            json += matur[i]+",";
        }
        json+=matur[diff-1] + "],";

        json += "\"fatnadur\":[";
        for(int i = 0; i < diff - 1; i++) {
            json += fatnadur[i]+",";
        }
        json+=fatnadur[diff-1] + "],";

        json += "\"afengi\":[";
        for(int i = 0; i < diff - 1; i++) {
            json += afengi[i]+",";
        }
        json+=afengi[diff-1] + "],";

        json += "\"tobak\":[";
        for(int i = 0; i < diff - 1; i++) {
            json += tobak[i]+",";
        }
        json+=tobak[diff-1] + "],";

        json += "\"skemmtun\":[";
        for(int i = 0; i < diff - 1; i++) {
            json += skemmtun[i]+",";
        }
        json+=skemmtun[diff-1] + "],";

        json += "\"veitingar\":[";
        for(int i = 0; i < diff - 1; i++) {
            json += veitingar[i]+",";
        }
        json+=veitingar[diff-1] + "]}";

        System.out.println(json);
        return json;
    }

    @RequestMapping(value = "/comparison", method = RequestMethod.GET)
    public String comparison(Model model, HttpSession session) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date dateobj = new Date();

        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            User user = userService.findByUsername(sessionUser.getUsername());

            List<Receipt> receipts = receiptService.getReceiptByUser(user);
            JSONWriter.writeComparisonData(user, receipts);
            model.addAttribute("compReceipt", writeComparisonData(receipts));
            return "comparison";
        }
        return "redirect:/";
    }
}
