package kd.bos.demo;

import java.util.EventObject;
import java.util.HashMap;

import kd.bos.entity.datamodel.ListSelectedRowCollection;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.list.IListView;
import kd.bos.list.plugin.AbstractListPlugin;

public class booklistplugin extends AbstractListPlugin{
    
    @Override
    public void registerListener(EventObject e){
        super.registerListener(e);
        this.addItemClickListeners("toolbarap");
    }

    @Override
    public void beforeItemClick(BeforeItemClickEvent evt){
        super.beforeItemClick(evt);

        String operationKey = evt.getOperationKey();
        if(operationKey.equals("borrowlist")){
            IListView view = (IListView) this.getView();
            ListSelectedRowCollection selectedRows = view.getSelectedRows();
            if(selectedRows.size() == 0){
                this.getView().showErrorNotification("必须至少选择一本书籍进行借阅");
                evt.setCancel(true);
            }
        }
    }

    @Override
    public void itemClick(ItemClickEvent evt){
        super.itemClick(evt);
        String operationKey = evt.getOperationKey();
        if(operationKey.equals("borrowlist")){
            FormShowParameter p = new FormShowParameter();
            p.setFormId("comt_borrow_form");
            p.setCaption("批量借阅界面");
            p.setShowClose(true);

            HashMap<String, Object> cusMap = new HashMap<String, Object>();

            IListView view = (IListView) this.getView();
            ListSelectedRowCollection selectedRows = view.getSelectedRows();

            Object[] pkArr = selectedRows.getPrimaryKeyValues();

            cusMap.put("selectedBookId", pkArr);
            p.setCustomParams(cusMap);

            p.getOpenStyle().setShowType(ShowType.Modal);

            p.setCloseCallBack(new CloseCallBack(this, "BorrowListFormClose"));

            this.getView().showForm(p);
            
        }
    }


    @Override
    public void closedCallBack(ClosedCallBackEvent closedCallBackEvent){
        super.closedCallBack(closedCallBackEvent);
        Object returnData = this.getView().getReturnData();
        if(returnData.equals("borrowSuccess")){
            this.getView().showSuccessNotification("借阅成功");
        }
    }

}
