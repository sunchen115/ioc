package containers;

import configure.ClassInfo;
import configure.XmlConfigure;
import modles.Person;
import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by csun on 10/6/14.
 */
public class IocContainer {

    private static XmlConfigure configure=null;
    private HashMap<String,Object> mapIdToObj=new HashMap<String, Object>();
    public IocContainer(String fileName) throws Exception {
        configure = new XmlConfigure(fileName);

    }


    public  static  void log(Object obj){
        System.out.println(obj);

    }
    public Object getBeanById(String id){
        if (mapIdToObj.containsKey(id)){return mapIdToObj.get(id);}
        ClassInfo configInfo =getClassinfoById(id);
        Object returnBean = null;
        if (configInfo.isRejectBySetter()){
            returnBean= getBeanBySetter(configInfo);
        }
        else{
            returnBean= getBeanByConstructor(configInfo);
        }
        mapIdToObj.put(id,returnBean);
        return returnBean;
    }

    private ClassInfo getClassinfoById(String id){
        return configure.getClassInfoes().get(id);
    }

    private String getClassNameById(String id){
        return getClassinfoById(id).getClassName();
    }

    private Object getBeanBySetter (ClassInfo clsInfo){
        try {
            Class<?> objClass = Class.forName(clsInfo.getClassName());
            Object obj = objClass.newInstance();
            List props = clsInfo.getProperties();
            for(int i=0;i<props.size();i++){
                Element prop = (Element)props.get(i);
                String propName = prop.getAttributeValue("name");
                String setMethodName = getSetterName(propName);
                Class paramTypes[]= new Class[1];
                Object params[] = new Object[1];
                Attribute attr_type = prop.getAttribute("type");

                if (attr_type!= null && attr_type.getValue().equals("int")){
                    paramTypes[0]=int.class;
                    params[0] = (int)(Integer.valueOf((String)getValueForProperty(prop)));
                }
                else if (prop.getAttribute("value")!=null){
                    paramTypes[0] = String.class;
                    params[0] = getValueForProperty(prop);

                }
                else{
                    //object ref
                    paramTypes[0] = Class.forName(getClassNameById(prop.getAttributeValue("ref"))).getInterfaces()[0];
                    params[0] = getValueForProperty(prop);
                }

                //find method by name and param type
                Method set_func = objClass.getMethod(setMethodName,paramTypes);
                set_func.invoke(obj,params);
            }
            return obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  null;
    }



    private Object getBeanByConstructor(ClassInfo clsInfo) {

        try {
            Class<?> objClass = Class.forName(clsInfo.getClassName());
            Object obj = objClass.newInstance();
            List constructorParams = clsInfo.getConstructorParams();
            Object params[] = new Object[constructorParams.size()];

            Class paramTypes[] = new Class[ constructorParams.size()];
            for (int i = 0; i < constructorParams.size(); i++) {
                Element parameter = (Element)constructorParams.get(i);
                Attribute attrType = parameter.getAttribute("type");
                if (attrType != null && attrType.getValue().equals("int")){
                    paramTypes[i] = int.class;
                    params[i] =(int)Integer.valueOf((String)getValueForContructor(parameter));
                }
                else if (parameter.getChild("value")!=null){
                    paramTypes[i]=String.class;
                    params[i] = getValueForContructor(parameter);
                }
                else {
                    //ref
                    paramTypes[i] = Class.forName(getClassNameById(parameter.getChildText("ref"))).getInterfaces()[0];
                    params[i] = getValueForContructor(parameter);
                }
            }
            Constructor<?> cons  = objClass.getConstructor(paramTypes);
            Object ins = cons.newInstance(params);
            return ins;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSetterName(String property){
        StringBuilder prop = new StringBuilder(property.toLowerCase());
        prop.setCharAt(0,Character.toUpperCase(prop.charAt(0)));
        return "set"+prop.toString();
    }


    private Object getValueForProperty(Element prop){
        if (prop.getAttribute("value")!=null){
            return prop.getAttributeValue("value");
        }
        else{
            //ref
            String ref_id = prop.getAttributeValue("ref");
            Object ref = getBeanById(ref_id);
            return ref;
        }

    }

    private Object getValueForContructor(Element prop){
        if (prop.getChild("value")!=null){
            return prop.getChildText("value").trim();
        }
        else{
            //ref
            String ref_id = prop.getChildText("ref");
            Object ref = getBeanById(ref_id);
            return ref;
        }

    }

    public static  void main(String[] args) throws Exception{
       IocContainer ioc = new IocContainer("./src/jdom.xml");
        Person p1= (Person)ioc.getBeanById("person1");
        log("By Constructor: name="+p1.getName()+";age="+p1.getAge());
        Person p2 = (Person)ioc.getBeanById("person2");
        log("By Setter: name="+p2.getName()+";age="+p2.getAge());
    }

}
