package com.algebraweb.editor.server.graphcanvas.remotesorter.dotsorter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.server.graphcanvas.remotesorter.RemoteSorter;

public class DotSorter implements RemoteSorter {

	@Override
	public HashMap<Integer, Coordinate> getCoordinateHashMap(
			ArrayList<RawNode> nodes, ArrayList<RawEdge> edges) {


		HashMap<String,HashMap<String,String>> map = getDot(getDotCode(nodes,edges));

		HashMap<Integer, Coordinate> ret = new HashMap<Integer, Coordinate>();

		Iterator<RawNode> i = nodes.iterator();

		while (i.hasNext()) {

			RawNode c = i.next();


			double x = Double.parseDouble(map.get("n_" + c.getNid()).get("pos").split(",")[0]);
			double y = Double.parseDouble(map.get("n_" + c.getNid()).get("pos").split(",")[1]);

			double height = Double.parseDouble(map.get("graph").get("bb").split(",")[3]);

			ret.put(c.getNid(),new Coordinate(x-c.getWidth()/2,(height-y)-c.getHeight()/2));

		}


		return ret;
	}



	private HashMap<String,HashMap<String,String>> getDot(String dotSource) {

		HashMap<String,HashMap<String,String>> ret = new HashMap<String,HashMap<String,String>>();


		Runtime rt = Runtime.getRuntime();

		String dot_path = "/usr/bin/dot";

		String[] args = {dot_path};

		try {
			Process p = rt.exec(args);

			BufferedReader stdInput = new BufferedReader(new 
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(p.getErrorStream()));

			BufferedOutputStream b = new BufferedOutputStream(p.getOutputStream());


			OutputStreamWriter w = new OutputStreamWriter(b);

			w.write(dotSource);
			w.close();


			String s;
			String retStr="";

			while ((s = stdInput.readLine()) != null) {

				System.out.println(s);
				retStr+=s;

			}

			retStr = retStr.split("\\{")[1].split("\\}")[0];

			String[] lines = retStr.split(";");

			for(String line : lines) {


				String id =line.trim().split("\\[")[0].trim();
				String vals = line.trim().split("\\[")[1].trim();

				vals = vals.substring(0, vals.length()-2);

				String[] values = vals.split(", ");

				HashMap<String,String> subRet = new HashMap<String,String>();

				for(String v : values) {

					subRet.put(v.split("=")[0].trim(), v.split("=")[1].trim().replaceAll("\"", ""));

				}

				ret.put(id, subRet);

			}


			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}

			return ret;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;

	}

	private String getDotCode(ArrayList<RawNode> nodes,ArrayList<RawEdge> edges) {


		String ret = "digraph sort_graph {\n graph [rankdir=\"BT\"];";

		Iterator<RawNode> i = nodes.iterator();

		while (i.hasNext()) {

			ret += getDotNodeString(i.next()) + "\n";

		}

		ret +="\n\n";

		Iterator<RawEdge> j = edges.iterator();

		while (j.hasNext()) {

			ret += getDotEdgeString(j.next()) + "\n";

		}



		ret +="}";
		return ret;



	}



	private String getDotNodeString(RawNode n) {

		double width = ((double)n.getWidth()) / 55; 
		double height = ((double)n.getHeight()) / 55;


		String ret ="";

		ret += "n_" + n.getNid() + " ";
		ret += "[shape=box fixedsize=true width=" + width + " height=" + height + " label=\"" + n.getText() + "\"];";

		return ret;	

	}


	private String getDotEdgeString(RawEdge e) {

		return "n_" + e.getFrom() + " -> n_" + e.getTo() + ";";


	}




}
