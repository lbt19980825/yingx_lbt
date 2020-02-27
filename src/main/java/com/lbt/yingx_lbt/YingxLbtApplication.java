package com.lbt.yingx_lbt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@tk.mybatis.spring.annotation.MapperScan("com.lbt.yingx_lbt.dao")
@org.mybatis.spring.annotation.MapperScan("com.lbt.yingx_lbt.dao")
@SpringBootApplication
public class YingxLbtApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(YingxLbtApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(YingxLbtApplication.class); }
}
