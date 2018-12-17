package com.mycuckoo.vo;

import java.io.Serializable;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 10:59:31 AM
 */
public class CheckBoxTree extends SimpleTree implements Serializable {
    private static final long serialVersionUID = 10000L;
    private Boolean checked;
    @Deprecated
    private Boolean nocheck;

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    @Deprecated
    public Boolean isNocheck() {
        return nocheck;
    }

    @Deprecated
    public void setNocheck(Boolean nocheck) {
        this.nocheck = nocheck;
    }

}
