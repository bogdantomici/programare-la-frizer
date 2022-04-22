package model;

import lombok.*;
import org.dizitart.no2.objects.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Haircut {

    @Id
    private String id;

    private String name;
    private float price;
}
