package com.example.finance;

import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.dataentity.metadata.IDataEntityType;
import kd.bos.dataentity.metadata.clr.CollectionProperty;
import kd.bos.dataentity.metadata.clr.ComplexProperty;
// import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.bill.AbstractBillPlugIn;

public class TestO extends AbstractBillPlugIn {
    
    private void useDynamicObject() {

        // 当前表单数据包 
        DynamicObject dataEntity = this.getModel().getDataEntity(true);
        
        // 数据包是否从数据库加载，还是全新创建的？
        boolean isFromDB = dataEntity.getDataEntityState().getFromDatabase();//为什么会全新创建全新数据包？？？
        
        // 读取表单数据包中全部的字段值
        this.readPropValue(dataEntity);
    }
    
    /**
     * 递归读取数据包中全部属性的值
     * 
     * @param dataEntity
     */
    private void readPropValue(DynamicObject dataEntity){
        
        // 获取数据包对应的实体模型
        IDataEntityType dType = dataEntity.getDataEntityType();
        
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
                
                // 输出" 属性名 = 属性值 "
                String msg = String.format("%s = %s", property.getName(), propValue);
                System.out.println(msg);
            }
        }
    }

}
