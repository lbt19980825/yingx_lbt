package com.lbt.yingx_lbt;

import com.google.gson.Gson;
import com.lbt.yingx_lbt.dao.*;
import com.lbt.yingx_lbt.entity.*;
import com.lbt.yingx_lbt.po.City;
import com.lbt.yingx_lbt.po.MonthAndSex;
import com.lbt.yingx_lbt.service.AdminService;
import com.lbt.yingx_lbt.service.UserService;
import com.lbt.yingx_lbt.service.VideoService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YingxLbtApplicationTests {
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private AdminService adminService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private VideoService videoService;
    @Resource
    private FeedbackMapper feedbackMapper;
    @Resource
    private LogMapper logMapper;
    @Resource
    private LikeMapper likeMapper;

    @Test
    public void contextLoads() {
        List<Admin> admins = adminMapper.selectAll();
        System.out.println(admins);
    }
    @Test
    public void contextLoads1() {
        AdminExample adminExample = new AdminExample();
        adminExample.createCriteria().andUsernameEqualTo("admin");
        List<Admin> admins = adminMapper.selectByExample(adminExample);
        System.out.println(admins);
    }
    @Test
    public void contextLoads11() {
        // adminService.findAdminByUsername(new Admin("1","admin","admin"));
        Admin admin = new Admin();

    }
    @Test
    public void contextLoads112() {
        List<User> users = userMapper.selectAll();
        System.out.println(users);
    }

    @Test
    public void contextLoads11s22() {
        PageBean pageBean = new PageBean();
        pageBean.setPageNo(3);
        List<User> users = userService.queryAllUser(pageBean);
        System.out.println(users);
    }
    @Test
    public void contextLoads11s2s2() {
        List<Category> categories = categoryMapper.selectAll();
        System.out.println(categories);
    }
    @Test
    public void contextLoads11s2ss2() {
        Category category = new Category();
        category.setLevels("1");
        int i = categoryMapper.selectCount(category);
        System.out.println(i);
    }
    @Test
    public void contextLoads11s2sss2() {
        List<Video> videos = videoMapper.selectAll();
        System.out.println(videos);
    }
    @Test
    public void contextLoads11s2sddss2() {

    }
    //测试将文件上传到七牛云
    @Test
    public void tester(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "qSPGX1c7OYRRgNb2npbbPfm9tbcZ_VK8__1Rbs0C";
        String secretKey = "7lYjTpuzufhJWoEB9UWJZYKd2WDiif7orSGgnrE6";
        String bucket = "yingx-videoss";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "H:\\xjbn.mp4";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "xjbn.mp4";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }


    //测试文件下载
    //公开空间下载
    @Test
    public void testDownLoad(){
        String fileName = "xjbn.mp4";  //文件名
        String domainOfBucket = "http://q5uaig5zl.bkt.clouddn.com";   //空间域名
        String finalUrl = String.format("%s/%s", domainOfBucket, fileName);
        System.out.println(finalUrl);  //http://q5u1l78s3.bkt.clouddn.com/人民的名义.mp4  网络路径

        String url="D://"+fileName+"";
        downloadFile(finalUrl,url);

    }

    //私有空间下载
    @Test
    public void testDownLoads() throws UnsupportedEncodingException {

        String fileName = "xjbn.mp4";  //文件名
        String domainOfBucket = "http://q5uaig5zl.bkt.clouddn.com";   //空间域名
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);

        String accessKey = "qSPGX1c7OYRRgNb2npbbPfm9tbcZ_VK8__1Rbs0C";
        String secretKey = "7lYjTpuzufhJWoEB9UWJZYKd2WDiif7orSGgnrE6";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);

        String url="D://人民的名义aaa.mp4";
        downloadFile(finalUrl,url);
    }

    /**
     * 下载远程文件并保存到本地
     *
     * @param remoteFilePath-远程文件路径
     * @param localFilePath-本地文件路径（带文件名）
     * http://q5qobmi0y.bkt.clouddn.com/01.jpg
     */
    public void downloadFile(String remoteFilePath, String localFilePath) {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        try {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //测试获取QiNiuyunPropertirs中的内容
    @Test
   public void sfdf() {
        Properties prop = new Properties();
        //如果properties文件和某个java文件在同一目录下，可以如下读取配置文件：
        InputStream inStream = YingxLbtApplicationTests.class.getClassLoader().getResourceAsStream("QiNiuyunProperties.properties");
        try {
            prop.load(inStream);//properties文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        String keyValue = prop.getProperty("accessKey");
        System.out.println(keyValue);
    }
    //测试删除
    @Test
    public void delete(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region1());
        //...其他参数参考类注释
        String accessKey = "qSPGX1c7OYRRgNb2npbbPfm9tbcZ_VK8__1Rbs0C";
        String secretKey = "7lYjTpuzufhJWoEB9UWJZYKd2WDiif7orSGgnrE6";
        String bucket = "yingx-video1";  //存储空间的名字
        String key = "0200215162614.mp4";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
    @Test
    //测试截取字符串
    public void ewf(){
        //Video video = videoService.queryVideoById("1");
      //  String path = video.getPath();
       /* System.out.println(path);
        String substring = path.substring(33);
        System.out.println(substring);*/
    }

    //测试反馈
    @Test
    public void asdad(){
        List<Feedback> feedbacks = feedbackMapper.selectAll();
        System.out.println(feedbacks);
    }
    //测试日志
    @Test
    public void query(){
        List<Log> logs = logMapper.selectAll();
        //查询共有多少行日志
        int i = logMapper.selectCount(new Log());
        System.out.println(i);
    }
    //测试点赞数   b09819bb-8fd3-411e-bf7b-dc979341b448
    @Test
    public void querysss(){
        LikeExample likeExample = new LikeExample();
        likeExample.createCriteria().andVideoPhotoIdEqualTo("b09819bb-8fd3-411e-bf7b-dc979341b448");
        int i = likeMapper.selectCountByExample(likeExample);
        System.out.println(i);
    }
    //测试每个月份男姓，女性的注册数量
    @Test
    public void dasdsad(){
        HashMap<String, List<String>> map = new HashMap<>();
        ArrayList<String> month = new ArrayList<>();
        ArrayList<String> boys = new ArrayList<>();
        ArrayList<String> womans = new ArrayList<>();
        List<MonthAndSex> monthAndSexes = userMapper.selectManCountByCreateDate();
        for (MonthAndSex monthAndSex : monthAndSexes) {
            month.add(monthAndSex.getMonth());//月份
            boys.add(monthAndSex.getCount());//男姓
        }
        List<MonthAndSex> monthAndSexes1 = userMapper.selectWomanCountByCreateDate();
        for (MonthAndSex monthAndSex : monthAndSexes1) {
            womans.add(monthAndSex.getCount());//女性
        }
        map.put("month",month);
        map.put("boys",boys);
        map.put("womans",womans);
        System.out.println("++++++++++");
        System.out.println(map);
        System.out.println("------------");
    }
    //测试每个月份男姓，女性的注册数量
    @Test
    public void dasdsaddsad(){
        HashMap<String, List<String>> stringListHashMap = userService.selectManOrWomanCount();
        System.out.println(stringListHashMap);
    }

    @Test
    public void dasdad(){
        List<City> cities = userMapper.selectManCountByCity();
        System.out.println(cities);
    }


}
