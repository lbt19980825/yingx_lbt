package com.lbt.yingx_lbt.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySelfPo implements Serializable {
    private String id;
    private String cateName;
    private String levels;
    private String parentId;
    private List<CategorySelfPo> categoryList;

}
