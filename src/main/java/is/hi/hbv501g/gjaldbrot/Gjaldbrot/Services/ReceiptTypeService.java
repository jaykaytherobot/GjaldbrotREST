package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptType;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;

import java.util.List;

public interface ReceiptTypeService {
    List<ReceiptType> getReceiptTypesByUser(User user);
}
