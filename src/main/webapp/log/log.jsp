<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<script>

    $(function(){

        //初始化表单属性
        $("#uTable").jqGrid({
            url : "${path}/log/showAll",  //分页查询   rows  total recoreds  page  rows
            datatype : "json",
            rowNum : 20,  //每页展示条数
            rowList : [5,10, 20,30,40,50,60,1000 ],
            pager : '#uPager',
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            viewrecords:true,  //是否展示数据总条数
            colNames : [ 'ID', '管理员姓名', '操作时间', '操作', '状态(是否成功)' ],
            colModel : [
                {name : 'id',width : 130},
                {name : 'adminName',width : 130},
                {name : 'date',width : 130},
                {name : 'operate',width : 130},
                {name : 'message',width : 130},
            ]
        });
    });

</script>

<%--初始化一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <center><h2>日志管理</h2></center>
    </div>

    <%--选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">日志信息</a></li>
    </div>

    <%--初始化表单--%>
    <table id="uTable" />

    <%--工具栏--%>
    <div id="uPager" />

</div>