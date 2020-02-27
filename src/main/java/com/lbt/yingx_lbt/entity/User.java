package com.lbt.yingx_lbt.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Table(name="yx_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @Excel(name="ID")
    private String id;

    @Excel(name="用户名")
    private String username;

    @Excel(name="手机号")
    private String phone;

    @Column(name="pic_img")
    @Excel(name="网上图片路径")
    private String picImg;

    @Excel(name="签名")
    private String sign;

    @Excel(name="状态")
    private String status;

    @Excel(name="微信")
    private String wechat;

    @Excel(name="注册时间",format = "yyyy年MM月dd日 hh点mm分ss秒",width = 30)
    @Column(name="create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @Excel(name="学分")
    private Double score;

    private String sex;

    private String city;


}