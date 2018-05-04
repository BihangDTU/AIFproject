package dataStructure;
public class Function extends AST {
	private String funName;
	private String num;
	public Function(String funName,String num){
		this.funName = funName;
		this.num = num;
	}

	public String getFunName() {
		return funName;
	}
	public void setFunName(String funName) {
		this.funName = funName;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return funName+"/"+num;
	}
	
}
