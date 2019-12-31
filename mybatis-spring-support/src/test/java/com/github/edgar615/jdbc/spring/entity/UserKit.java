package com.github.edgar615.jdbc.spring.entity;

import com.github.edgar615.jdbc.PersistentKit;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;

/**
* This class is generated by Jdbc code generator.
*
* Table : user
*
* @author Jdbc Code Generator
*/
public class UserKit implements PersistentKit<Long, User> {

    private static final long serialVersionUID = 1L;
    
    public static final String USER_ID = "userId";
    
    public static final String TYPE = "type";
    
    public static final String USERNAME = "username";
    
    public static final String HEAD_PIC = "headPic";
    
    public static final String NICKNAME = "nickname";
    
    public static final String FULLNAME = "fullname";
    
    public static final String MOBILE = "mobile";
    
    public static final String MOBILE_AREA = "mobileArea";
    
    public static final String MOBILE_BIND_TIME = "mobileBindTime";
    
    public static final String MAIL = "mail";
    
    public static final String EMAIL_BIND_TIME = "emailBindTime";
    
    public static final String STATE = "state";
    
    public static final String GENDER = "gender";
    
    public static final String BIRTHDAY = "birthday";
    
    public static final String AGE = "age";
    
    public static final String LANGUAGE = "language";
    
    public static final String TIME_ZONE = "timeZone";
    
    public static final String REGION_CODE = "regionCode";
    
    public static final String INTERNAL = "internal";
    
    public static final String ADD_TIME = "addTime";
    

    @Override
    public List<String> fields() {
      return Lists.newArrayList(USER_ID,
						TYPE,
						USERNAME,
						HEAD_PIC,
						NICKNAME,
						FULLNAME,
						MOBILE,
						MOBILE_AREA,
						MOBILE_BIND_TIME,
						MAIL,
						EMAIL_BIND_TIME,
						STATE,
						GENDER,
						BIRTHDAY,
						AGE,
						LANGUAGE,
						TIME_ZONE,
						REGION_CODE,
						INTERNAL,
						ADD_TIME);
    }
    
    @Override
    public String primaryField() {
        return USER_ID;
    }

    @Override
    public void toMap(User user, Map<String, Object> map) {
        if (map == null) {
            return;
        }
        
        map.put(USER_ID,  user.getUserId());
        map.put(TYPE,  user.getType());
        map.put(USERNAME,  user.getUsername());
        map.put(HEAD_PIC,  user.getHeadPic());
        map.put(NICKNAME,  user.getNickname());
        map.put(FULLNAME,  user.getFullname());
        map.put(MOBILE,  user.getMobile());
        map.put(MOBILE_AREA,  user.getMobileArea());
        map.put(MOBILE_BIND_TIME,  user.getMobileBindTime());
        map.put(MAIL,  user.getMail());
        map.put(EMAIL_BIND_TIME,  user.getEmailBindTime());
        map.put(STATE,  user.getState());
        map.put(GENDER,  user.getGender());
        map.put(BIRTHDAY,  user.getBirthday());
        map.put(AGE,  user.getAge());
        map.put(LANGUAGE,  user.getLanguage());
        map.put(TIME_ZONE,  user.getTimeZone());
        map.put(REGION_CODE,  user.getRegionCode());
        map.put(INTERNAL,  user.getInternal());
        map.put(ADD_TIME,  user.getAddTime());
    }

    @Override
    public void fromMap(Map<String, Object> map, User user) {
        if (map == null) {
            return;
        }
        if (map.get(USER_ID) instanceof Long) {
            user.setUserId((Long) map.get(USER_ID));
        }
        if (map.get(TYPE) instanceof Integer) {
            user.setType((Integer) map.get(TYPE));
        }
        if (map.get(USERNAME) instanceof String) {
            user.setUsername((String) map.get(USERNAME));
        }
        if (map.get(HEAD_PIC) instanceof String) {
            user.setHeadPic((String) map.get(HEAD_PIC));
        }
        if (map.get(NICKNAME) instanceof String) {
            user.setNickname((String) map.get(NICKNAME));
        }
        if (map.get(FULLNAME) instanceof String) {
            user.setFullname((String) map.get(FULLNAME));
        }
        if (map.get(MOBILE) instanceof String) {
            user.setMobile((String) map.get(MOBILE));
        }
        if (map.get(MOBILE_AREA) instanceof String) {
            user.setMobileArea((String) map.get(MOBILE_AREA));
        }
        if (map.get(MOBILE_BIND_TIME) instanceof Long) {
            user.setMobileBindTime((Long) map.get(MOBILE_BIND_TIME));
        }
        if (map.get(MAIL) instanceof String) {
            user.setMail((String) map.get(MAIL));
        }
        if (map.get(EMAIL_BIND_TIME) instanceof Long) {
            user.setEmailBindTime((Long) map.get(EMAIL_BIND_TIME));
        }
        if (map.get(STATE) instanceof Integer) {
            user.setState((Integer) map.get(STATE));
        }
        if (map.get(GENDER) instanceof Integer) {
            user.setGender((Integer) map.get(GENDER));
        }
        if (map.get(BIRTHDAY) instanceof String) {
            user.setBirthday((String) map.get(BIRTHDAY));
        }
        if (map.get(AGE) instanceof Integer) {
            user.setAge((Integer) map.get(AGE));
        }
        if (map.get(LANGUAGE) instanceof Integer) {
            user.setLanguage((Integer) map.get(LANGUAGE));
        }
        if (map.get(TIME_ZONE) instanceof Integer) {
            user.setTimeZone((Integer) map.get(TIME_ZONE));
        }
        if (map.get(REGION_CODE) instanceof String) {
            user.setRegionCode((String) map.get(REGION_CODE));
        }
        if (map.get(INTERNAL) instanceof Boolean) {
            user.setInternal((Boolean) map.get(INTERNAL));
        }
        if (map.get(ADD_TIME) instanceof Long) {
            user.setAddTime((Long) map.get(ADD_TIME));
        }
        
    }

   /* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/
	/* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/


}
