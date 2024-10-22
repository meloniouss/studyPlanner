package UserJv;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User { //basically the values in the database

    @Id
    @Column(nullable = false, unique = true)
    private String email;
    private String name;

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
}
