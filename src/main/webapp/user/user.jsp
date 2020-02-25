<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<script>

    $(function(){

        //初始化表单属性
        $("#uTable").jqGrid({
            url : "${path}/user/showAll",  //分页查询   rows  total recoreds  page  rows
            editurl:"${path}/user/edit",
            datatype : "json",
            rowNum : 5,  //每页展示条数
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#uPager',
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            viewrecords:true,  //是否展示数据总条数
            colNames : [ 'ID', '用户名', '手机号', '头像', '签名','状态', '微信','注册时间','学分','性别','城市' ],
            colModel : [
                {name : 'id',width : 130,serach:true,searchoptions: {sopt:['eq', 'cn',]}},
                {name : 'username',editable:true,width : 90,align : "center",searchoptions: {sopt:['eq', 'cn',]}},
                {name : 'phone',editable:true,width : 100,align : "center",searchoptions: {sopt:['eq', 'cn',]}},
                {name : 'picImg',editable:true,search:false,width : 80,align : "center",edittype:"file",
                    formatter:function(cellvalue, options, rowObject){
                        return "<img src='"+ cellvalue+"' style='height:100px;width:100px' />";
                    }
                },
                {name : 'sign',editable:true,width : 80,align : "center",search:false},
                {name : 'status',width : 80,search:false,align : "center",
                    //cellvalue：具体的值，options：操作的属性，rowObject：行对象一行的数据
                    formatter:function(cellvalue, options, rowObject){

                        if(cellvalue=="冻结"){ //正常状态
                            return "<button class='btn btn-success' id='blocking' onclick='updateStatus(\""+rowObject.id+"\",\""+cellvalue+"\")'>冻结</button> ";
                        }else{  //冻结状态
                            return "<button class='btn btn-danger' id='active' onclick='updateStatus(\""+rowObject.id+"\",\""+cellvalue+"\")'>激活</button>";
                        }
                    }
                },
                {name : 'wechat',editable:true,width :60,align : "center",searchoptions: {sopt:['eq', 'cn',]}},
                {name : 'createDate',width : 80,align : "center",search:false},
                {name : 'score',width : 50,sortable : false,align : "center",search:false},
                {name : 'sex',editable:true,width : 100,align : "center"},
                {name : 'city',editable:true,width : 100,align : "center"}
            ]
        });
        //处理增删改查操作   工具栏
        $("#uTable").jqGrid('navGrid', '#uPager',
            {edit : true,add : true,del : true,edittext:"修改",addtext:"添加",deltext:"删除",},
            {}, //执行修改之后的额外操作
            {
                closeAfterAdd:true, //关闭添加的对话框
                afterSubmit:function(data){
                   $.ajaxFileUpload({
                        fileElementId: "picImg",    //需要上传的文件域的ID，即<input type="file">的ID。
                        url: "${path}/user/uploadUserPicImg", //后台方法的路径
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
            {//执行删除之后的额外操作

            },
            {closeAfterSearch: true}
        );
    });
    //修改用户状态
    function updateStatus(id,status){
        $.ajax({
            url:"${pageContext.request.contextPath}/user/updateStatus",
            type:"post",
            dataType:"text",
            data:{"id":id,"status":status},
            success:function (data) {
                //刷新页面
                $("#uTable").trigger("reloadGrid");
            }
        })
    }
    function exportUser() {
        var path = $("#path").val();
        $.ajax({
            url:"${pageContext.request.contextPath}/user/exportUser",
            type:"post",
            dataType:"text",
            data:{"path":path},
            success:function (data) {
                if(data=="导入成功"){
                    confirm("导入成功")
                }else{
                    alert("导入失败")
                }
            }
        })
    }
    function importUser() {
        var path = $("#path").val();
        alert(path)
        $.ajax({
            url:"${pageContext.request.contextPath}/user/importUser",
            type:"post",
            dataType:"text",
            data:{"path":path},
            success:function (data) {
                if(data=="导入成功"){
                    confirm("导入成功")
                }else{
                    alert("导入失败")
                }
            }
        })
    }

</script>

<%--初始化一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <center><h2>用户管理</h2></center>
    </div>
    <%--选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户信息</a></li>
    </div>
    <input style="width: 500px" type="text" value="请输入文件绝对路径(以.xls文件结尾)，eg：H:\adminPOI.xls" id="path" name="path">
    <button class='btn btn-danger' onclick="exportUser()" >一键导出用户</button>
    <button class='btn btn-success' onclick="importUser()">一键导入用户</button>
    <%--初始化表单--%>
    <table id="uTable" />

    <%--工具栏--%>
    <div id="uPager" />

</div>