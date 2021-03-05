package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Repositories;


import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptType;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptTypeRepository extends JpaRepository<ReceiptType, Long> {
    ReceiptType save(ReceiptType receiptType);
    void delete(ReceiptType receiptType);

    List<ReceiptType> findByUser(User user);
}
