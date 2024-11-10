package studyplanner.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("Select s from User s where s.email = ?1")
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByOauth2UserId(String oauth2UserId);
}