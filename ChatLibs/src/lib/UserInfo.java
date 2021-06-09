/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.io.Serializable;

/**
 *
 * @author an9x0
 */
public class UserInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;

    public UserInfo() {
    }

    public UserInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean Equal(UserInfo us2){
        if( this.getUserName().equals(us2.getUserName())  && this.getPassword().equals(us2.getPassword()))
            
            return true;
        return false;
    }
}
