<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<script>

    $(function(){
        pageInit();
    });

    //创建父表格
    function pageInit(){
        $("#cateTable").jqGrid({
            url : "${path}/category/showAllFirstCategory", //分页查询一级类别
            editurl: "${path}/category/edit" ,
            datatype : "json",
            rowNum : 3,
            rowList : [2,3,4, 5,6,7,8,9, 10, 20, 30 ],
            pager : '#catePager',//工具栏
            viewrecords : true,
            styleUI:"Bootstrap",
            height : "auto",
            autowidth:true,
            colNames : [ 'Id', '名称', '级别'],
            colModel : [
                {name : 'id',editable:true,editoptions:{readonly:true,size:10},width :60,align : "center"},
                {name : 'name',editable:true,width :60,align : "center"},
                {name : 'levels',editable:true,editoptions:{readonly:true,size:10},width :60,align : "center"},
            ],
            subGrid : true, //是否开启子表格
            //1.subgrid_id 点击一行时会在在父表格中创建一个div用来容纳子表格的  subgrid_id就是这个div的id
            //2.row_id 点击行的id
            subGridRowExpanded : function(subgridId, rowId) {

                addSubGrid(subgridId, rowId);
            }
        });

        //父表格的工具栏
        $("#cateTable").jqGrid('navGrid', '#catePager', {add : true,edit : true,del : true},
            {closeAfterEdit:true },//修改操作
            {
                closeAfterAdd:true,
            }, //添加操作关闭添加的对话框
            {
                closeAfterDel:true,
                afterSubmit:function(data) {
                    //向警告框中追加错误信息
                    $("#delCate").html(data.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();
                    //自动关闭
                    setTimeout(function(){
                        $("#deleteMsg").hide();
                    },3000);
                     return "hello";
                }
            }   //删除操作
            );
    }

    //创建子表格的属性
    function addSubGrid(subgridId, rowId){
        var subgridTableId= subgridId + "Table";
        var pagerId= subgridId+"Page";

        //在父表格创建出的div中创建子表格的table,工具栏
        $("#"+subgridId).html("<table id='"+subgridTableId+"' /><div id='"+pagerId+"' />");

        //配置子表格的属性
        $("#" + subgridTableId).jqGrid({
            url : "${path}/category/showAllSecondByParentId?parentId=" + rowId, //rowId 以及类别id 根据以及类别id查询所对应的二级类别
            editurl:"${path}/category/secondEdit?parentId=" + rowId,
            datatype : "json",
            rowNum : 2,
            rowList : [ 2,5, 10, 20, 30 ],
            pager : "#"+pagerId,
            styleUI:"Bootstrap",
            height : "auto",
            viewrecords:true,
            autowidth:true,
            colNames : [ 'Id', '名称', '级别',"所属类别id"],
            colModel : [
                {name : 'id',index : 'id',editable:true,editoptions:{readonly:true,size:10}, width : 55},
                {name : 'name',index : 'invdate',editable:true,width : 90},
                {name : 'levels',index : 'name',editable:true,editoptions:{readonly:true,size:10},width : 100},
                {name : 'category.id',index : 'name',editable:true,editoptions:{readonly:true,size:10}, width : 100},
            ],
        });

        //子表格的工具栏
        $("#" + subgridTableId).jqGrid('navGrid',"#" + pagerId, {edit : true,add : true,del : true},
            {closeAfterEdit:true },//修改操作
            {closeAfterAdd:true}, //添加操作关闭添加的对话框
            {
                closeAfterDel:true,
                afterSubmit:function(data) {
                    //向警告框中追加错误信息
                    $("#delCate").html(data.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();
                    //自动关闭
                    setTimeout(function(){
                        $("#deleteMsg").hide();
                    },3000);
                    return "hello";
                }
            }   //删除操作
            );
    }

</script>

<%--初始化一个面板--%>
<div class="panel panel-success">


    <%--面板头--%>
    <div class="panel panel-heading">
        <center><h2>类别管理</h2></center>
    </div>
    <%--警告提示框--%>
    <div id="deleteMsg" class="alert alert-danger" style="height: 50px;width: 300px;display: none" align="center">
        <span id="delCate">sadsad</span>
    </div>
    <%--选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">类别信息 <span id="msgDiv"></span></a></li>
    </div>

    <%--初始化表单--%>
    <table id="cateTable" />

    <%--工具栏--%>
    <div id="catePager" />

</div>