package br.usp.pcs.backdoor;

public class ThatOneMaliciousException extends Exception {
    public ThatOneMaliciousException(String str) {
        // calling the constructor of parent Exception
        super(str);
        System.out.println("Backdoor of malicious exception!");
    }
}
