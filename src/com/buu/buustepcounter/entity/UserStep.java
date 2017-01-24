package com.buu.buustepcounter.entity;

public class UserStep {
	private int _id;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	private int step;
	//计步开始时间
	private String sT;
	//计步结束时间
	private String eT;
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getsT() {
		return sT;
	}
	public void setsT(String sT) {
		this.sT = sT;
	}
	public String geteT() {
		return eT;
	}
	public void seteT(String eT) {
		this.eT = eT;
	}
	
}
