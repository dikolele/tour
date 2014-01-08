<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>用户</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery.jsp"%>
<%@ include file="/common/include-bootstap.jsp"%>
<script src="${ctx }/js/grid.js"></script>
<%@ include file="/common/include-styles.jsp"%>
</head>
<body>
	<div class="panel panel-info">
		<div class="panel-heading">
			<div class="btn-group btn-group-sm">
				<button id="addButton"
					actionUrl="${ctx }/tour/notify!toAdd.action" class="btn btn-info">
					<span class="glyphicon glyphicon-plus"></span> 新建
				</button>
				<button id="deleteButton" class="btn btn-info">
					<span class="glyphicon glyphicon-minus"></span> 删除
				</button>
				<button id="queryButton" class="btn btn-info">
					<span class="glyphicon glyphicon-search"></span> 查询
				</button>
			</div>
			<div class="pull-right" style="margin-top: 6px;">
				<a href="javascript:void(0)" title="查询表单"
					id="showOrHideQueryPanelBtn"><span
					class="glyphicon glyphicon-chevron-down pull-right"></span> 查询条件</a>
			</div>
		</div>
		<div class="panel-body hide" id="queryPanel">
			<form role="form" id="queryForm" class="form-horizontal"
				action="${ctx}/tour/notify!findPage.action" method="post">
				<table class="formTable">
					<Tr>
						<Td class="control-label"><label for="title">标题：</label></Td>
						<Td class="query_input"><s:textfield name="title"
								cssClass="form-control" id="title"></s:textfield></Td>
					</Tr>
				</table>
			</form>
		</div>
	</div>
	<form method="post" action="${ctx }/tour/notify!delete.action"
		id="deleteForm">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th class="table_checkbox"><input type="checkbox"
						id="checkAllCheckBox"></th>
					<th>标题</th>
					<th>接收企业用户</th>
					<th>发送时间</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="#page.result">
					<tr>
						<td class="table_checkbox"><input type="checkbox" name="ids"
							value="${id }" /></td>
						<td>${title }</td>
						<Td>${userNames }</Td>
						<td>${createDate }</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</form>
	<tags:pagination page="${page }"></tags:pagination>

</body>
</html>
