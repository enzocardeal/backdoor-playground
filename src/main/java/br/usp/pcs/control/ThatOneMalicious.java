package br.usp.pcs.control;

public class ThatOneMalicious extends Exception {
    // Backdoor by exception pt3
    public ThatOneMalicious(String str) {
        // calling the constructor of parent Exception
        super(str);
    }
}
