package com.github.edgar615.jdbc.spring.entity;

import com.github.edgar615.jdbc.Persistent;
import com.github.edgar615.jdbc.PrimaryKey;
import com.google.common.base.MoreObjects;

/**
* This class is generated by Jdbc code generator.
*
* Table : user
* remarks: 类型：1:个人用户 2-公司用户
状态：1：活动 2：锁定（锁定的用户不能访问系统）
langu
*
* @author Jdbc Code Generator
*/
public class User implements Persistent<Long> {

    private static final long serialVersionUID = 5705048697194337470L;
    
    /**
    * Column : user_id
    * remarks: 用户id
    * default: 
    * isNullable: false
    * isAutoInc: true
    * isPrimary: true
    * type: -5
    * size: 19
    */
    @PrimaryKey
    private Long userId;

    /**
    * Column : type
    * remarks: 类型
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -6
    * size: 3
    */
    private Integer type;
    
    /**
    * Column : username
    * remarks: 用户名
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 60
    */
    private String username;
    
    /**
    * Column : head_pic
    * remarks: 头像
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 1024
    */
    private String headPic;
    
    /**
    * Column : nickname
    * remarks: 昵称
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 16
    */
    private String nickname;
    
    /**
    * Column : fullname
    * remarks: 姓名
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 16
    */
    private String fullname;
    
    /**
    * Column : mobile
    * remarks: 联系电话
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 11
    */
    private String mobile;
    
    /**
    * Column : mobile_area
    * remarks: 手机区号
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 5
    */
    private String mobileArea;
    
    /**
    * Column : mobile_bind_time
    * remarks: 手机号绑定时间
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -5
    * size: 19
    */
    private Long mobileBindTime;
    
    /**
    * Column : mail
    * remarks: 邮箱
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 12
    * size: 60
    */
    private String mail;
    
    /**
    * Column : email_bind_time
    * remarks: 邮箱绑定时间
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -5
    * size: 19
    */
    private Long emailBindTime;
    
    /**
    * Column : state
    * remarks: 状态
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -6
    * size: 3
    */
    private Integer state;
    
    /**
    * Column : gender
    * remarks: 性别
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -6
    * size: 3
    */
    private Integer gender;
    
    /**
    * Column : birthday
    * remarks: 生日
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 1
    * size: 10
    */
    private String birthday;
    
    /**
    * Column : age
    * remarks: 年龄
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer age;
    
    /**
    * Column : language
    * remarks: 语言
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer language;
    
    /**
    * Column : time_zone
    * remarks: 时区
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 4
    * size: 10
    */
    private Integer timeZone;
    
    /**
    * Column : region_code
    * remarks: 地区编码
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: 1
    * size: 6
    */
    private String regionCode;
    
    /**
    * Column : internal
    * remarks: 内部的
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -7
    * size: 1
    */
    private Boolean internal;
    
    /**
    * Column : add_time
    * remarks: 添加时间
    * default: 
    * isNullable: true
    * isAutoInc: false
    * isPrimary: false
    * type: -5
    * size: 19
    */
    private Long addTime;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
    
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getMobileArea() {
        return mobileArea;
    }

    public void setMobileArea(String mobileArea) {
        this.mobileArea = mobileArea;
    }
    
    public Long getMobileBindTime() {
        return mobileBindTime;
    }

    public void setMobileBindTime(Long mobileBindTime) {
        this.mobileBindTime = mobileBindTime;
    }
    
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public Long getEmailBindTime() {
        return emailBindTime;
    }

    public void setEmailBindTime(Long emailBindTime) {
        this.emailBindTime = emailBindTime;
    }
    
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
    
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }
    
    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }
    
    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
    
    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
    
    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper("User")
            .add("userId",  userId)
            .add("type",  type)
            .add("username",  username)
            .add("headPic",  headPic)
            .add("nickname",  nickname)
            .add("fullname",  fullname)
            .add("mobile",  mobile)
            .add("mobileArea",  mobileArea)
            .add("mobileBindTime",  mobileBindTime)
            .add("mail",  mail)
            .add("emailBindTime",  emailBindTime)
            .add("state",  state)
            .add("gender",  gender)
            .add("birthday",  birthday)
            .add("age",  age)
            .add("language",  language)
            .add("timeZone",  timeZone)
            .add("regionCode",  regionCode)
            .add("internal",  internal)
            .add("addTime",  addTime)
           .toString();
    }

    @Override
    public Long id () {
    return userId;
    }

    @Override
    public void setId(Long id) {
        this.userId = id;
    }

    @Override
    public void setGeneratedKey(Number key) {
    
        this.userId = key.longValue();
    
    }

   /* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/
	/* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/


}
