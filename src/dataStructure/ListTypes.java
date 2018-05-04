package dataStructure;
import java.util.List;


public class ListTypes extends AST {
	private List<String> types;
	
	public ListTypes(List<String> types) {
		this.types = types;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
}
