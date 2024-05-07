package com.mycuckoo.domain.platform;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 功能说明: 域对象
 *
 * @author rutine
 * @version 3.0.0
 * @time Sep 22, 2014 8:59:44 PM
 */
public class Affiche implements Serializable {

    private Long afficheId; //公告ID
    private String title; //公告标题
    private String content; //公告内容
    private Date invalidate; //公告有效期
    private Boolean publish; //是否发布
    private String creator; //创建人
    private Date createDate; //创建时间
    private List<Accessory> accessories; //公告附件添加

    /**
     * default constructor
     */
    public Affiche() {
    }

    /**
     * minimal constructor
     */
    public Affiche(Long afficheId) {
        this.afficheId = afficheId;
    }

    /**
     * full constructor
     */
    public Affiche(Long afficheId, String title, String content,
                   Date invalidate, Boolean publish, String creator, Date createDate) {
        this.afficheId = afficheId;
        this.title = title;
        this.content = content;
        this.invalidate = invalidate;
        this.publish = publish;
        this.creator = creator;
        this.createDate = createDate;
    }

    public Long getAfficheId() {
        return this.afficheId;
    }

    public void setAfficheId(Long afficheId) {
        this.afficheId = afficheId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getInvalidate() {
        return invalidate;
    }

    public void setInvalidate(Date invalidate) {
        this.invalidate = invalidate;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
