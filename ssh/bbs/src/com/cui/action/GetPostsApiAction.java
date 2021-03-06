package com.cui.action;

import com.alibaba.druid.support.json.JSONUtils;
import com.cui.po.Post;

import com.cui.service.PostLoadService;
import com.cui.util.Session;
import com.cui.util.SessionManager;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetPostsApiAction extends ActionSupport {
private int board;
private int start;
private int length;
private int draw;
private String data;
private List<Post> posts;

public String getSessionId() {
	return sessionId;
}

public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
}

private String sessionId;
@Autowired
private PostLoadService postLoadService;
private List<Map<String, Object>> currentPagePosts = new ArrayList<>();

public int getStart() {
	return start;
}

public void setStart(int start) {
	this.start = start;
}

public int getLength() {
	return length;
}

public void setLength(int length) {
	this.length = length;
}

public String execute() {
	posts = postLoadService.pageAllPost(board, start, length);
	for (Post post : posts) {
		Map<String, Object> data = new HashMap<>(0);
		if (post.getAid() != null) {
			data.put("author", post.getAid().getNickname());
			if (sessionId != null && ! SessionManager.IsExist(sessionId)) {
				data.put("postName", "<a href='/post?sessionId=" + getSessionId() + "&post=" + post.getId() + "' style='color:red;'>【管理员发帖】" + post.getName() + "</a>");
			} else {
				data.put("postName", "<a href='/post?post=" + post.getId() + "' style='color:red;'>【管理员发帖】" + post.getName() + "</a>");
			}
			
			data.put("publishTime", post.getPublishTime());
			data.put("readCount", post.getCount());
			data.put("replyCount", post.getReplies().size());
			currentPagePosts.add(data);
		}
	}
	for (Post post : posts) {
		Map<String, Object> data = new HashMap<>(0);
		if (post.getSid() != null) {
			data.put("author", post.getSid().getNickName());
			if (sessionId != null && ! SessionManager.IsExist(sessionId)) {
				data.put("postName", "<a href='/post?sessionId=" + getSessionId() + "&post=" + post.getId() + "' style='color:black;'>" + post.getName() + "</a>");
			} else {
				data.put("postName", "<a href='/post?post=" + post.getId() + "' style='color:black;'>" + post.getName() + "</a>");
			}
			data.put("publishTime", post.getPublishTime());
			data.put("readCount", post.getCount());
			data.put("replyCount", post.getReplies().size());
			currentPagePosts.add(data);
		}
		
	}
	Map<String, Object> jsons = new HashMap<>();
	int allPostCount = postLoadService.getBoardPostsCount(board);
	jsons.put("code", 200);
	jsons.put("msg", "");
	jsons.put("error", 0);
	jsons.put("data", currentPagePosts);
	jsons.put("recordsTotal", allPostCount);
	jsons.put("draw", draw);
	jsons.put("recordsFiltered", allPostCount);
	String json = JSONUtils.toJSONString(jsons);
	HttpServletResponse response = ServletActionContext.getResponse();
	response.setContentType("application/json;charset=UTF-8");
	try {
		response.getWriter().println(json);
	} catch (IOException e) {
		e.printStackTrace();
	}
	return null;
}

public String getData() {
	return data;
}

public void setData(String data) {
	this.data = data;
}

public int getBoard() {
	return board;
}

public void setBoard(int board) {
	this.board = board;
}


public PostLoadService getPostLoadService() {
	return postLoadService;
}

public void setPostLoadService(PostLoadService postLoadService) {
	this.postLoadService = postLoadService;
}

public List<Post> getPosts() {
	return posts;
}

public void setPosts(List<Post> posts) {
	this.posts = posts;
}


public List<Map<String, Object>> getCurrentPagePosts() {
	return currentPagePosts;
}

public void setCurrentPagePosts(List<Map<String, Object>> currentPagePosts) {
	this.currentPagePosts = currentPagePosts;
}

public int getDraw() {
	return draw;
}

public void setDraw(int draw) {
	this.draw = draw;
}
}
