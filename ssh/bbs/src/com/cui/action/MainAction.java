package com.cui.action;

import com.cui.po.Admin;
import com.cui.po.Board;
import com.cui.po.Post;
import com.cui.po.Student;
import com.cui.service.BoardLoadService;

import com.cui.service.PostLoadService;

import com.cui.util.SessionManager;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class MainAction extends ActionSupport {
private List<Board> boards;
private List<Post> hotPosts;
@Autowired
BoardLoadService boardLoad;
private String sessionId;
private Admin admin;


public Admin getAdmin() {
	return admin;
}

public void setAdmin(Admin admin) {
	this.admin = admin;
}

public Student getStudent() {
	return student;
}

public void setStudent(Student student) {
	this.student = student;
}

private Student student;

public PostLoadService getPostLoad() {
	return postLoad;
}

public void setPostLoad(PostLoadService postLoad) {
	this.postLoad = postLoad;
}

@Autowired
PostLoadService postLoad;


public String execute() {
	boards = boardLoad.loadRootBoards();
	hotPosts = postLoad.rankPosts(6);
	if (sessionId != null && ! SessionManager.IsExist(sessionId) && !SessionManager.IsInitIPAddr(sessionId, ServletActionContext.getRequest().getRemoteAddr())) {
		Object oj = SessionManager.Get(sessionId).getObject();
		if (oj != null) {
			if (oj instanceof Admin) {
				admin = (Admin) oj;
			} else {
				student = (Student) oj;
			}
		}
		if (oj != null) {
			return "USER_LOGIN";
		} else {
			return "TOURIST";
		}
	} else {
		return "TOURIST";
	}
	
}

public List<Board> getBoards() {
	return boards;
}

public void setBoards(List<Board> boards) {
	this.boards = boards;
}

public BoardLoadService getBoardLoad() {
	return boardLoad;
}

public void setBoardLoad(BoardLoadService boardLoad) {
	this.boardLoad = boardLoad;
}


public List<Post> getHotPosts() {
	return hotPosts;
}

public void setHotPosts(List<Post> hotPosts) {
	this.hotPosts = hotPosts;
}

public String getSessionId() {
	return sessionId;
}

public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
}


}

