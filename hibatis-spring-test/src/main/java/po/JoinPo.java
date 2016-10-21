package po;

import java.util.Date;

/**
 * 
 * @author lee
 * @date Oct 20, 2016
 *
 */
public class JoinPo
{
    private Long id;

    private Long deptId;

    private String empName;

    private String address;

    private Date birthday;

    private String email;

    private String mobile;

    private String phone;

    private String sex;

    private String deptNo;

    private String deptName;

    private String deptAddr;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getEmpName()
    {
        return empName;
    }

    public void setEmpName(String empName)
    {
        this.empName = empName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
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

    @Override
    public String toString()
    {
        return "JoinPo [id=" + id + ", deptId=" + deptId + ", empName=" + empName + ", address=" + address + ", birthday=" + birthday + ", email=" + email
                + ", mobile=" + mobile + ", phone=" + phone + ", sex=" + sex + ", deptNo=" + deptNo + ", deptName=" + deptName + ", deptAddr=" + deptAddr + "]";
    }

}
