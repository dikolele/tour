package com.lxs.security.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lxs.core.action.BaseAction;
import com.lxs.core.cache.ClearCache;
import com.lxs.core.common.BeanUtil;
import com.lxs.core.common.SystemConstant;
import com.lxs.security.domain.Dept;
import com.lxs.security.domain.Job;
import com.lxs.security.domain.Role;
import com.lxs.security.domain.User;
import com.lxs.security.service.IUserService;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/security")
@Action(value = "user", className = "userAction")
@Results({
		@Result(name = "add", location = "/WEB-INF/jsp/security/user/add.jsp"),
		@Result(name = "update", location = "/WEB-INF/jsp/security/user/update.jsp"),
		@Result(name = "list", location = "/WEB-INF/jsp/security/user/list.jsp"),
		@Result(name = "listAction", type = "redirect", location = "/security/user!findPage.action"),
		@Result(name = "updateAction", type = "redirect", location = "/security/user!toUpdate.action?id=${id}"),
		@Result(name = "index", type = "redirect", location = "/index.jsp"),
		@Result(name = "login", type = "redirect", location = "/login.jsp")

})
public class UserAction extends BaseAction<User> {
	private Long townId;
	private String factoryName="";
	@Resource
	private IUserService userService;

	private Long roleId;
	private Long deptId;
	private Long jobId;

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * TODO 如果系统处理多个区，这里代码有错误
	 * 查询区的镇
	 * @return
	 */
	public List<Dept> getDistrictTown() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dept.class);
		criteria.add(Restrictions.eq("deptLevel","镇级"));
		return baseService.find(criteria);
	}	
	@Override
	public void beforFind(DetachedCriteria criteria) {
		User u=(User)ActionContext.getContext().getSession().get(SystemConstant.CURRENT_USER);
		u=baseService.get(User.class, u.getId());
		criteria.createAlias("dept", "d");
		criteria.add(Restrictions.like("d.text", factoryName.trim(),MatchMode.ANYWHERE));
		if (u.getDept().getDeptLevel().equals("镇级")) {
			ActionContext.getContext().put("deptLevel", u.getDept().getDeptLevel());
			criteria.createAlias("d.parent", "pd");
			criteria.add(Restrictions.eq("pd.id",u.getDept().getId()));
		}
		if (null!=townId) {//添加镇查询条件
			criteria.createAlias("d.parent", "pd");
			criteria.add(Restrictions.eq("pd.id", townId));
		}
		
		if (null != model.getUserName() && !"".equals(model.getUserName())) {
			criteria.add(Restrictions.like("userName", model.getUserName(),
					MatchMode.ANYWHERE));
		}
	}
	public void checkUserIsRepeat(){
		DetachedCriteria criteria=DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("userName", model.getUserName()));
		List<User> list=baseService.find(criteria);
		if (null!=list&&list.size()!=0) {
			getOut().print("存在");
		}
	}
	public void getCurrentUserToModifyPassword()
			throws JsonGenerationException, JsonMappingException, IOException {
		User u = (User) ServletActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		u = baseService.get(User.class, u.getId());
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("success", true);
		map.put("id", u.getId());
		map.put("userPwd", u.getPassword());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(getOut(), map);
	}

	public void modifiedPassword() {
		User oldUser = baseService.get(modelClass, model.getId());
		BeanUtil.copy(model, oldUser);
		baseService.update(oldUser);
		getOut().print("{'success': true}");
	}

	@Override
	public void afterToUpdate(User user) {
		
		User currentUser=(User)ActionContext.getContext().getSession().get(SystemConstant.CURRENT_USER);
		currentUser=baseService.get(User.class, currentUser.getId());
		DetachedCriteria criteria=DetachedCriteria.forClass(Role.class);
		if (currentUser.getDept().getDeptLevel().equals("镇级")) {
			criteria.add(Restrictions.or(Restrictions.eq("roleName", "企业"),Restrictions.eq("roleName", "镇政府")));
		}
		List<Role> roles = baseService.find(criteria);
		Set<Role> userRoles = user.getRoles();
		for (Role role : userRoles) {
			roles.remove(role);
		}
		ActionContext.getContext().put("roleList", roles);

//		List<Dept> depts = baseService.find(DetachedCriteria
//				.forClass(Dept.class));
//		depts.remove(user.getDept());
//		ActionContext.getContext().put("deptList", depts);

//		List<Job> jobs = baseService.find(DetachedCriteria.forClass(Job.class));
//		Set<Job> userJobs = user.getJobs();
//		for (Job job : userJobs) {
//			jobs.remove(job);
//		}
//		ActionContext.getContext().put("jobList", jobs);
	}

	
	public String addRole() {
		userService.addRole(roleId, model.getId());

		return UPDATE_ACTION;
	}

	public void findUserByName() {
		User currentUser = (User) ActionContext.getContext().getSession()
				.get(SystemConstant.CURRENT_USER);
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(User.class);
		detachedCriteria.add(Restrictions.like("realName", model.getRealName(),
				MatchMode.ANYWHERE));
		List<User> users = baseService.find(detachedCriteria);
		StringBuffer script = new StringBuffer();
		for (User user : users) {
			script.append("<option value=" + user.getId() + ">"
					+ user.getRealName() + "</option>");
		}
		getOut().print(script);
	}

	@ClearCache
	public String deleteRole() {
		userService.deleteRole(roleId, model.getId());

		return UPDATE_ACTION;
	}

	public String addJob() {
		userService.addJob(jobId, model.getId());

		return UPDATE_ACTION;
	}

	public String deleteJob() {
		userService.deleteJob(jobId, model.getId());

		return UPDATE_ACTION;
	}

	/**
	 * 登陆
	 * 
	 * @return
	 */
	public String login() {
		User user = userService.login(model.getUserName(), model.getPassword());
		if (null != user) {
			ActionContext.getContext().getSession()
					.put(SystemConstant.CURRENT_USER, user);
			ActionContext.getContext().getSession()
			.put("currentUserDeptName", user.getDept().getText());
			ActionContext.getContext().getSession()
			.put(SystemConstant.CURRENT_USER, user);
			ActionContext.getContext().getSession()
			.put("currentUserDeptLevel", user.getDept().getDeptLevel());
			return INDEX;
		} else {
			return LOGIN;
		}
	}

	/**
	 * 注销
	 * 
	 * @return
	 */
	public String logout() {
		ActionContext.getContext().getSession()
				.remove(SystemConstant.CURRENT_USER);
		return LOGIN;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public Long getTownId() {
		return townId;
	}
	public void setTownId(Long townId) {
		this.townId = townId;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	

}
