package com.example.finance;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.clr.CollectionProperty;
import kd.bos.dataentity.metadata.clr.ComplexProperty;
import kd.bos.entity.botp.plugin.AbstractWriteBackPlugIn;
import kd.bos.entity.botp.plugin.args.AfterCalcWriteValueEventArgs;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import java.math.BigDecimal;


public class Pur_toMoneyPlugin extends AbstractWriteBackPlugIn
{
    private final static String KEY_MONEYNAME = "comt_money";//金额基础资料 整个基础资料的id
    private final static String KEY_BASEDATAFIELD = "comt_basedatafield";//金额基础资料 帮助锁定要修改的基础资料
    private final static String KEY_MONEYFIELD = "comt_moneyfield";//金额基础资料表单上的字段id
    private final static String KEY_AMOUNTFIELD = "comt_amountfield";//目标单 也就是购书申请管理单据上的预付款金额字段

    @Override
    public void afterCalcWriteValue(AfterCalcWriteValueEventArgs e){
        super.afterCalcWriteValue(e);
        DynamicObject activeRow = e.getActiveRow();//获取 激活的行的数据包（选中） 获取每一行单据体信息
        DynamicObject comt_bd_money = (DynamicObject) activeRow.get(KEY_BASEDATAFIELD);//获取单据体数据包中的基础资料数据包
        DynamicObject comt_money = BusinessDataServiceHelper.loadSingle(comt_bd_money.getPkValue(),KEY_MONEYNAME);//根据基础资料主键，
        // 基础资料实体标识找到数据库中的基础资料数据包
        BigDecimal comt_moneyfield = (BigDecimal) comt_money.get(KEY_MONEYFIELD);

        BigDecimal comt_pur_money = (BigDecimal) activeRow.get(KEY_AMOUNTFIELD);//获取金额字段

        // BigDecimal bigDecimal = readPropValue(comt_pur_money);

        BigDecimal subtract = comt_moneyfield.subtract(comt_pur_money);

        comt_money.set("comt_moneyfield",subtract);//对数据包设置值

        SaveServiceHelper.save(new DynamicObject[]{comt_money});//保存
    }

    private BigDecimal readPropValue(DynamicObject dataEntity){

        // 获取数据包对应的实体模型
        IDataEntityType dType = dataEntity.getDataEntityType();
        BigDecimal bigDecimal = null;

        for(IDataEntityProperty property : dType.getProperties()){
            if (property instanceof CollectionProperty){
                // 集合属性，关联子实体，值是数据包集合
                DynamicObjectCollection rows = dataEntity.getDynamicObjectCollection(property);
                // 递归读取子实体各行的属性值
                for (DynamicObject row : rows){
                    this.readPropValue(row);
                }
            }
            else if (property instanceof ComplexProperty){
                // 复杂属性，关联引用的基础资料，值是另外一个数据包
                DynamicObject refDataEntity = dataEntity.getDynamicObject(property);
                // 递归读取引用的基础资料属性值
                if (refDataEntity != null){
                    this.readPropValue(refDataEntity);
                }
            }
            else {

                // 简单属性，对应普通的字段，值是文本、数值、日期、布尔等
                Object propValue = dataEntity.get(property);
                if(propValue instanceof BigDecimal)
                {
                    bigDecimal = bigDecimal.add((BigDecimal)propValue);
                }
                // 输出" 属性名 = 属性值 "
                String msg = String.format("%s = %s", property.getName(), propValue);
                System.out.println(msg);
            }
        }
        return bigDecimal;
    }
}
