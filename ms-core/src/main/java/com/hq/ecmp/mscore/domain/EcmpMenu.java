package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 菜单权限对象 ecmp_menu
 *
 * @author hqer
 * @date 2020-01-02
 */
public class EcmpMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    private Long menuId;

    /** 菜单名称 */
    @Excel(name = "菜单名称")
    private String menuName;

    /** 父菜单ID */
    @Excel(name = "父菜单ID")
    private Long parentId;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 路由地址 */
    @Excel(name = "路由地址")
    private String path;

    /** 组件路径 */
    @Excel(name = "组件路径")
    private String component;

    /** 是否为外链（0是 1否） */
    @Excel(name = "是否为外链", readConverterExp = "0=是,1=否")
    private Integer isFrame;

    /** 菜单类型（M目录 C菜单 F按钮） */
    @Excel(name = "菜单类型", readConverterExp = "M=目录,C=菜单,F=按钮")
    private String menuType;

    /** 菜单状态（0显示 1隐藏） */
    @Excel(name = "菜单状态", readConverterExp = "0=显示,1=隐藏")
    private String visible;

    /** 权限标识 */
    @Excel(name = "权限标识")
    private String perms;

    /** 菜单图标 */
    @Excel(name = "菜单图标")
    private String icon;

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    public Long getMenuId()
    {
        return menuId;
    }
    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuName()
    {
        return menuName;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }
    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
    public void setComponent(String component)
    {
        this.component = component;
    }

    public String getComponent()
    {
        return component;
    }
    public void setIsFrame(Integer isFrame)
    {
        this.isFrame = isFrame;
    }

    public Integer getIsFrame()
    {
        return isFrame;
    }
    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getMenuType()
    {
        return menuType;
    }
    public void setVisible(String visible)
    {
        this.visible = visible;
    }

    public String getVisible()
    {
        return visible;
    }
    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    public String getPerms()
    {
        return perms;
    }
    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return icon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("component", getComponent())
            .append("isFrame", getIsFrame())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
