package containers;

import modles.Person;
import modles.QQMember;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class IocContainerTest {

    private  IocContainer ioc;
    @Before
    public void createIocContainer()  throws Exception {
        ioc = new IocContainer("./src/main/resources/jdom.xml");
    }

    @Test
    public void testGetBeanByIdViaConstructor() throws Exception {
        Person p1 =(Person) ioc.getBeanById("person1");
        assertThat(p1.getAge(), equalTo(90));

        assertThat(p1.getName(), equalTo("sunchen"));
    }

    @Test
    public void testGetBeanByIdViaSetter() throws Exception {
        Person p1 =(Person) ioc.getBeanById("person2");
        assertThat(p1.getAge(), equalTo(2));

        assertThat(p1.getName(), equalTo("sc"));
    }

    @Test
    public void testGetBeanByIdViaSetterWithRef() throws Exception {
        QQMember p1 =(QQMember) ioc.getBeanById("qqmember");
        assertThat(p1.getBase(), equalTo(10));
        Person p3 =(Person) ioc.getBeanById("person3");
        assertThat(p3.getMember().computePrice(),equalTo(10));

    }

    @Test
    public void testGetBeanByIdViaConstructorWithRef() throws Exception {
        Person p4 =(Person) ioc.getBeanById("person4");
        assertThat(p4.getMember().computePrice(),equalTo(10));

    }

    @Test
    public void testGetSameBeanAtTwice() throws Exception {
        Person p1 =(Person) ioc.getBeanById("person2");
        Person p2 =(Person) ioc.getBeanById("person2");
        assertThat(p1, is(p2));
    }
}