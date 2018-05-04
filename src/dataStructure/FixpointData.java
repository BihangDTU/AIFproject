package dataStructure;
import java.util.HashMap;
import java.util.Map;

public class FixpointData extends AST {
	private HashMap<Integer, Fixpoint> fixpoints = new HashMap<>();

	public FixpointData(HashMap<Integer, Fixpoint> fixpoints) {
		super();
		this.fixpoints = fixpoints;
	}

	public HashMap<Integer, Fixpoint> getFixpoints() {
		return fixpoints;
	}

	public void setFixpoints(HashMap<Integer, Fixpoint> fixpoints) {
		this.fixpoints = fixpoints;
	}

	@Override
	public String toString() {
		String s="";
		for (Map.Entry<Integer, Fixpoint> entry : fixpoints.entrySet()) {
			Fixpoint fp = entry.getValue();
	    s += fp.toString() + ";\n";
		}
		return s;
	}
	
}
