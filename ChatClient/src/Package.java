
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
    private final String iService;
    private String iMessage;
    private Object iContent;

    public Package(String pS, String pM, Object pC) {
        iService = pS;
        iMessage = pM;
        iContent = pC;
    }

    public String getiService() {
        return iService;
    }

    public String getiMessage() { return iMessage; }

    public Object getiContent() {
        return iContent;
    }
}
