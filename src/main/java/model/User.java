package model;

import lombok.*;
import org.dizitart.no2.objects.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}

