package io.github.sandy.ErrorCode;

public class Err{
    int errCode;
    String err;

    public Err(){}

    public Err(int errCode, String err) {
        this.errCode = errCode;
        this.err = err;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
