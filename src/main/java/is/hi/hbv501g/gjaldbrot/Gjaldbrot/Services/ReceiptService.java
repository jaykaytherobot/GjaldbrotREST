package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.Receipt;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptHost;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ReceiptService {
    Receipt add(Receipt receipt);
    void delete(Receipt receipt);
    void change(Receipt oldReceipt, ReceiptHost newReceipt);
    List<Receipt> getReceiptByUser(User u);

    Receipt getReceiptById(long id);

    Optional<Receipt> findById(long id);

    List<Receipt> findAll();

    ArrayList<Receipt> getReceiptsByMonth(User u, String month);
}
