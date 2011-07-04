package com.algebraweb.editor.client;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.Uploader;
import gwtupload.client.IUploadStatus.Status;

import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.remotefiller.GraphCanvasRemoteFillingMachine;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.LogicalNodePopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadDialog extends DialogBox {
	
	
	private ControllPanel p;
	private AlgebraEditor e;
	private String awaitingFileUpload;
	
	
	public UploadDialog(ControllPanel p,AlgebraEditor e) {
		
		
		super();
		super.setModal(false);
		super.setAnimationEnabled(true);
		super.setGlassEnabled(false);
		
		super.setText("Upload XML plan");
		this.p=p;
		this.e=e;
		
		SingleUploader defaultUploader = new SingleUploader();
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setWidth("160px");
		defaultUploader.getFileInput().setLength(20);

		Uploader.setStatusInterval(50);

		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStartUploadHandler(onStartUploaderHandler);

		VerticalPanel v = new VerticalPanel();
		
		v.add(defaultUploader);
		
		Button cancelButton = new Button("Cancel");
		
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
				
			}
			
		});
		
		v.add(cancelButton);
		v.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_RIGHT);
		cancelButton.getElement().getStyle().setMargin(10, Unit.PX);
		this.add(v);
		
	}
	

	
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			
			hide();
			
			if (uploader.getStatus() == Status.SUCCESS) {

				String msg = uploader.getServerInfo().message.split("!")[0];
				String idstr = uploader.getServerInfo().message.split("!")[1];
				
				
				GWT.log("Msg:" + msg + " Awaiting: " + awaitingFileUpload);
				
				if (awaitingFileUpload.equals(msg)) {

					GWT.log("received!");
					
					
					String[] ids = idstr.split(":");
					
					e.clearCanvases();
					
					for (String sid : ids) {
						
						final int id = Integer.parseInt(sid);
					
						LogicalCanvas c = e.addCanvas(id);
						//e.changeCanvas(c.getId());
						
						GraphCanvasRemoteFillingMachine f = new GraphCanvasRemoteFillingMachine(c);

						awaitingFileUpload = "";
						f.fill(new RemoteFiller("xml",Integer.toString(id)), new GraphManipulationCallback() {

							@Override
							public void onComplete() {
								//p.getM().validate(id);
								
							}
						});
						
						
					}
					
					
					
			

				}

			}
		}
	};

	private IUploader.OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {

		@Override
		public void onStart(IUploader uploader) {
	
			awaitingFileUpload = Long.toString(System.currentTimeMillis());
			
			String servPath = uploader.getServletPath().split("\\?")[0];
			
			GWT.log(servPath);
			setVisible(false);
			uploader.setServletPath(servPath+ "?myinfo=" + awaitingFileUpload);

		}


	};
}
