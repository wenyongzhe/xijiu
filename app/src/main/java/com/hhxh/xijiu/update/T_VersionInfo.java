package com.hhxh.xijiu.update;

import java.sql.Date;

public class T_VersionInfo {
	public Date CCreateDate;
	public String CFullISN;
	public Boolean CIsPublish;
	public String CName;
	public int CParentID;
	public String CPath;
	public int CProductID;
	public String CRemark;
	public Date getCCreateDate() {
		return CCreateDate;
	}
	public void setCCreateDate(Date cCreateDate) {
		CCreateDate = cCreateDate;
	}
	public String getCFullISN() {
		return CFullISN;
	}
	public void setCFullISN(String cFullISN) {
		CFullISN = cFullISN;
	}
	public Boolean getCIsPublish() {
		return CIsPublish;
	}
	public void setCIsPublish(Boolean cIsPublish) {
		CIsPublish = cIsPublish;
	}
	public String getCName() {
		return CName;
	}
	public void setCName(String cName) {
		CName = cName;
	}
	public int getCParentID() {
		return CParentID;
	}
	public void setCParentID(int cParentID) {
		CParentID = cParentID;
	}
	public String getCPath() {
		return CPath;
	}
	public void setCPath(String cPath) {
		CPath = cPath;
	}
	public int getCProductID() {
		return CProductID;
	}
	public void setCProductID(int cProductID) {
		CProductID = cProductID;
	}
	public String getCRemark() {
		return CRemark;
	}
	public void setCRemark(String cRemark) {
		CRemark = cRemark;
	}
}