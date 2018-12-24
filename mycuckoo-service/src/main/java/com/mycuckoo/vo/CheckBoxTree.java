package com.mycuckoo.vo;

/**
 * 功能说明:
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 23, 2014 10:59:31 AM
 */
public class CheckBoxTree extends SimpleTree {
    private Boolean checked;
    private Boolean nocheck;
    private CheckBox checkBox;

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

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public static class CheckBox {
        private int type = 0;
        private Integer isChecked;

        public CheckBox() {}

        public CheckBox(Integer isChecked) {
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
