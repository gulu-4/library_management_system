package com.example.finance;
    
import java.util.EventObject;

import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.TextEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.entity.BillEntityType ;
import java.util.EventObject;

import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.plugin.AbstractFormPlugin;

//     常量KEY_MAINBAR是菜单栏标识；

// 常量KEY_BARITEM_NEW是按钮标识；

// implements BeforeF7SelectListener  implements ClickListener
public class MyBillModel  extends AbstractFormPlugin  {

 
    private final static String KEY_MAINBAR = "comt_tbar_main";
    private final static String KEY_BARITEM_HELLO = "comt_baritemap";
    private final static String KEY_NAME = "comt_i18nnamefield";

    @Override
    public void registerListener(EventObject e) {
        super.registerListener(e);
        // 侦听主菜单按钮点击事件
        this.addItemClickListeners(KEY_MAINBAR);
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt) {
        if (StringUtils.equals(KEY_BARITEM_HELLO, evt.getItemKey())){//获取点击的按钮编码，和预定的编码相绑定，比较如果相等
            String youName = (String)this.getModel().getValue(KEY_NAME);//获取姓名字段的值 值为String类型
            if (StringUtils.isBlank(youName)){//如果姓名字段为空，显示。。
                this.getView().showMessage("hello, who are you?");
                System.out.println("执行了MyBillModel");
                evt.setCancel(true);    // 取消后续操作 即itemClick不会执行。如果没有进入到这个分支，那么itemClick会被执行
            }
        }
    }

    @Override
    public void itemClick(ItemClickEvent evt) {
        super.itemClick(evt);
        if (StringUtils.equals(KEY_BARITEM_HELLO, evt.getItemKey())){//获取点击的按钮编码，和预定的编码相绑定，比较如果相等
            String youName = (String)this.getModel().getValue(KEY_NAME);
            System.out.println("执行了MyBillModel");
            this.getView().showMessage("hello, " + youName + "!");
        }
    }


    // IBillModel billModel = (IBillModel)this.getModel();


    // private void printEventInfo(String eventName, String argString){
    //     String msg = String.format("%s : %s", eventName, argString);
    //     System.out.println(msg);
    // }
}
