package com.lbt.yingx_lbt.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class QiNiuYunUtil {
    public static HashMap<String,String> Information(){
        HashMap<String, String> map = new HashMap<>();
        //读取小配置文件
        Properties prop = new Properties();
        //如果properties文件和某个java文件在同一目录下，可以如下读取配置文件：
        //如果出现空指针异常原因可能为：要把小配置文件放在resources目录下
        InputStream inStream = QiNiuYunUtil.class.getClassLoader().getResourceAsStream("QiNiuyunProperties.properties");
        try {
            prop.load(inStream);//properties文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        String accessKey = prop.getProperty("accessKey");
        String secretKey = prop.getProperty("secretKey");
        String dns = prop.getProperty("dns");
        map.put("accessKey",accessKey);
        map.put("secretKey",secretKey);
        //map.put("dns",dns);
        return map;
    }
    //进行文件上传                                 //存储空间的名字   域名
    public static String Upload(MultipartFile file,String bucket,String dns){
        //获取配置文件信息
        HashMap<String, String> map = Information();
        String accessKey = map.get("accessKey");
        String secretKey = map.get("secretKey");
        //String dns = map.get("dns");
        //你的域名
        //1.向七牛云提交文件
        String filename = file.getOriginalFilename();
        String newName=+new Date().getTime()+"-"+filename;
        //将域名与文件名拼接，作为上数据库的图片或视频名
        String name = dns+"/"+newName;
        //2.将文件转为字节数组
        byte[] bytes=null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //3.构造一个带指定Region对象的配置类  参数：指定的区域  华北
        Configuration cfg = new Configuration(Region.region1());
        //4....其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //5....生成上传凭证，然后准备上传

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //String localFilePath = "C:\\Users\\NANAN\\Desktop\\video\\人民的名义.mp4";  //文件本地路径
        //6.默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = newName;
        //根据密钥去做授权
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            //上传   文件上传  文件字节数组
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);  //文件名
            //System.out.println(putRet.hash);  //件内容的hash值
            //http://q5u1l78s3.bkt.clouddn.com/照片.jpg  网络路径
            //http://q5u1l78s3.bkt.clouddn.com/人民的名义.mp4

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return name;
    }
    //删除文件                //云存储中文件的名字
    public static void delete(String key,String bucket){
        //获取小配置文件信息
        HashMap<String, String> map = Information();
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        String accessKey = map.get("accessKey"); //密钥：你的AK
        String secretKey = map.get("secretKey"); //密钥：你的SK

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);//存储空间的名字
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
    //进行文件上传,首个参数传的是文件名    返回上传后的文件名     //存储空间的名字
    public static String Upload(String fileName,String filePath,String bucket){
        //获取配置文件信息
        HashMap<String, String> map = Information();
        String accessKey = map.get("accessKey");
        String secretKey = map.get("secretKey");
        //构造一个带指定Region对象的配置类  参数：指定的区域  华北
        Configuration cfg = new Configuration(Region.region1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

        //String bucket = "184-video";  //存储空间的名字

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //String localFilePath = "C:\\Users\\NANAN\\Desktop\\video\\人民的名义.mp4";  //文件本地路径
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //String key = fileName;
        //根据密钥去做授权
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        DefaultPutRet putRet =null;
        try {
            //上传   文件上传  文件字节数组
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
             putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);  //文件名
            //System.out.println(putRet.hash);  //件内容的hash值
            //http://q5u1l78s3.bkt.clouddn.com/照片.jpg  网络路径
            //http://q5u1l78s3.bkt.clouddn.com/人民的名义.mp4

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return putRet.key;
    }
}
