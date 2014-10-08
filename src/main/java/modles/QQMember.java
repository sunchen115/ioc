package modles;

import interfaces.MemberInfo;

/**
 * Created by csun on 10/7/14.
 */
public class QQMember implements MemberInfo {
    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    private int base;

    @Override
    public int computePrice() {
        return  base;
    }
}
