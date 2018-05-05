import dataStructure.*;
import myException.*;
import java.util.ArrayList;
import java.util.Map;


public class Mgu {
	public Mgu(){}
	DeepClone dClone = new DeepClone();
	
  public ArrayList<String> vars(Term term){    
    ArrayList<String> var = new ArrayList<>();
    if(term instanceof Variable){
      var.add(((Variable)term).getVarName());
      return var;
    }else if(term instanceof Composed){
      for(int i=0; i < term.getArguments().size(); i++){
        if(term.getArguments().get(i) instanceof Variable){
          var.add(((Variable)term.getArguments().get(i)).getVarName());
        }else if(term.getArguments().get(i) instanceof Composed){
          var.addAll(vars(term.getArguments().get(i)));
        }
      }
      return var;
    }		
    return var;
  }
  
  public Term termSubstituted(Term t, Substitution subs){
    Term t_copy = (Term)dClone.deepClone(t);
    if(!vars(t_copy).isEmpty()){
      if(t_copy instanceof Variable){
        if(subs.getSubstitution().containsKey((Variable)t_copy)){
          return subs.getSubstitution().get((Variable)t_copy);		
        }
      }else{
        for(int i=0; i < t_copy.getArguments().size(); i++){
          if(t_copy.getArguments().get(i) instanceof Variable){
            Variable var = (Variable)(t_copy.getArguments().get(i));
            if(subs.getSubstitution().containsKey(var)){
              t_copy.getArguments().add(i, subs.getSubstitution().get(var));
              t_copy.getArguments().remove(i+1);
            }
          }else{
            t_copy.getArguments().add(i, termSubstituted(t_copy.getArguments().get(i),subs));
            t_copy.getArguments().remove(i+1);
          }
        }
      }
    }
    return t_copy;
  }
  
  public Substitution mgu(Term s, Term t, Substitution subs) throws UnificationFailedException{
    if(s.equals(t)){
      return subs;
    }
    if(s instanceof Variable){
      if(vars(t).contains(((Variable)s).getVarName())){
        subs.setUnifierState(false);
        throw new UnificationFailedException();
      }else{
        subs.addSubstitution(((Variable)s).getVarName(), t);			
      }		
    }else if(t instanceof Variable){
      if(vars(s).contains(((Variable)t).getVarName())){
        subs.setUnifierState(false);
        throw new UnificationFailedException();
      }else{
        subs.addSubstitution(((Variable)t).getFactName(), s);
      }
    }else{
      if(((Composed)s).getFactName().equals(((Composed)t).getFactName()) && s.getArguments().size() == t.getArguments().size()){
        for(int i=0; i < s.getArguments().size(); i++){
          Term s_substitued = termSubstituted(s.getArguments().get(i),subs);
          Term t_substitued = termSubstituted(t.getArguments().get(i),subs);
          subs.addAllSubstitution(mgu(s_substitued,t_substitued,subs).getSubstitution());
        }
      }else{
        subs.setUnifierState(false);
        throw new UnificationFailedException();
      }
    }
    /*substitued Variables in Subtitution map if applicable*/
    for(Map.Entry sub : subs.getSubstitution().entrySet()){
      Term term = termSubstituted((Term)(sub.getValue()),subs);
      subs.addSubstitution(((Variable)(sub.getKey())).getVarName(), term);    
    }
    return subs;
  }

}
