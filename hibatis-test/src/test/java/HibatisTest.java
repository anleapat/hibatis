import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibatis.db.HbFactory;
import org.hibatis.db.HbFactoryImpl;
import org.hibatis.utils.MapToPo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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

    private HbFactory hbFactory;
    
    private Session session;

    @Before
    public void before()
    {
        SessionFactory sessionFactory = new Configuration().configure().addAnnotatedClass(Emp.class).addAnnotatedClass(Dept.class).buildSessionFactory();
        this.session = sessionFactory.openSession();
        this.hbFactory = new HbFactoryImpl(session);
    }

    @Test
    public void insert()
    {
        session.getTransaction().begin();
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
        session.getTransaction().commit();
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
//        parameter.put("empname", "hibatis2");
//        parameter.put("email", "2@2.2");
        parameter.put("sex", "male");
        List <Map <String, Object>> list = hbFactory.select("com.hibatis.hibatis-config.dynamicSel", parameter);
        List<JoinPo> pos = MapToPo.mapToPo(list, JoinPo.class);
        for (JoinPo joinPo : pos)
        {
            System.out.println(joinPo);
        }
    }
}
