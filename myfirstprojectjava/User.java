package com.example.myfirstprojectjava;

import java.io.Serializable;

public class User implements Serializable {
    int[] toSend;
    int money;
    String login;
    String password;

    public User(int id, int[] toSend, int money, String login, String password) {
        this.toSend = toSend;
        this.money = money;
        this.login = login;
        this.password = password;
    }

    public int[] getToSend() {
        return toSend;
    }

    public int getMoney() {
        return money;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
