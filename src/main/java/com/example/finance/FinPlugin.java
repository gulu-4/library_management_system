package com.example.finance;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.dataentity.entity.*;
import java.util.*;

public class FinPlugin extends AbstractBillPlugIn{// com.example.finance.FinPlugin
    
    @Override
    public void afterCreateNewData(EventObject e)
    {
        this.getView().showMessage("hello world!");

        DynamicObject dataEntity = this.getModel().getDataEntity();//界面数据包:DynamicObject，基于主实体模型构建的一个数据字典，存储单据体、字段值；
        System.out.println("somedata:"+dataEntity.toString());
    }

}
