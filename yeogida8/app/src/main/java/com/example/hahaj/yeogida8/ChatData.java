package com.example.hahaj.yeogida8;

//채팅화면에 뿌려줄 Chatdata

import java.io.Serializable;

public class ChatData implements Serializable {
    private String msg;
    private int personpid;

    public int getPersonpid() {
        return personpid;
    }

    public void setPersonpid(int personpid) {
        this.personpid = personpid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

  }
