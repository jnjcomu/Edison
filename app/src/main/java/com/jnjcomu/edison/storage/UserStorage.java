package com.jnjcomu.edison.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.jnjcomu.edison.model.Ticket;
import com.jnjcomu.edison.model.User;

/**
 * @author CodeRi <ruto1924@gmail.com>
 * @since 2017-05-07
 */

public class UserStorage {
    private static UserStorage instance;

    private static final String STORAGE_NAME = "user_storage";

    private static final String NAME_PREF = "name";
    private static final String GRADE_PREF = "grade";
    private static final String CLASS_PREF = "class";
    private static final String NUMBER_PREF = "number";
    private static final String TICKET_CD_PREF = "ticket_cd";

    private Preference mPref;

    private String name;
    private Integer grade;
    private Integer clazz;
    private Integer number;
    private Ticket ticket;

    public static UserStorage getInstance(Context context) {
        if (instance == null) instance = new UserStorage(context);

        return instance;
    }

    private UserStorage(Context context) {
        mPref = new Preference(context, STORAGE_NAME);

        name = mPref.getString(NAME_PREF, "");
        grade = mPref.getInt(GRADE_PREF, 0);
        clazz = mPref.getInt(CLASS_PREF, 0);
        number = mPref.getInt(NUMBER_PREF, 0);
        ticket = new Ticket(mPref.getString(TICKET_CD_PREF, ""));
    }

    public String getName() {
        return name;
    }

    public Integer getGrade() {
        return grade;
    }

    public Integer getClazz() {
        return clazz;
    }

    public Integer getNumber() {
        return number;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public User getUser() {
        User user = new User(name, clazz, grade, number);

        user.setName(name);
        user.setClazz(clazz);
        user.setGrade(grade);
        user.setNumber(number);

        return user;
    }

    public UserStorage saveUser(User user) {
        saveUserName(user.getName());
        saveUserGrade(user.getGrade());
        saveUserClazz(user.getClazz());
        saveUserNumber(user.getNumber());

        return this;
    }

    public UserStorage saveUserName(String userName) {
        this.name = userName;

        mPref.putString(NAME_PREF, userName);

        return this;
    }

    public UserStorage saveUserGrade(Integer userGrade) {
        this.grade = userGrade;

        mPref.putInt(GRADE_PREF, userGrade);

        return this;
    }

    public UserStorage saveUserClazz(Integer userClazz) {
        this.clazz = userClazz;

        mPref.putInt(CLASS_PREF, userClazz);

        return this;
    }

    public UserStorage saveUserNumber(Integer userNumber) {
        this.number = userNumber;

        mPref.putInt(NUMBER_PREF, userNumber);

        return this;
    }

    public UserStorage saveUserTicket(Ticket userTicket) {
        this.ticket = userTicket;

        mPref.putString(TICKET_CD_PREF, userTicket.getTicketCode());

        return this;
    }

    /**
     * NOTE : Please! this method need some time.
     */
    public void syncUserData() {
        name = mPref.getString(NAME_PREF, "");
        grade = mPref.getInt(GRADE_PREF, 0);
        clazz = mPref.getInt(CLASS_PREF, 0);
        number = mPref.getInt(NUMBER_PREF, 0);
        ticket.setTicketCode(mPref.getString(TICKET_CD_PREF, ""));
    }

    /**
     * NOTE : WARNING! This method execute dangerous task. Please used careful this method.
     */
    public void resetUserStorage() {
        name = "";
        grade = 0;
        clazz = 0;
        number = 0;
        ticket.setTicketCode("");

        mPref.clear();
    }
}
