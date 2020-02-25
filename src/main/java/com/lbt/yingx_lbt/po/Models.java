package com.lbt.yingx_lbt.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Models {
    private String titles;
    private List<City> cities;
}
