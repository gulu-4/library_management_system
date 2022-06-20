package com.example.finance;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import java.math.BigDecimal;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.IBillView;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.events.ItemClickEvent;
import java.util.EventObject;

public class TestCheckButton extends AbstractBillPlugIn {
    private final static String KEY_MAINBAR = "tbmain";
    private final static String KEY_BARITEM = "comt_baritemap";
    private final static String KEY_NAME = "comt_i18nnamefield";
    /** 单据头基础资料字段 */
    private final static String KEY_BASEDATAFIELD = "comt_basedatafield";

    @Override
    public void registerListener(EventObject e) {//用户与界面上的控件进行交互时，即会触发此事件。
        super.registerListener(e);
        Toolbar mbar = this.getView().getControl(KEY_MAINBAR);
        mbar.addItemClickListener(this);
    }

    @Override
    public void itemClick(ItemClickEvent evt) {//用户点击菜单项时，触发此事件；
        super.itemClick(evt);
        if (StringUtils.equals(KEY_BARITEM, evt.getItemKey())){
            IBillView billView = (IBillView)this.getView();
            billView.showMessage("success??");
            DynamicObject refBasedataObj = (DynamicObject) this.getModel().getValue(KEY_BASEDATAFIELD);
            Long refBasedataId = (Long) refBasedataObj.getPkValue();
            DynamicObject comt_money = BusinessDataServiceHelper.loadSingle(refBasedataId,"comt_money");
            BigDecimal comt_moneyfield = (BigDecimal) comt_money.get("comt_moneyfield");
            //对comt_moneyfield进行运算
            
            BigDecimal money =new BigDecimal(30000.00);
            comt_money.set("comt_moneyfield",money);
            SaveServiceHelper.save(new DynamicObject[]{comt_money});
        }
    }
}
