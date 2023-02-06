package janala.instrument;

import janala.instrument.utils.BranchExpection;

public class Test {
    public void test (){
        try {
            throw new BranchExpection("mensagem");
        } catch (BranchExpection branchExpection) {
            branchExpection.printStackTrace();
        }
    }
}
