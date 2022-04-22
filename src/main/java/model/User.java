package model;

import lombok.*;
import org.dizitart.no2.objects.Id;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    @Id
    private String username;

    private String password;
    private String firstName;
    private String secondName;
    private String phoneNumber;
    private String address;
    private String role;

    private List<Haircut> haircutList;

    public User(String username, String password, String firstName, String secondName, String phoneNumber, String address, String role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }
}

