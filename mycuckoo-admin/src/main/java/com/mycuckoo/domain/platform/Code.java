package com.mycuckoo.domain.platform;

import com.mycuckoo.domain.BasicDomain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:00:36 PM
 */
public class Code extends BasicDomain<Long> {

    private Long codeId; //编码ID
    private String code; //英文编码名称
    private String name; //中文编码名称
    private String moduleName; // 模块名称
    private String delimiter; //delimiter分隔符
    private Integer partNum; //段数
    private String part1;
    private String part1Con;
    private String part2;
    private String part2Con;
    private String part3;
    private String part3Con;
    private String part4;
    private String part4Con;
    private String effect; //编码效果
    private String memo; //备注
    private String status; //状态

    private List<String> partList;
    private List<String> partConList;

    /**
     * default constructor
     */
    public Code() {
    }

    /**
     * minimal constructor
     */
    public Code(Long codeId, String status) {
        this.codeId = codeId;
        this.status = status;
    }


    public Long getCodeId() {
        return this.codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public Integer getPartNum() {
        return this.partNum;
    }

    public void setPartNum(Integer partNum) {
        this.partNum = partNum;
    }

    public String getPart1() {
        return this.part1;
    }

    public void setPart1(String part1) {
        this.part1 = part1;
    }

    public String getPart1Con() {
        return this.part1Con;
    }

    public void setPart1Con(String part1Con) {
        this.part1Con = part1Con;
    }

    public String getPart2() {
        return this.part2;
    }

    public void setPart2(String part2) {
        this.part2 = part2;
    }

    public String getPart2Con() {
        return this.part2Con;
    }

    public void setPart2Con(String part2Con) {
        this.part2Con = part2Con;
    }

    public String getPart3() {
        return this.part3;
    }

    public void setPart3(String part3) {
        this.part3 = part3;
    }

    public String getPart3Con() {
        return this.part3Con;
    }

    public void setPart3Con(String part3Con) {
        this.part3Con = part3Con;
    }

    public String getPart4() {
        return this.part4;
    }

    public void setPart4(String part4) {
        this.part4 = part4;
    }

    public String getPart4Con() {
        return this.part4Con;
    }

    public void setPart4Con(String part4Con) {
        this.part4Con = part4Con;
    }

    public String getEffect() {
        return this.effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getPartList() {
        return partList;
    }

    public void setPartList(List<String> partList) {
        this.partList = partList;
    }

    public List<String> getPartConList() {
        return partConList;
    }

    public void setPartConList(List<String> partConList) {
        this.partConList = partConList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
