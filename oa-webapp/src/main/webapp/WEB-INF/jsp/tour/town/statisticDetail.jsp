<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>镇统计详情</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<script type="text/javascript"
	src="${ctx }/js/My97DatePicker/WdatePicker.js"></script>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body class="editBody">
	<button class="btn btn-info btn-sm pull-left" id="backButton">
		<span class="glyphicon glyphicon-backward"></span> 返回列表
	</button>
	<div class="clearfix" style="margin-bottom: 20px;"></div>
	<form method="post" id="deleteForm">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>名称</th>
					<th>接待人数</th>
					<th>收入&nbsp;<font color="green">(万元)</font></th>
					<Th>申报时间</Th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="statisticList">
					<tr>
						<td>${user.dept.text }</td>
						<td>${totalPersonNum }</td>
						<td>${totalIncome }</td>
						<td>${reportYear }年${reportMonth}月</td>
						<td><a href="${ctx }/tour/reported!toDetail.action?id=${id}">详情</a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</form>
</body>
</html>