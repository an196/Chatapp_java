package lib;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author an9x0
 */
public class Package implements Serializable{
    private static final long serialVersionUID = 1L;
    private String iService;
    private String iUserName;
    private Object iContent = null;

    public Package(String pS, Object pC) {
        iService = pS;
        iContent = pC;
    }
    public Package(String pS, String pU,Object pC) {
        iService = pS;
        iUserName = pU;
        iContent = pC;
    }

    public String getiUserName() {
        return iUserName;
    }

    public String getiService() {
        return iService;
    }

    public void setiContent(Object iContent) {
        this.iContent = iContent;
    }

    public Object getiContent() {
        return iContent;
    }
}
