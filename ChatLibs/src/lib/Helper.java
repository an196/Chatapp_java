/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import javax.swing.JTextPane;

/**
 *
 * @author an9x0
 */
public class Helper {
    public void UpdateChatPane(JTextPane tp, String content){
        tp.setText(tp.getText() + content +  "\n");
    } 
    
}
