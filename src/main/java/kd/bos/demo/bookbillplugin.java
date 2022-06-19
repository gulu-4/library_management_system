package kd.bos.demo;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;

import com.dingtalk.api.request.OapiRhinoMosExecPerformInvalidbyentopRequest.EntityOperation;

import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.BillShowParameter;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.entity.datamodel.IDataModel;
import kd.bos.entity.report.CellStyle;
import kd.bos.form.ConfirmCallBackListener;
import kd.bos.form.ConfirmTypes;
import kd.bos.form.IPageCache;
import kd.bos.form.MessageBoxOptions;
import kd.bos.form.ShowType;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.events.AfterDoOperationEventArgs;
import kd.bos.form.events.BeforeDoOperationEventArgs;
import kd.bos.form.events.MessageBoxClosedEvent;
import kd.bos.form.operate.EntityOperate;


public class bookbillplugin extends AbstractBillPlugIn{
    
    @Override
    public void afterLoadData (EventObject e){
        super.afterLoadData(e); 
        IDataModel model = this.getModel();
        Object detail = model.getValue("comt_entryentity");
        DynamicObjectCollection dynamicObjectCollection =(DynamicObjectCollection) detail;
        int cnt = 0;
        for( DynamicObject  item :  dynamicObjectCollection){
            // Object comt_basedatafield2 = item.get("comt_basedatafield2"); //图书对象
            if(item.get("comt_basedatafield2.comt_billstatusfield")==null)
                return;
            String status = item.get("comt_basedatafield2.comt_billstatusfield").toString();
            if (status.equals("A")){
                cnt++;
            }
            
        }
        model.setValue("comt_textfiel9",dynamicObjectCollection.size());
        model.setValue("comt_textfield10",cnt);   

    }

    @Override
    public void afterBindData(EventObject e){
        super.afterBindData(e);

        Object detail = this.getModel().getEntryEntity("comt_entryentity");
        System.out.println(detail);
        DynamicObjectCollection dynamicObjectCollection =(DynamicObjectCollection) detail;
        System.out.println(detail);
        System.out.println(dynamicObjectCollection.size());
        EntryGrid detailControl = (EntryGrid) this.getView().getControl("comt_entryentity");
        System.out.println(detailControl);

        for(int i = 0; i< dynamicObjectCollection.size(); i++){
            System.out.println(dynamicObjectCollection.size());
            DynamicObject item = dynamicObjectCollection.get(i);
            System.out.println(item);
            System.out.println(item.get("comt_basedatafield2.comt_billstatusfield"));
            if(item.get("comt_basedatafield2.comt_billstatusfield")==null)
                return;
            String status = item.get("comt_basedatafield2.comt_billstatusfield").toString();
            System.out.println(status);
                
            if(status.equals("A")){
                ArrayList<CellStyle> cellStyles = this.setForeSize("green", i);
                detailControl.setCellStyle(cellStyles);
            }
            if(status.equals("B")){
                ArrayList<CellStyle> cellStyles = this.setForeSize("orange", i);
                detailControl.setCellStyle(cellStyles);
            }
            if(status.equals("C")){
                ArrayList<CellStyle> cellStyles = this.setForeSize("red", i);
                detailControl.setCellStyle(cellStyles);
            }
            if(status.equals("D")){
                ArrayList<CellStyle> cellStyles = this.setForeSize("gray", i);
                detailControl.setCellStyle(cellStyles);
            }
        }
    }

    @Override
    public void beforeDoOperation(BeforeDoOperationEventArgs args){
        super.beforeDoOperation(args);
        System.out.println("-----------before-----------------");
        Object source = args.getSource();
        if(source instanceof EntityOperate){
            EntityOperate opt = (EntityOperate) source;
            String operatekey = opt.getOperateKey();
            System.out.println(operatekey);
            if(operatekey.equals("borrow")){
                EntryGrid entryentity = (EntryGrid) this.getView().getControl("comt_entryentity");

                int[] selectRows = entryentity.getSelectRows();

                if(selectRows.length > 1){
                    this.getView().showErrorNotification("每次只能选择借阅一本书");
                    args.setCancel(true);
                    return;
                }
                else if(selectRows.length == 0){
                    this.getView().showErrorNotification("至少选择借阅一本书");
                    args.setCancel(true);
                    return;
                }

                Object detail = this.getModel().getEntryEntity("comt_entryentity");
                DynamicObjectCollection dynamicObjectCollection =(DynamicObjectCollection) detail;
                int selectRow = selectRows[0];
                DynamicObject dynamicObject =dynamicObjectCollection.get(selectRow);
                String s = dynamicObject.get("comt_basedatafield2.comt_billstatusfield").toString();
                if(!s.equals("A")){
                    this.getView().showErrorNotification("只有可借阅状态的书籍才能借阅");
                    args.setCancel(true);
                    return;
                }

                IPageCache pageCache = this.getView().getPageCache();
                pageCache.put("seelctedRowIndex",String.valueOf(selectRow));
             }


        }
    }

    @Override
    public void afterDoOperation(AfterDoOperationEventArgs args){
        super.afterDoOperation(args);
        System.out.println("-----------after-----------------");
        String operatekey = args.getOperateKey();
        System.out.println(operatekey);
        if(operatekey.equals("borrow")){
            System.out.println("借阅");
            // ConfirmCallBackListener borrowConfirm = new ConfirmCallBackListener("borrowconfirm",this);
            // this.getView().showConfirm("是否进行借阅？",MessageBoxOptions.OKCancel,ConfirmTypes.Default, borrowConfirm);
            BillShowParameter p = new BillShowParameter();
            p.setFormId("comt_borrowbooks");
            p.setCaption("借书单");
            p.setShowClose(true);

            // p.setCloseCallBack(new CloseCallBack(this, "BorrowListFormClose"));

            HashMap<String, Object> cusMap = new HashMap<String, Object>();

            EntryGrid entryentity = (EntryGrid) this.getView().getControl("comt_entryentity");
            int[] selectedRows = entryentity.getSelectRows();

            Object detail = this.getModel().getEntryEntity("comt_entryentity");
            DynamicObjectCollection dynamicObjectCollection =(DynamicObjectCollection) detail;

            int selectedRow = selectedRows[0];
            System.out.println("被选中的行"+selectedRow);

            DynamicObject dynamicObject =dynamicObjectCollection.get(selectedRow);


            cusMap.put("selectedBookId", dynamicObject);
            System.out.println(dynamicObject);
            p.setCustomParams(cusMap);
            p.getOpenStyle().setShowType(ShowType.Modal);
            this.getView().showForm(p);
        }
        
    }

    // @Override
    // public void confirmCallBack(MessageBoxClosedEvent event){
    //     super.confirmCallBack(event);
    //     System.out.println("-------------");
    //     String callBackId = event.getCallBackId();
    //     System.out.println(callBackId);
    //     if(callBackId.equals("borrowconfirm")){
    //         System.out.println("借阅成功");
    //         this.getView().showSuccessNotification("借阅成功");
    //         this.getView().updateView();
    //     }
    // }



    private ArrayList<CellStyle> setForeSize(String color , int index){
        CellStyle cellStyle = new CellStyle();
                cellStyle.setFieldKey("comt_basedatapropfield1");
                cellStyle.setRow(index);
                cellStyle.setForeColor(color);

                ArrayList<CellStyle> cellStyles = new ArrayList<>();
                cellStyles.add(cellStyle);
                // detailControl.setCellStyle(cellStyles);
                return cellStyles;
    }

    // @Override
    // public void registerListener(EventObject e){
    //     super.registerListener(e);
    // }
}
