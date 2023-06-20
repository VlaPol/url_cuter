package by.tms.url_cuter.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Setter
@ToString
public class ConvertRecord {
    private String shortName;
    private String url;
}
