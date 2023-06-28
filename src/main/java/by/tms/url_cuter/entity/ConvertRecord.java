package by.tms.url_cuter.entity;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ConvertRecord {
    private String shortName;
    private String url;
}
