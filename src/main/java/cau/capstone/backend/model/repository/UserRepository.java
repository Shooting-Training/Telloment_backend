package cau.capstone.backend.model.repository;

import cau.capstone.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUserId(Long id);

    @Query(value = "SELECT u FROM User u WHERE u.name = ?1")
    User findByName(String name);

    List<User> findUsersByEmailNotIn(List<String> existedList);

    User findByNickName(String nickname);

}
