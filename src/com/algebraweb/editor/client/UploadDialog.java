package com.algebraweb.editor.client;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.Uploader;
import gwtupload.client.IUploadStatus.Status;

import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.remotefiller.GraphCanvasRemoteFillingMachine;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadDialog extends DialogBox {
	
	
	private ControllPanel p;
	private String awaitingFileUpload;
	
	
	public UploadDialog(ControllPanel p) {
		
		
		super();
		super.setModal(false);
		super.setAnimationEnabled(true);
		super.setGlassEnabled(false);
		
		super.setText("Upload XML plan");
		this.p=p;
		
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

				if (awaitingFileUpload.equals(uploader.getServerInfo().message)) {

					GraphCanvasRemoteFillingMachine f = new GraphCanvasRemoteFillingMachine(p.getC());

					awaitingFileUpload = "";
					f.fill(new RemoteFiller("xml"), new GraphManipulationCallback() {

						@Override
						public void onComplete() {
							p.getM().validate(0);
							
						}
					});

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
