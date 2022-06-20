package com.example.finance;

import java.util.EventObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.CloseCallBack;
import kd.bos.form.FormShowParameter;
import kd.bos.form.ShowType;
import kd.bos.form.control.Control;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.events.ClosedCallBackEvent;
import kd.bos.form.field.TextEdit;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.entity.BillEntityType ;
import java.util.EventObject;
import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.events.BeforeItemClickEvent;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.bill.AbstractBillPlugIn;
import kd.bos.bill.IBillView;
import kd.bos.entity.datamodel.IBillModel ;
import java.util.EventObject;

import kd.bos.dataentity.utils.StringUtils;
import kd.bos.form.control.Button;
import kd.bos.form.control.Control;
import kd.bos.form.control.EntryGrid;
import kd.bos.form.control.Toolbar;
import kd.bos.form.control.TreeView;
import kd.bos.form.control.events.ClickListener;
import kd.bos.form.control.events.ItemClickEvent;
import kd.bos.form.control.events.ItemClickListener;
import kd.bos.form.control.events.RowClickEvent;
import kd.bos.form.control.events.RowClickEventListener;
import kd.bos.form.control.events.TreeNodeClickListener;
import kd.bos.form.control.events.TreeNodeEvent;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.LocaleString;
import kd.bos.dataentity.metadata.IDataEntityProperty;
import kd.bos.entity.MainEntityType;
import kd.bos.form.plugin.AbstractFormPlugin;
import kd.bos.entity.datamodel.*;
import kd.bos.entity.property.BasedataProp;

public class TestMyBillModel extends AbstractBillPlugIn  {

    private final static String KEY_TITLE = "titlepanel";
    private final static String KEY_MAINBAR = "tbmain";
    private final static String KEY_BARITEM_HELLO = "comt_baritemap";
    private final static String KEY_NAME = "comt_i18nnamefield";
           /** 单据头基础资料字段 */
    private final static String KEY_BASEDATAFIELD = "comt_basedatafield";

    // @Override
    // public void afterCreateNewData(EventObject e) {
        // TODO 在此添加业务逻辑
        // IBillView billView = (IBillView)this.getView();
        // billView.showMessage("hello,TestMyBillModel!!!!");
    // }


    @Override
	public void registerListener(EventObject e) {//用户与界面上的控件进行交互时，即会触发此事件。
		super.registerListener(e);
		// 侦听主菜单按钮点击事件
        // IBillView billView = (IBillView)this.getView();
        // billView.showMessage("Hello");
        // 主菜单按钮点击
        Toolbar mbar = this.getView().getControl(KEY_MAINBAR);
        mbar.addItemClickListener(this);
        // this.addItemClickListeners(KEY_MAINBAR);
	}

    @Override
    public void itemClick(ItemClickEvent evt) {//用户点击菜单项时，触发此事件；
        super.itemClick(evt);
        if (StringUtils.equals(KEY_BARITEM_HELLO, evt.getItemKey())){
            IBillView billView = (IBillView)this.getView();
            // billView.showMessage("hello,TestMyBillModel!!!!");

            DynamicObject refBasedataObj = (DynamicObject) this.getModel().getValue(KEY_BASEDATAFIELD);
            Long refBasedataId = (Long) refBasedataObj.getPkValue();
            billView.showMessage("xxx"+refBasedataId.toString());
            // this.getModel().setValue(KEY_BASEDATAFIELD, refBasedataId);    // 赋值，传入基础资料主键
            // IDataModel model = this.getModel();
            // model.setItemValueByID("money","comt_moneyfield",5000);
        }
    }


    private void useMainEntityType(){
		IDataModel model = this.getModel();
        model.setItemValueByNumber("comt_money","comt_moneyfield",5000);
        // SaveServiceHepler
		// 获取当前表单的主实体模型
		MainEntityType mainEntityType = this.getModel().getDataEntityType();
		
		// 基于表单主实体模型，创建空的数据包（多种方法）
		DynamicObject dataEntity1 = new DynamicObject(mainEntityType);
		DynamicObject dataEntity2 = (DynamicObject)mainEntityType.createInstance();
		
		// 获取主实体部分属性值
		String entityNumber = mainEntityType.getName();		// 实体标识 
		LocaleString entityCaption = mainEntityType.getDisplayName();	// 标题，支持多语言
		String tableName = mainEntityType.getAlias();	// 物理表格
		boolean isDbIgnore = mainEntityType.isDbIgnore();	// 有没有关联物理表格
		String dbRouteKey = mainEntityType.getDBRouteKey();	// 分库标识
		String bizAppId = mainEntityType.getAppId();	// 业务应用标识
		
		// 获取单据头上的字段属性（多种方法）
		IDataEntityProperty billNoProp1 = mainEntityType.getProperties().get("billno");
		IDataEntityProperty billNoProp2 = mainEntityType.getProperty("billno");
		IDataEntityProperty billNOProp3 = mainEntityType.findProperty("billno");
		
		// 获取单据体上的字段属性（多种方法）
		IDataEntityProperty bdProp1 = mainEntityType.getAllEntities().get("entryentity").getProperty("basedatafield1");
		IDataEntityProperty bdProp2 = mainEntityType.findProperty("basedatafield1");
	}



       /**
        * 演示如何读取、设置字段值；
        * @param entryRow 单据体行号
        * @param subEntryRow 子单据体行号
        * @remark
        * 设置单据体字段，需要在参数中加上行号
        */
       private void demoFieldValue(int entryRow, int subEntryRow){
   
           // 方案1:利用表单数据模型存取字段值
           // 在表单插件中，推荐此方案
           DynamicObject refBasedataObj = (DynamicObject) this.getModel().getValue(KEY_BASEDATAFIELD);
           Long refBasedataId = (Long) refBasedataObj.getPkValue();
   
           this.getModel().setValue(KEY_BASEDATAFIELD, refBasedataId);    // 赋值，传入基础资料主键
   
           // 方案2:利用属性对象，操作单据数据包，给字段赋值
           // 这种方式适用于单据操作插件、单据转换插件等，这些场景，没有表单数据模型，只能自行操作数据包
           MainEntityType mainType = this.getModel().getDataEntityType();
           BasedataProp basedataProp = (BasedataProp) mainType.findProperty(KEY_BASEDATAFIELD);
   
           DynamicObject billObj = this.getModel().getDataEntity();    // 演示用的字段在单据头上，调用这个方法，只取单据头数据包，够了
           refBasedataObj = (DynamicObject)basedataProp.getValue(billObj);
           refBasedataId = (Long)basedataProp.getRefIdProp().getValue(billObj);
   
           basedataProp.setValue(billObj, refBasedataObj);     // 赋值；这个方法内部会同步填写主键属性值
   
           // 方案3:直接操作单据数据包，不使用属性对象，不使用数据模型（不推荐）
           refBasedataObj = billObj.getDynamicObject(KEY_BASEDATAFIELD);
           refBasedataId = (Long) refBasedataObj.getPkValue();
   
           billObj.set(KEY_BASEDATAFIELD, refBasedataObj);
           billObj.set(KEY_BASEDATAFIELD + "_id", refBasedataId);    // 这种方法赋值，必须同时填写主键值
       }

}
