package simpledatabase;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Table extends Operator{
	private BufferedReader br = null;
	private boolean getAttribute=false;
	private Tuple tuple=null;
	private String attributeLine, dataTypeLine;

	public Table(String from){
		this.from = from;
		//Create buffer reader
		try{
			br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/datafile/"+from+".csv")));
				attributeLine = br.readLine();
				dataTypeLine = br.readLine();
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	
	/**
     * Create a new tuple and return the tuple to its parent.
     * Set the attribute list if you have not prepare the attribute list
     * @return the tuple
     */
	@Override
	public Tuple next(){
		String tupleLine;
		try{
			tupleLine = br.readLine();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(tupleLine!=null){
			tuple = new Tuple(attributeLine, dataTypeLine, tupleLine);
			tuple.setAttributeName();
			tuple.setAttributeType();
			tuple.setAttributeValue();
			return tuple;
		}
		return null;
	}
	

	/**
     * The function is used to get the attribute list of the tuple
     * @return the attribute list
     */
	public ArrayList<Attribute> getAttributeList(){
		if (tuple == null){
			Tuple tuple;
			tuple = new Tuple(attributeLine, dataTypeLine, "");
			tuple.setAttributeName();
			tuple.setAttributeType();
			return tuple.getAttributeList();
		}
		return tuple.getAttributeList();
	}
	public Operator getChild(){
		return null;
	}
	public String from(){
		return this.from;
	}
	
}
