package studyplanner.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User { //basically the values in the database

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    @Column(name = "oauth2_user_id", unique = true)
    private String oauth2UserId;

    // Constructors, getters, setters
    public User(Long id, String email, String name, String oauth2UserId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.oauth2UserId = oauth2UserId;
    }

    public User(String email, String name, String oauth2UserId) {
        this.email = email;
        this.name = name;
        this.oauth2UserId = oauth2UserId;
    }

    public User() {}

    // Getters and setters for new field
    public String getOauth2UserId() {
        return oauth2UserId;
    }

    public void setOauth2UserId(String oauth2UserId) {
        this.oauth2UserId = oauth2UserId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
