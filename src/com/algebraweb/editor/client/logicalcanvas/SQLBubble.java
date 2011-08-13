package com.algebraweb.editor.client.logicalcanvas;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResTable;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class SQLBubble extends FlowPanel {

	private SqlResTable t;
	final private RemoteManipulationServiceAsync rmsa;
	private EvaluationContext c;
	private int nid;
	private int pid;

	private FlowPanel p;

	private AsyncCallback<List<Map<String, String>>> sqlCb = new AsyncCallback<List<Map<String, String>>>() {

		@Override
		public void onFailure(Throwable caught) {
			p.clear();
			removeStyleName("loading");

			if (caught instanceof PathFinderCompilationError) {
				p.add(new HTML("Error: SQL compilation failed."));
				return;
			}

			if (caught instanceof SqlError) {
				p.add(new HTML("Error: SQL qry failed on server."));
				return;
			}

			p.add(new HTML("Error."));
		}

		@Override
		public void onSuccess(List<Map<String, String>> result) {
			removeStyleName("loading");
			showResult(result);
		}
	};

	public SQLBubble(int nid, int pid, final RemoteManipulationServiceAsync rmsa, EvaluationContext c, final LogicalCanvas ca) {

		this.rmsa = rmsa;
		this.nid=nid;
		this.pid=pid;
		this.c=c;
		this.addStyleName("sql-bubble");
		p = new FlowPanel();
		this.add(p);
		p.addStyleName("sql-bubble-inner");
		Button b = new Button("");

		p.addDomHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				addStyleName("hover");
			}
		},MouseMoveEvent.getType());

		this.addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {

				removeStyleName("hover");

			}
		},MouseOutEvent.getType());

		b.addStyleName("sql-bubble-close");
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ca.removeSQLListener(SQLBubble.this);

			}
		});

		b.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				addStyleName("hover");
			}
		},MouseMoveEvent.getType());


		Button d= new Button("");
		d.addStyleName("sql-bubble-edit");
		d.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new EditSQLListenerDialog(SQLBubble.this, rmsa, ca);
			}
		});

		d.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				addStyleName("hover");
			}
		},MouseMoveEvent.getType());

		Button refresh= new Button("");
		refresh.addStyleName("sql-bubble-refresh");
		refresh.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				update();
			}
		});

		refresh.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				addStyleName("hover");
			}
		},MouseMoveEvent.getType());
		this.add(d);
		this.add(refresh);
		this.add(b);
	}

	/**
	 * @return the c
	 */
	public EvaluationContext getEvaluationContext() {
		return c;
	}

	public int getNid() {
		return nid;
	}

	public int getPid() {
		return pid;
	}

	/**
	 * @param c the c to set
	 */
	public void setEvaluationContext(EvaluationContext c) {
		this.c = c;
	}

	private void showResult(List<Map<String, String>> res) {
		p.clear();
		t= new SqlResTable(res.size()+1,res.get(0).size());
		p.add(t);
		t.fill(res);
	}

	public void update() {
		p.clear();
		this.addStyleName("loading");
		rmsa.eval(pid, nid, c, false,sqlCb);
	}

}
