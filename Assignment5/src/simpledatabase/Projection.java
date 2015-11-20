package simpledatabase;
import java.util.ArrayList;

public class Projection extends Operator{
	ArrayList<Attribute> newAttributeList;
	private String attributePredicate;
	private String[] predicates;

	public Projection(Operator child, String attributePredicate){
		this.attributePredicate = attributePredicate;
		this.child = child;
		newAttributeList = new ArrayList<Attribute>();
		predicates = attributePredicate.split(",");
	}
	
	
	/**
     * Return the data of the selected attribute as tuple format
     * @return tuple
     */
	@Override
	public Tuple next(){
		ArrayList<Attribute> newAttributeList = new ArrayList<Attribute>();
		Tuple tuple = child.next();			
		if(tuple!=null){
			ArrayList<Attribute> attributeList = tuple.getAttributeList();
			if(attributeList!=null){
				if (attributePredicate.equals("*")){
					return tuple;
				}
				for(String predicate : predicates){
					for(Attribute attribute : attributeList){
						if(predicate.equals(attribute.getAttributeName())){
							newAttributeList.add(attribute);
						}
					}
				}
				if(newAttributeList.size()>0){
					return new Tuple(newAttributeList);
				}
			}
		}
		return null;		
	}
		

	
	/**
     * The function is used to get the attribute list of the tuple
     * @return attribute list
     */
	public ArrayList<Attribute> getAttributeList(){
		return child.getAttributeList();
	}

}