package is.hi.hbv501g.gjaldbrot.Gjaldbrot.Repositories;

import is.hi.hbv501g.gjaldbrot.Gjaldbrot.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    void delete(User user);
    List<User> findAll();
    User findByUName(String uName);
}