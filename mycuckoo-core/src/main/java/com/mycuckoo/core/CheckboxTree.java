package com.mycuckoo.core;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 10:59:31 AM
 */
public class CheckboxTree extends SimpleTree<CheckboxTree> {
    private Boolean checked; //ztree
    private Boolean nocheck;
    private Checkbox checkbox;

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(Boolean nocheck) {
        this.nocheck = nocheck;
    }

    public Boolean getChecked() {
        return checked;
    }

    public Boolean getNocheck() {
        return nocheck;
    }

    public Checkbox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(Checkbox checkbox) {
        this.checkbox = checkbox;
    }

    public static class Checkbox {
        private int type = 0;
        private Integer isChecked;

        public Checkbox() {}

        public Checkbox(Integer isChecked) {
            this.isChecked = isChecked;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Integer getIsChecked() {
            return isChecked;
        }

        public void setIsChecked(Integer isChecked) {
            this.isChecked = isChecked;
        }
    }
}
