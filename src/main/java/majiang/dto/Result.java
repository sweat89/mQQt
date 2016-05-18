package majiang.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Result {

	private int status;   //数据请求状态　　　0：成功　　　　-100未登陆　-101 系统故障
	private Object data;  //业务数据
	private String msg;   //业务描述
	private Object extra; //其他业务数据

	
	public Result() {
		// TODO Auto-generated constructor stub
	}

	public Result(int status, Object data, String msg) {
		this.status = status;
		this.data = data;
		this.msg = msg;
		this.extra="";
	}

	public Result(int status, Object data, String msg, Object extra) {
		this.status = status;
		this.data = data;
		this.msg = msg;
		this.extra = extra;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
}
