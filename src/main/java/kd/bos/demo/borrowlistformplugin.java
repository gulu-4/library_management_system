package kd.bos.demo;

import java.util.EventObject;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import kd.bos.form.FormShowParameter;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.operate.EntityOperate;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.servicehelper.user.UserServiceHelper;

public class borrowlistformplugin extends AbstractFormPlugin{
    @Override
    public void afterCreateNewData(EventObject e){
        super.afterCreateNewData(e);
        long currentUserId = UserServiceHelper.getCurrentUserId();
        this.getModel().setValue("comt_basedatafield", currentUserId);

        FormShowParameter formShowParameter = this.getView().getFormShowParameter();
        Map<String, Object> map = formShowParameter.getCustomParams();
        JSONArray pkArr = (JSONArray) map.get("selectedBookId");
        int size =  pkArr.size();
        this.getModel().setValue("comt_textfield",size);

    }
    
    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args){
        super.beforeDoOperation(args);
        Object source = args.getSource();
        if(source instanceof EntityOperate){
            EntityOperate opt = (EntityOperate) source;
            if("btnok".equals(opt.getOperateKey())){
                Object campus = this.getModel().getValue("comt_basedatafield1");
                if(campus == null){
                    this.getView().showErrorNotification("必须选校区");
                    args.setCancel(true);
                }
            }
        }
    }
    
    @Override
    public void afterDoOperation(AfterDoOperationEventArgs args){
        super.afterDoOperation(args);
        if(args.getOperateKey().equals("btnok")){
            this.getView().returnDataToParent("borrowSuccess");
            this.getView().close();
        }

    }
}
