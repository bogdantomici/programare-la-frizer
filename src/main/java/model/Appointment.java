package model;

import lombok.*;
import org.dizitart.no2.objects.Id;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Appointment {

    @Id
    private String id;

    private String barberName;
    private String clientName;
    private String haircutName;
    private float haircutPrice;
    private Date appointmentDate;
}
