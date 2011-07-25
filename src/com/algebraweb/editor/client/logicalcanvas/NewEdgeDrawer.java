package com.algebraweb.editor.client.logicalcanvas;

import com.hydro4ge.raphaelgwt.client.Raphael.Path;

public class NewEdgeDrawer {



	private LogicalCanvas c;
	private Path p;
	private Path arrowPath;
	private double x;
	private double y;

	public NewEdgeDrawer(double x, double y,LogicalCanvas c) {

		this.x=x;
		this.y=y;
		p=c.new Path("m" + this.x + " " + this.y + " l" + x + " " + y);
		arrowPath = c.new Path("");
		p.attr("stroke-width","2");
		p.attr("stroke","#B40404");
		arrowPath.attr("stroke-width","2");
		arrowPath.attr("stroke","#B40404");
		arrowPath.attr("fill","#B40404");
		moveTo(x,y);

	}

	public Path getArrowPath() {
		return arrowPath;
	}
	
	public Path getPath() {
		return p;
	}
	
	public void moveTo(double x,double y) {
		
		

		double angle = Math.atan2(this.y-y+30,this.x-x+30);
		angle = ((angle / (2 * Math.PI)) * 360);
		
		p.attr("path", "M" + this.x + " " + this.y + " Q" + (x-30) + " " + (y-30) + " " + x + " " + y);
		arrowPath.attr("path","M" + this.x + "," + this.y + "," + (this.x - 5) + "," + (this.y-5) + " L" + (this.x -5) + "," + (this.y+5) + " L" + this.x + "," + this.y);
		
		arrowPath.rotate(angle,this.x,this.y);
		
	}


}
