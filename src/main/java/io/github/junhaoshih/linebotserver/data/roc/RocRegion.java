package io.github.junhaoshih.linebotserver.data.roc;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RocRegion {
    @JsonSetter("ZipCode")
    private String zipCode;


    @JsonSetter("AreaName")
    private String areaName;

    @JsonSetter("AreaEngName")
    private String areaEngName;
}