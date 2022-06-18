package kd.demo.purapply;

import java.util.EventObject;
import java.util.List;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.ILocaleString;
import kd.bos.entity.datamodel.events.PropertyChangedArgs;
import kd.bos.form.field.BasedataEdit;
import kd.bos.form.field.events.BeforeF7SelectEvent;
import kd.bos.form.field.events.BeforeF7SelectListener;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.user.UserServiceHelper;

public class PurApplyEdit extends AbstractBillPlugIn implements BeforeF7SelectListener{

    private final static Log logger = LogFactory.getLog(PurApplyEdit.class);

    // @Override
    // public void afterCreateNewData(EventObject e){
    //     String userId = RequestContext.get().getUserId();
    //     long mainOrgId = UserServiceHelper.getUserMainOrgId(Long.valueOf(userId));
    //     logger.info("当前登录人员的主职部门为：" + mainOrgId);
        
    //     IDataModel model = getModel();
        
    //     logger.info(model.getDataEntity().toString());

    //     model.setValue( "kdec_appluorg", mainOrgId);
       
    //     super.afterCreateNewData(e);


    // }

    @Override
    public void registerListener(EventObject e){

        super.registerListener(e);
        BasedataEdit basedataEdit = this.getControl("comt_apply_org");
        basedataEdit.addBeforeF7SelectListener(this);
        

    }

    @Override
    public void beforeF7Select(BeforeF7SelectEvent arg0) {
        // TODO Auto-generated method stub

        String name = arg0.getProperty().getName();

        if("comt_apply_org".equals(name)){
            Object comt_applicant = this.getModel().getValue("comt_applicant");
            if(null != comt_applicant){

                //单据体的一行等
                DynamicObject applicant = (DynamicObject) comt_applicant;
                logger.info("---------------------" + applicant.toString());

                long pkValue = (long)applicant.getPkValue();
                List<Long> orgsUserJoin = UserServiceHelper.getOrgsUserJoin(pkValue);

                QFilter ids = new QFilter("id", QCP.in, orgsUserJoin);

                arg0.addCustomQFilter(ids);

                logger.info("选择申请人部门信息");

            }
        }
    }


    @Override
    public void afterCreateNewData(EventObject e){

        String username = RequestContext.get().getUserName();
        this.getModel().setValue("comt_comment", username + "需要采购：");
    }

    @Override
    public void propertyChanged(PropertyChangedArgs e){
        String name = e.getProperty().getName();
        Object newValue = e.getChangeSet()[0].getNewValue();
        if("comt_applicant".equals(name)){

            if(newValue instanceof DynamicObject){
                DynamicObject applicant = (DynamicObject) newValue;
                ILocaleString localeString = applicant.getLocaleString("name");
                this.getModel().setValue("comt_comment", localeString + "需要采购：");
            }

        }

    }

    
}
