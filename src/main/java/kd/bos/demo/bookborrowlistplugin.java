package kd.bos.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.Map;


import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.BillShowParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.field.DateEdit;
import kd.bos.servicehelper.user.UserServiceHelper;

public class bookborrowlistplugin extends AbstractBillPlugIn{
    @Override
    public void afterCreateNewData(EventObject e){
        super.afterCreateNewData(e);
        System.out.println("-------------------------------借书单----------------");
        long currentUserId = UserServiceHelper.getCurrentUserId();
        System.out.println(currentUserId);
        this.getModel().setValue("comt_basedatafield", currentUserId);
        DateEdit dateEdit = this.getView().getControl("comt_datefield");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        dateEdit.setMinDate(date);
        System.out.println(formatter.format(date));
        this.getModel().setValue("comt_datetimefield", date);

        BillShowParameter billShowParameter = (BillShowParameter) this.getView().getFormShowParameter();
        Map<String, Object> map = billShowParameter.getCustomParams();
        DynamicObject dynamicObject = (DynamicObject) map.get("selectedBookId");
        System.out.println(dynamicObject);
        String s1 = dynamicObject.get("comt_basedatafield2.comt_billstatusfield").toString();
        
        
        // this.getModel().batchCreateNewEntryRow("entryentity", 0);

        // // Object detail = this.getModel().getValue("comt_entryentity");
        // // DynamicObjectCollection dynamicObjectCollection =(DynamicObjectCollection) detail;
        // // DynamicObject item = dynamicObjectCollection.get(0);
        // //     item.get("comt_basedatafield2.comt_billstatusfield")
        //  this.getModel().setValue("comt_basedatafield1",s1,0);

    }
    
}
