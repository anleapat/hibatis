package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author lee
 * @date Oct 20, 2016
 *
 */
@Entity
@Table(name = "dept")
@NamedQuery(name = "Dept.findAll", query = "SELECT t FROM Dept t")
public class Dept implements Serializable
{
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dept_no")
    private String deptNo;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "dept_addr")
    private String deptAddr;

    public Dept()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDeptNo()
    {
        return deptNo;
    }

    public void setDeptNo(String deptNo)
    {
        this.deptNo = deptNo;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptAddr()
    {
        return deptAddr;
    }

    public void setDeptAddr(String deptAddr)
    {
        this.deptAddr = deptAddr;
    }

}
