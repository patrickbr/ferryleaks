package com.algebraweb.editor.client.dialogs;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.Uploader;
import gwtupload.client.IUploadStatus.Status;

import com.algebraweb.editor.client.AlgebraEditor;
import com.algebraweb.editor.client.ControlPanel;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasErrorDialogBox;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The dialog to ask for a plan-file to upload
 * 
 * @author Patrick Brosi
 * 
 */
public class LogicialPlanUploadDialog extends DialogBox {

	private final AlgebraEditor e;
	private String awaitingFileUpload;

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.ERROR) {
				AlgebraEditor.log("Upload finished with error status.");
				new GraphCanvasErrorDialogBox(
						"Error while uploading. Please try again.");
			}

			if (uploader.getStatus() == Status.SUCCESS
					&& uploader.getServerInfo().message != null
					&& uploader.getServerInfo().message.split("!").length > 1) {

				AlgebraEditor.log("Upload finished successfull! Received: '"
						+ uploader.getServerInfo().message + "'");
				String msg = uploader.getServerInfo().message.split("!")[0];
				String idstr = uploader.getServerInfo().message.split("!")[1];

				if (awaitingFileUpload.equals(msg)) {

					String fileName = uploader.getFileName().split("\\\\")[uploader
							.getFileName().split("\\\\").length - 1];

					AlgebraEditor.setSubTitle(fileName);
					String[] ids = idstr.split(":");
					e.clearCanvases();

					for (String sid : ids) {
						AlgebraEditor.log("Loading finished plan #" + sid);
						e.loadFinishedPlanFromServer(Integer.parseInt(sid));
					}
					awaitingFileUpload = "";
				}
			}
			hide();
		}
	};

	private IUploader.OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {

		@Override
		public void onStart(IUploader uploader) {
			awaitingFileUpload = Long.toString(System.currentTimeMillis());
			String servPath = uploader.getServletPath().split("\\?")[0];
			setVisible(false);
			uploader
					.setServletPath(servPath + "?file_id=" + awaitingFileUpload);
		}
	};

	public LogicialPlanUploadDialog(ControlPanel p, final AlgebraEditor e) {
		super();
		super.setModal(false);
		super.setAnimationEnabled(true);
		super.setGlassEnabled(false);

		super.setText("Upload XML plan");
		this.e = e;

		SingleUploader defaultUploader = new SingleUploader();
		defaultUploader.setAutoSubmit(true);
		defaultUploader.getFileInput().setLength(20);
		Uploader.setStatusInterval(50);

		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStartUploadHandler(onStartUploaderHandler);

		VerticalPanel v = new VerticalPanel();
		v.add(defaultUploader);

		Anchor example1Button = new Anchor("Example 1");
		Anchor example2Button = new Anchor("Example 2");

		example1Button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				e.loadExamplePlanFromServer("/examples/example1.xml");
				hide();
			}
		});

		example2Button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				e.loadExamplePlanFromServer("/examples/example2.xml");
				hide();
			}
		});

		FlowPanel pp = new FlowPanel();
		example1Button.addStyleName("examplebutton");
		example2Button.addStyleName("examplebutton");
		pp.add(example1Button);
		pp.add(example2Button);
		v.add(pp);

		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		v.add(cancelButton);
		v.setCellHorizontalAlignment(cancelButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		cancelButton.getElement().getStyle().setMargin(10, Unit.PX);
		this.add(v);
	}
}
