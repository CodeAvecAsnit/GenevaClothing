package com.ecomm.np.genevaecommerce.DTO;

public class SignUpAttempt {
    private int code;
    private int attempts;
    private int mailsSent;

    public SignUpAttempt() {
    }

    public SignUpAttempt(int code) {
        this.code = code;
        this.attempts = 0;
        this.mailsSent = 0;
    }

    private boolean check(int verificationCode){
        return this.code==verificationCode;
    }

    public int verificationAttempt(int vCode){
        ++attempts;
        if(attempts>5){
            return -1;
        }
        return check(vCode)?1:0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }


    public boolean canSendMail(){
        if(this.mailsSent>=5){
            return false;
        }else ++attempts;
        return true;
    }

}
