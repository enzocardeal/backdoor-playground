package janala.instrument.utils;

public class BranchExpection extends Exception{
    public BranchExpection(String errorMessage){
        super(errorMessage);
    }
    public void test (){
        try {
            throw new BranchExpection("mensagem");
        } catch (BranchExpection branchExpection) {
            branchExpection.printStackTrace();
        }
    }
}
