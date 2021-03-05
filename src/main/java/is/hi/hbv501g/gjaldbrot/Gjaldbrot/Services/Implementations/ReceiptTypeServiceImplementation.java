package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.Implementations;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.ReceiptType;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Repositories.ReceiptRepository;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Repositories.ReceiptTypeRepository;
import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Services.ReceiptTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptTypeServiceImplementation implements ReceiptTypeService {

    ReceiptTypeRepository repository;

    @Autowired
    public ReceiptTypeServiceImplementation(ReceiptTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReceiptType> getReceiptTypesByUser(User user) {
        return repository.findByUser(user);
    }
}
