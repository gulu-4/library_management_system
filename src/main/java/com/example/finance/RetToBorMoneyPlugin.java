package com.example.finance;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.entity.botp.plugin.AbstractWriteBackPlugIn;
import kd.bos.entity.botp.plugin.args.AfterCalcWriteValueEventArgs;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import java.math.BigDecimal;

public class RetToBorMoneyPlugin extends AbstractWriteBackPlugIn{

    public void afterCalcWriteValue(AfterCalcWriteValueEventArgs e){
        super.afterCalcWriteValue(e);
        DynamicObject activeRow = e.getActiveRow();
        DynamicObject comt_bd_money = (DynamicObject) activeRow.get("comt_basedatafield");
        DynamicObject comt_money = BusinessDataServiceHelper.loadSingle(comt_bd_money.getPkValue(),"comt_money");
        BigDecimal money =new BigDecimal(6000.00);
        comt_money.set("comt_moneyfield",money);
        SaveServiceHelper.save(new DynamicObject[]{comt_money});
    }
}
