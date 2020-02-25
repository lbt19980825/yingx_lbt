package com.lbt.yingx_lbt;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lbt.yingx_lbt.dao.AdminMapper;
import com.lbt.yingx_lbt.entity.Admin;
import com.lbt.yingx_lbt.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EasyPoiTest {
    @Resource
    private AdminMapper adminMapper;
    @Test
    public void exportTest(){
        List<Admin> admins = adminMapper.selectAll();
        //title:标题  sheetName:表名  实体类类对象  导出的集合
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("管理员信息", "管理员表"), Admin.class, admins);
        try {
            workbook.write(new FileOutputStream(new File("萨达萨达")));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void importTest(){
        //配置导入参数
        ImportParams params = new ImportParams();
        params.setTitleRows(1);  //标题占几行 默认0
        params.setHeadRows(1);  //表头占几行  默认1

        try {
            //获取导入的文件
            FileInputStream inputStream = new FileInputStream(new File("萨达萨达"));
            //导入    参数：读入流,实体类映射
            List<User> teacherList = ExcelImportUtil.importExcel(inputStream,User.class, params);

            //遍历
            for (User teacher : teacherList) {
                System.out.println("用户： "+teacher);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
