package dataStructure;
import java.util.ArrayList;
import java.util.List;

public class NumList extends AST {
	private List<Num> nums = new ArrayList<Num>();

	public NumList(List<Num> nums) {
		super();
		this.nums = nums;
	}

	public List<Num> getNums() {
		return nums;
	}

	public void setNums(List<Num> nums) {
		this.nums = nums;
	}
	
}
