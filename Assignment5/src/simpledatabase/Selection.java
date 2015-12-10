package simpledatabase;
import java.util.ArrayList;

public class Selection extends Operator{
	ArrayList<Attribute> attributeList;
	String whereTablePredicate;
	String whereAttributePredicate;
	String whereValuePredicate;
	private Boolean select = null;
	private Boolean inited = false;
	
	public Selection(Operator child, String whereTablePredicate, String whereAttributePredicate, String whereValuePredicate) {
		this.child = child;
		this.whereTablePredicate = whereTablePredicate;
		this.whereAttributePredicate = whereAttributePredicate;
		this.whereValuePredicate = whereValuePredicate;
		attributeList = new ArrayList<Attribute>();

	}
	
	private void init(){
		try{
			select = (((Table)child).from().equals(whereTablePredicate));
		}catch (Exception e){
			select = false;
		}
		inited = true;
		return;
	}
	
	/**
     * Get the tuple which match to the where condition
     * @return the tuple
     */
	@Override
	public Tuple next(){
		if(!inited) init();
		Tuple tuple = child.next();
		while(tuple!=null && select){
			ArrayList<Attribute> attributes= tuple.getAttributeList();
			for(Attribute attribute : attributes){
				if(attribute.getAttributeName().equals(whereAttributePredicate)
					&& attribute.getAttributeValue().equals(whereValuePredicate)){
					return tuple;
				}
			}
			tuple = child.next();
		}
		return tuple;	
	}
	
	/**
     * The function is used to get the attribute list of the tuple
     * @return the attribute list
     */
	public ArrayList<Attribute> getAttributeList(){
		return(child.getAttributeList());
	}
}