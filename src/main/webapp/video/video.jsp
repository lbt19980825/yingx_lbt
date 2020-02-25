<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<script>

    $(function(){
        //初始化表单属性
        $("#uTable").jqGrid({
            url : "${path}/video/showAll",  //分页查询   rows  total recoreds  page  rows
            editurl:"${path}/video/edit",
            datatype : "json",
            rowNum : 2,  //每页展示条数
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#uPager',
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            viewrecords:true,  //是否展示数据总条数
            colNames : [ 'ID', '标题', '简介', '视频封面', '视频地址','发布时间', '用户id','类别id','分组id' ],
            colModel : [
                {name : 'id',width : 130,editable:true,editoptions:{readonly:true,size:10}},
                {name : 'title',editable:true,width : 90,align : "center"},
                {name : 'brief',editable:true,width : 100,align : "center"},
                {name : 'cover',editable:true,width : 80,align : "center",edittype:"file",
                     formatter:function(cellvalue, options, rowObject){
                         return "<img src='"+cellvalue+"' style='height:120px;width:100px;align-content: center'  controls/>";                     }
                },
                {name : 'path',editable:true,editoptions:{readonly:true,size:10},width : 80,align : "center"},
                {name : 'uploadTime',editable:true,editoptions:{readonly:true,size:10},width : 80,align : "center",},
                {name : 'userId',editable:true,width :60,align : "center"},
                {name : 'categoryId',editable:true,width : 80,align : "center",editable:true},
                {name : 'groupId',editable:true,width : 50,sortable : false,align : "center"}
            ]
        });

        //处理增删改查操作   工具栏
        $("#uTable").jqGrid('navGrid', '#uPager',
            {edit : true,add : true,del : true,edittext:"修改",addtext:"添加",deltext:"删除"},
            {}, //执行修改之后的额外操作
            {
                closeAfterAdd:true, //关闭添加的对话框
                afterSubmit:function(data){
                    $.ajaxFileUpload({
                        fileElementId: "cover",    //需要上传的文件域的ID，即<input type="file">的ID。
                        url: "${path}/video/uploadVideo", //后台方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        dataType: 'text',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        //async : true,   //是否是异步
                        data:{id:data.responseText},  //responseText: "74141b4c-f337-4ab2-ada2-c1b07364adee"
                        success: function(data) {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新页面
                            $("#uTable").trigger("reloadGrid");
                        }
                    });
                    //必须要有返回值
                    return "hello";
                }
            }, //执行添加之后的额外操作
            {}  //执行删除之后的额外操作

        );
    });
</script>

<%--初始化一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <center><h2>视频管理</h2></center>
    </div>

    <%--选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">视频展示</a></li>
    </div>

    <%--初始化表单--%>
    <table id="uTable" />

    <%--工具栏--%>
    <div id="uPager" />

</div>