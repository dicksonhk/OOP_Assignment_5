package simpledatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;

public class Sort extends Operator{
	private ArrayList<Attribute> newAttributeList;
	private String orderPredicate;
	private ArrayList<Tuple> tuplesResult;
	private Iterator<Tuple> tuplesResultIterator;
	private	int orderAttIndex = -1;
	private Boolean inited = false;
	
	public Sort(Operator child, String orderPredicate){
		this.child = child;
		this.orderPredicate = orderPredicate;
		newAttributeList = new ArrayList<Attribute>();
		tuplesResult = new ArrayList<Tuple>();

	}

	private void init(){
		tuplesResultIterator = tuplesResult.iterator();
		getChildTuples();
		getOrderAttIndex();
		sortTuples();
		inited = true;
		return;
	}

	private void getChildTuples(){
		Tuple tuple;
		while((tuple = child.next())!=null){
			tuplesResult.add(tuple);
		}
		this.tuplesResultIterator = tuplesResult.iterator();
	}

	private void getOrderAttIndex(){
		ArrayList<Attribute> attributes = tuplesResult.get(0).getAttributeList();
		for(int i = 0; i<attributes.size(); i++){
			if(attributes.get(i).getAttributeName().equals(orderPredicate)){
				orderAttIndex = i;
				return;
			}
		}
	}

	private void sortTuples(){
		tuplesResult.sort((Tuple tupleX, Tuple tupleY) -> String.valueOf(tupleX.getAttributeValue(orderAttIndex)).compareTo(String.valueOf(tupleY.getAttributeValue(orderAttIndex))));
		this.tuplesResultIterator = tuplesResult.iterator();
	}
	
	/**
	 * The function is used to return the sorted tuple
	 * @return tuple
	 */
	@Override
	public Tuple next(){
		if(!inited) init();
		if(tuplesResultIterator.hasNext()){
			return tuplesResultIterator.next();
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