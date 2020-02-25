package com.lbt.yingx_lbt.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySelfPo {
    private String id;
    private String cateName;
    private String levels;
    private String parentId;
    private List<CategorySelfPo> categoryList;

}
