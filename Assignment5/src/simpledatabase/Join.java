package simpledatabase;
import java.util.ArrayList;
import java.util.Iterator;

public class Join extends Operator{
	private ArrayList<Attribute> newAttributeList;
	private String joinPredicate;
	private ArrayList<Tuple> tuples1;
	private ArrayList<Tuple> leftChildTuples = new ArrayList<Tuple>();
	private Iterator<Tuple> leftTupleIterator = leftChildTuples.iterator();
	private Tuple rightTuple;
	private String joinName = null;
	private int leftJoinIndex = -1, rightJoinIndex = -1;
	private ArrayList<String> skipNames = new ArrayList<String>();
	private Boolean inited = false;
	
	public Join(Operator leftChild, Operator rightChild, String joinPredicate){
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.joinPredicate = joinPredicate;
		newAttributeList = new ArrayList<Attribute>();
		tuples1 = new ArrayList<Tuple>();
		
	}

	private void getLeftTuples(){
		Tuple tuple;
		while((tuple = leftChild.next())!=null){
			leftChildTuples.add(tuple);
		}
		this.leftTupleIterator = leftChildTuples.iterator();
	}

	private void init(){
		getLeftTuples(); //get leftChild tuples
		rightTuple = rightChild.next();
		ArrayList<Attribute> leftAttributes = leftChildTuples.get(0).getAttributeList();
		ArrayList<Attribute> rightAttributes = rightTuple.getAttributeList();
		for(int i = 0; i < rightAttributes.size(); i++){
			Attribute rightAttribute = rightAttributes.get(i);
			for(int j = 0; j < leftAttributes.size(); j++){
				Attribute leftAttribute = leftAttributes.get(j);
				//System.out.println("[Join] leftName: "+leftAttribute.getAKttributeName()+", rightName: "+rightAttribute.getAttributeName());
				if(leftAttribute.getAttributeName().equals(rightAttribute.getAttributeName())){
					if(joinName == null){
						joinName = rightAttribute.getAttributeName();
						skipNames.add(rightAttribute.getAttributeName());
						leftJoinIndex = j;
						rightJoinIndex = i;
					} else{
						skipNames.add(rightAttribute.getAttributeName());
					}
				}
			}
		}
		inited = true;
		return;
	}

	/**
     * It is used to return a new tuple which is already joined by the common attribute
     * @return the new joined tuple
     */
	//The record after join with two tables
	@Override
	public Tuple next(){
		if(!inited) init();
		if(rightTuple != null){
			ArrayList<Attribute> rightAttributeList = rightTuple.getAttributeList();
			while(true){
				while(leftTupleIterator.hasNext()){
					Tuple leftTuple = leftTupleIterator.next();
					ArrayList<Attribute> leftAttributeList = leftTuple.getAttributeList();
					if (leftAttributeList.get(leftJoinIndex).getAttributeValue().equals(rightAttributeList.get(rightJoinIndex).getAttributeValue())){
						mergeRecord(leftAttributeList, rightAttributeList);
						rightTuple = rightChild.next();
						return new Tuple(newAttributeList);
					}
				}
				leftTupleIterator = leftChildTuples.iterator();
			}
		}
		return null;
	}
	
	private void mergeRecord(ArrayList<Attribute> leftAttributeList, ArrayList<Attribute> rightAttributeList){
		newAttributeList = rightAttributeList;
		for(Attribute attribute : leftAttributeList){
			if(!skipNames.contains(attribute.getAttributeName())) newAttributeList.add(attribute);
		}
		return;
	}
	
	/**
     * The function is used to get the attribute list of the tuple
     * @return attribute list
     */
	public ArrayList<Attribute> getAttributeList(){
		if(joinPredicate.isEmpty())
			return child.getAttributeList();
		else
			return(newAttributeList);
	}

}