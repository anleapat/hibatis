import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibatis.db.HbFactory;
import org.hibatis.db.HbFactoryImpl;
import org.hibatis.utils.MapToPo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entity.Dept;
import entity.Emp;
import po.JoinPo;

/**
 * 
 * @author lee
 * @date Oct 20, 2016
 *
 */
public class HibatisTest
{

    private HbFactoryImpl hbFactory;

    @Before
    public void before()
    {
        SessionFactory sessionFactory = new Configuration().configure().addAnnotatedClass(Emp.class).addAnnotatedClass(Dept.class).buildSessionFactory();

        HbFactoryImpl hbFactoryImpl = new HbFactoryImpl(sessionFactory);
        this.hbFactory = hbFactoryImpl;
    }

    @Test
    public void insert()
    {
        hbFactory.getSession().getTransaction().begin();
        for (int i = 1; i < 14; i++)
        {
            Dept dept = new Dept();
            dept.setDeptAddr("China" + i);
            dept.setDeptName("SH" + i);
            dept.setDeptNo("DEPT" + i);
            hbFactory.insert(dept);

            Emp emp = new Emp();
            emp.setEmpName("hibatis" + i);
            emp.setDeptId(dept.getId());
            emp.setAddress("Shanghai");
            emp.setBirthday(new Date());
            emp.setEmail(i + "@" + i + "." + i);
            emp.setMobile("12" + i);
            String sex = "male";
            if (i % 2 == 0)
            {
                sex = "female";
            }
            emp.setSex(sex);
            hbFactory.insert(emp);
        }
        hbFactory.getSession().getTransaction().commit();
    }

    @Test
    public void select()
    {
        Emp e = new Emp();
        List <Emp> list = hbFactory.select(e);
        for (Emp emp : list)
        {
            System.out.println(emp);
        }
    }

    @Test
    public void selectStaticSql()
    {
        List <Map <String, Object>> list = hbFactory.select("com.hibatis.hibatis-config.staticSel", null);
        for (Map <String, Object> map : list)
        {
            System.out.println(map);
        }
    }

    @Test
    public void selectRawSql()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("empname", "hibatis2");
        parameter.put("email", "2@2.2");
        List <Map <String, Object>> list = hbFactory.select("com.hibatis.hibatis-config.rawSel", parameter);
        for (Map <String, Object> map : list)
        {
            System.out.println(map);
        }
    }

    @Test
    public void selectDynamicSql()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("empname", "hibatis2");
        parameter.put("email", "2@2.2");
        parameter.put("empname222", "hibatis2");
        List <Map <String, Object>> list = hbFactory.select("com.hibatis.hibatis-config.dynamicSel", parameter);
        for (Map <String, Object> map : list)
        {
            System.out.println(map);
        }
    }

    @Test
    public void selectSqlLimit()
    {
        List <Map <String, Object>> list = hbFactory.select("com.hibatis.hibatis-config.staticSel", null, 3, 10);
        for (Map <String, Object> map : list)
        {
            System.out.println(map);
        }
    }

    @Test
    public void insertHibatis()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("id", 41);
        parameter.put("address", "address");
        parameter.put("birthday", new Date());
        parameter.put("dept_id", 1);
        parameter.put("email", "email");
        parameter.put("empname", "test insert");
        parameter.put("mobile", "123456");
        parameter.put("phone", null);
        parameter.put("sex", "male");
        hbFactory.insert("com.hibatis.hibatis-config.hbatisInsert", parameter);
    }

    @Test
    public void delete()
    {
        Emp emp = new Emp();
        emp.setId(41L);
        emp.setAddress("address");
        hbFactory.delete(emp);
    }

    @Test
    public void deleteHibatis()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("id", 41);
        parameter.put("address", "address");
        parameter.put("birthday", new Date());
        parameter.put("dept_id", 1);
        hbFactory.delete("com.hibatis.hibatis-config.hibatisDel", parameter);
    }

    @Test
    public void update()
    {
        Emp where = new Emp();
        Emp set = new Emp();
        where.setEmail("13@13.13");
        where.setId(26L);
        set.setAddress("Shanghai");
        set.setSex("male");
        hbFactory.update(where, set);
    }

    @Test
    public void updateHibatis()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("id", 26);
        parameter.put("address", "Guangzhou");
        parameter.put("email", "13@13.13");
        parameter.put("empname", "test insert");
        parameter.put("mobile", "123456");
        parameter.put("phone", "021");
        parameter.put("sex", "male");
        hbFactory.update("com.hibatis.hibatis-config.hibatisUpdate", parameter);
    }

    @Test
    public void mapToPo()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        // parameter.put("empname", "hibatis2");
        // parameter.put("email", "2@2.2");
        parameter.put("sex", "male");
        List <Map <String, Object>> list = hbFactory.select("com.hibatis.hibatis-config.dynamicSel", parameter);
        List <JoinPo> pos = MapToPo.mapToPo(list, JoinPo.class);
        for (JoinPo joinPo : pos)
        {
            System.out.println(joinPo);
        }
    }

    @Test
    public void testEntityField()
    {
        Field[] fields = Emp.class.getDeclaredFields();
        for (Field field : fields)
        {
            String fn = field.getName();
            if ("serialVersionUID".equals(fn))
            {
                continue;
            }
            System.out.println("<if test=\"" + fn + " != null\">");
            System.out.println("\tand " + fn + " = :" + fn);
            System.out.println("</if>");
        }
    }

    @Test
    public void hqlSelect()
    {
        List <Emp> list = hbFactory.selectByHql("com.hibatis.hibatis-config.hqlSelect", null);
        for (Emp emp : list)
        {
            System.out.println(emp);
        }
    }

    @Test
    public void hqlRecordCount()
    {
        // Query query = hbFactory.getSession().createQuery("from (select e from
        // Emp e)");
        // List list = query.list();
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("address", "Shanghai");
        int rc = hbFactory.recordCountByHql("com.hibatis.hibatis-config.hqlSelect", parameter);
        System.out.println(rc);
    }

    @Test
    public void hqlJoinSelect()
    {
        Map <String, Object> parameter = new HashMap <String, Object>();
        parameter.put("deptNo", "DEPT1");
        List <JoinPo> list = hbFactory.selectByHql("com.hibatis.hibatis-config.hqlJoinSelect", parameter);
        for (JoinPo po : list)
        {
            System.out.println(po);
        }
    }

}
