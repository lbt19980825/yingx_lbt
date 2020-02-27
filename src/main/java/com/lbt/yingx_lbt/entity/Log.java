package com.lbt.yingx_lbt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="yx_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log implements Serializable {
    @Id
    private String id;
    @Column(name="admin_name")
    private String adminName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String operate;

    private String message;


}